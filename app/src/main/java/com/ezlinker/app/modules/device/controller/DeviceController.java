package com.ezlinker.app.modules.device.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ezlinker.app.common.AbstractXController;
import com.ezlinker.app.config.socketio.S2CMessage;
import com.ezlinker.app.constants.DeviceState;
import com.ezlinker.app.modules.device.model.Device;
import com.ezlinker.app.modules.device.pojo.DeviceStatus;
import com.ezlinker.app.modules.device.pojo.FieldParam;
import com.ezlinker.app.modules.device.service.IDeviceService;
import com.ezlinker.app.modules.module.model.Module;
import com.ezlinker.app.modules.module.service.IModuleService;
import com.ezlinker.app.modules.mqtttopic.model.MqttTopic;
import com.ezlinker.app.modules.mqtttopic.service.IMqttTopicService;
import com.ezlinker.app.modules.product.model.Product;
import com.ezlinker.app.modules.product.service.IProductService;
import com.ezlinker.app.modules.tag.model.Tag;
import com.ezlinker.app.modules.tag.service.ITagService;
import com.ezlinker.app.utils.IDKeyUtil;
import com.ezlinker.common.exception.BizException;
import com.ezlinker.common.exception.XException;
import com.ezlinker.common.exchange.R;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * <p>
 * 实际设备，是产品的一个实例。 前端控制器
 * </p>
 *
 * @author wangwenhai
 * @since 2019-11-19
 */
@RestController
@RequestMapping("/devices")
public class DeviceController extends AbstractXController<Device> {
    // 发布权限
    private static final int TOPIC_PUB = 1;
    // 订阅权限
    private static final int TOPIC_SUB = 2;

    @Resource
    IModuleService iModuleService;
    @Resource
    IDeviceService iDeviceService;

    @Resource
    IProductService iProductService;
    @Resource
    ITagService iTagService;
    @Resource
    IMqttTopicService iMqttTopicService;

    @Resource
    IDKeyUtil idKeyUtil;


    public DeviceController(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
    }

    /**
     * 新建一个设备
     *
     * @param form
     * @return
     * @throws XException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    protected R add(@RequestBody @Valid Device form) throws XException {

        Product product = iProductService.getById(form.getProductId());

        if (product == null) {
            throw new BizException("Product not exists", "该产品不存在!");
        }

        Device device = new Device();
        device.setName(form.getName())
                .setName(form.getName())
                .setDescription(form.getDescription())
                .setLocation(form.getLocation())
                .setModel(form.getModel())
                .setIndustry(form.getIndustry())
                //继承
                .setParameters(product.getParameters())
                .setProductId(product.getId())
                .setProjectId(product.getProjectId())
                .setLogo(product.getLogo())
                .setSn("SN" + idKeyUtil.nextId());

        // 建立 设备和模块的关系表
        List<Module> moduleList = iModuleService.list(new QueryWrapper<Module>().eq("product_id", product.getId()));
        for (Module module : moduleList) {
            // Clone 新的Module
            Module newModule = new Module();
            BeanUtil.copyProperties(module, newModule);
            String clientId = IDKeyUtil.generateId().toString();
            String username = SecureUtil.md5(clientId);
            String password = SecureUtil.md5(username);
            newModule.setClientId(clientId).setUsername(username).setPassword(password);
            iModuleService.save(newModule);
            // 给新的Module创建Topic
            // 数据上行
            MqttTopic s2cTopic = new MqttTopic();
            s2cTopic.setAccess(TOPIC_SUB)
                    .setType(MqttTopic.S2C)
                    .setClientId(clientId)
                    .setDeviceId(device.getId())
                    .setTopic("mqtt/out/" + device.getId() + "/" + clientId + "/s2c")
                    .setUsername(username)
                    .setDescription("服务端消息入口");
            // 数据下行
            MqttTopic c2sTopic = new MqttTopic();
            c2sTopic.setAccess(TOPIC_PUB)
                    .setType(MqttTopic.C2S)
                    .setDeviceId(device.getId())
                    .setClientId(clientId)
                    .setTopic("mqtt/in/" + device.getId() + "/" + clientId + "/c2s")
                    .setUsername(username)
                    .setDescription("服务端消息出口");
            // 状态上报
            MqttTopic statusTopic = new MqttTopic();
            statusTopic.setAccess(TOPIC_PUB)
                    .setType(MqttTopic.STATUS)
                    .setUsername(username)
                    .setClientId(clientId)
                    .setDeviceId(device.getId())
                    .setTopic("mqtt/in/" + device.getId() + "/" + clientId + "/status")
                    .setDescription("状态上报入口");
            //生成
            iMqttTopicService.save(s2cTopic);
            iMqttTopicService.save(c2sTopic);
            iMqttTopicService.save(statusTopic);

        }


        // 保存设备
        boolean ok = iDeviceService.save(device);

        return ok ? data(device) : fail();
    }

    /**
     * 更新
     *
     * @param id
     * @param newDevice
     * @return
     * @throws XException
     */
    @PutMapping("/{id}")
    @Override
    protected R update(@PathVariable Long id, @RequestBody Device newDevice) throws XException {
        Device device = iDeviceService.getById(id);

        if (device == null) {
            throw new BizException("Device not exists", "该设备不存在!");
        }
        device.setName(newDevice.getName())
                .setName(newDevice.getName())
                .setDescription(newDevice.getDescription())
                .setLocation(newDevice.getLocation())
                .setModel(newDevice.getModel());

        iDeviceService.updateById(device);

        return data(device);
    }

    /**
     * 设备详情
     *
     * @param id
     * @return
     * @throws XException
     */
    @Override
    protected R get(@PathVariable Long id) throws XException {
        Device device = iDeviceService.getById(id);
        if (device == null) {
            throw new BizException("Device not exist", "设备不存在");
        }
        addTags(device);
        return data(device);
    }

    private void addTags(Device device) {
        List<Tag> tagList = iTagService.list(new QueryWrapper<Tag>().eq("link_id", device.getProductId()));
        Set<String> tags = new HashSet<>();
        for (Tag tag : tagList) {
            tags.add(tag.getName());
        }
        device.setTags(tags);
    }

    /**
     * 获取属性
     *
     * @param id
     * @return
     * @throws XException
     */
    @GetMapping("/status/{id}")
    public R getStatus(@PathVariable Long id) throws XException {
        Device device = iDeviceService.getById(id);
        if (device == null) {
            throw new BizException("Device not exist", "设备不存在");
        }
        return data(device.getStatuses());
    }

    /**
     * 设置状态
     *
     * @param id
     * @param statuses
     * @return
     * @throws XException
     */
    @PutMapping("/status/{id}")
    public R setStatus(@PathVariable Long id,
                       @Valid @RequestBody List<@Valid DeviceStatus> statuses,
                       ObjectMapper objectMapper) throws XException {
        Device device = iDeviceService.getById(id);
        if (device == null) {
            throw new BizException("Device not exist", "设备不存在");
        }
        /**
         * 提取KeySet集合
         */
        //List<DeviceParam> deviceParamList = device.getParameters();
        List<FieldParam> deviceParamList = objectMapper.convertValue(device.getParameters(),
                new TypeReference<List<FieldParam>>() {
                });

        Set<String> fields = new HashSet<>();
        for (FieldParam deviceParam : deviceParamList) {
            fields.add(deviceParam.getField());
        }
        /**
         * 比对
         */
        List<DeviceStatus> deviceStatusList = new ArrayList<>();

        for (DeviceStatus deviceStatus : statuses) {
            if (fields.contains(deviceStatus.getField())) {
                deviceStatusList.add(deviceStatus);
            }
        }
        device.setStatuses(deviceStatusList);
        boolean ok = iDeviceService.updateById(device);

        return ok ? data(deviceStatusList) : fail();

    }

    /**
     * 删除
     *
     * @param ids
     * @return
     * @throws XException
     */
    @Override
    protected R delete(@PathVariable Integer[] ids) throws XException {
        boolean ok = iDeviceService.removeByIds(Arrays.asList(ids));
        return ok ? success() : fail();
    }

    /**
     * 条件检索
     *
     * @param current
     * @param size
     * @param name
     * @param type
     * @return
     */
    @GetMapping
    public R queryForPage(
            @RequestParam Long productId,
            @RequestParam Integer current,
            @RequestParam Integer size,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String sn,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) String model) throws BizException {

        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(projectId != null, "project_id", projectId);
        queryWrapper.eq(productId != null, "product_id", productId);
        queryWrapper.eq(sn != null, "sn", sn);
        queryWrapper.eq(model != null, "model", model);
        queryWrapper.eq(type != null, "type", type);
        queryWrapper.like(name != null, "name", name);
        queryWrapper.like(industry != null, "industry", industry);
        queryWrapper.orderByDesc("create_time");

        IPage<Device> devicePage = iDeviceService.page(new Page<>(current, size), queryWrapper);
        for (Device device : devicePage.getRecords()) {
            addTags(device);
        }

        return data(devicePage);
    }


    /**
     * 初始化设备
     * @return
     */
    @PutMapping("/init/{id}")
    public R initialDevice(@PathVariable Long id) throws BizException {
        Device device = iDeviceService.getById(id);
        if (device == null) {
            throw new BizException("Device not exist", "设备不存在");
        }
        device.setLastActive(null);
        device.setState(DeviceState.UN_ACTIVE);
        return success();
    }

    /**
     * 推送消息
     *
     * @param s2CMessage
     * @return
     */

    @PostMapping("/s2c")
    public R s2c(@RequestBody @Valid S2CMessage s2CMessage){
        //TODO
        //此处细节较多，等其他地方调通以后再来处理

        return success();
    }

}

