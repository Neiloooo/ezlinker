package com.ezlinker.app.modules.device.controller;


import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ezlinker.app.common.exception.BizException;
import com.ezlinker.app.common.exception.XException;
import com.ezlinker.app.common.exchange.R;
import com.ezlinker.app.common.web.CurdController;
import com.ezlinker.app.modules.device.model.Device;
import com.ezlinker.app.modules.device.service.IDeviceService;
import com.ezlinker.app.modules.feature.model.Feature;
import com.ezlinker.app.modules.feature.service.IFeatureService;
import com.ezlinker.app.modules.module.model.Module;
import com.ezlinker.app.modules.module.pojo.DataArea;
import com.ezlinker.app.modules.module.service.IModuleService;
import com.ezlinker.app.modules.moduletemplate.model.ModuleTemplate;
import com.ezlinker.app.modules.moduletemplate.service.IModuleTemplateService;
import com.ezlinker.app.modules.mqtttopic.model.MqttTopic;
import com.ezlinker.app.modules.mqtttopic.service.IMqttTopicService;
import com.ezlinker.app.modules.product.model.Product;
import com.ezlinker.app.modules.product.service.IProductService;
import com.ezlinker.app.modules.tag.model.Tag;
import com.ezlinker.app.modules.tag.service.ITagService;
import com.ezlinker.app.utils.IDKeyUtil;
import com.ezlinker.app.utils.ModuleTokenUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * @since 2020-02-23
 */
@RestController
@RequestMapping("/devices")
public class DeviceController extends CurdController<Device> {
    // 发布权限
    private static final int TOPIC_PUB = 1;
    // 订阅权限
    private static final int TOPIC_SUB = 2;

    @Resource
    ITagService iTagService;
    @Resource
    IFeatureService iFeatureService;
    @Resource
    IProductService iProductService;
    @Resource
    IDeviceService iDeviceService;
    @Resource
    IModuleService iModuleService;
    @Resource
    IMqttTopicService iMqttTopicService;
    @Resource
    IModuleTemplateService iModuleTemplateService;
    @Resource
    IDKeyUtil idKeyUtil;

    public DeviceController(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
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
        addModules(device);
        addFeatures(device);
        return data(device);
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
            addModules(device);
            addFeatures(device);
        }

        return data(devicePage);
    }

    private void addModules(Device device) {
        List<Module> modules = iModuleService.list(new QueryWrapper<Module>().eq("device_id", device.getId()));
        device.setModules(modules);
    }

    private void addFeatures(Device device) {
        List<Feature> features = iFeatureService.list(new QueryWrapper<Feature>().eq("product_id", device.getProductId()));
        device.setFeatures(features);
    }

    /**
     * 增加Tag
     *
     * @param device
     */
    private void addTags(Device device) {
        List<Tag> tagList = iTagService.list(new QueryWrapper<Tag>().eq("link_id", device.getProductId()));
        Set<String> tags = new HashSet<>();
        for (Tag tag : tagList) {
            tags.add(tag.getName());
        }
        device.setTags(tags);
    }

    @Override
    protected R add(@RequestBody @Valid Device device) throws XException {

        Product product = iProductService.getById(device.getProductId());

        if (product == null) {
            throw new BizException("Product not exists", "该产品不存在!");
        }

        String username = SecureUtil.md5(IDKeyUtil.generateId().toString());
        String clientId = SecureUtil.md5(username);
        String password = SecureUtil.md5(clientId);
        device.setParameters(product.getParameters())
                .setUsername(username)
                .setPassword(password)
                .setClientid(clientId)
                .setProductId(product.getId())
                .setProjectId(product.getProjectId())
                .setLogo(product.getLogo())
                .setSn("SN" + idKeyUtil.nextId());
        // 保存设备
        iDeviceService.save(device);


        // 从设计好的产品模板里面拿数据
        List<ModuleTemplate> moduleTemplates = iModuleTemplateService.list(new QueryWrapper<ModuleTemplate>().eq("product_id", product.getId()));


        for (ModuleTemplate moduleTemplate : moduleTemplates) {
            Module newModule = new Module();
            newModule.setName(moduleTemplate.getName())
                    .setDataAreas(moduleTemplate.getDataAreas())
                    .setDeviceId(device.getId());
            // Token
            ObjectMapper objectMapper = new ObjectMapper();
            List<DataArea> dataAreasList = objectMapper.convertValue(moduleTemplate.getDataAreas(), new TypeReference<List<DataArea>>() {
            });

            //
            List<String> fields = new ArrayList<>();
            for (DataArea area : dataAreasList) {
                fields.add(area.getField());
            }
            // 生成给Token，格式：clientId::[field1,field2,field3······]
            // token里面包含了模块的字段名,这样在数据入口处可以进行过滤。
            String token = ModuleTokenUtil.token(clientId + "::" + fields.toString());
            iModuleService.save(newModule);
            // 给新的Module创建Topic
            // 数据上行

            MqttTopic s2cTopic = new MqttTopic();
            s2cTopic.setAccess(TOPIC_SUB)
                    .setType(MqttTopic.S2C)
                    .setClientId(clientId)
                    .setDeviceId(device.getId())
                    .setTopic("mqtt/out/" + device.getClientid() + "/s2c")
                    .setUsername(username);
            // 数据下行
            MqttTopic c2sTopic = new MqttTopic();
            c2sTopic.setAccess(TOPIC_PUB)
                    .setType(MqttTopic.C2S)
                    .setDeviceId(device.getId())
                    .setClientId(clientId)
                    .setTopic("mqtt/in/" + device.getClientid() + "/c2s")
                    .setUsername(username);
            // 状态上报
            MqttTopic statusTopic = new MqttTopic();
            statusTopic.setAccess(TOPIC_PUB)
                    .setType(MqttTopic.STATUS)
                    .setUsername(username)
                    .setClientId(clientId)
                    .setDeviceId(device.getId())
                    .setTopic("mqtt/in/" + device.getClientid() + "/status");
            //生成
            iMqttTopicService.save(s2cTopic);
            iMqttTopicService.save(c2sTopic);
            iMqttTopicService.save(statusTopic);

        }


        return data(device);
    }

    /**
     * 查询模块数据定义
     *
     * @param deviceId
     * @return
     * @throws XException
     */
    @GetMapping("/queryDataStructureByDeviceId")
    public R queryDataStructureByDeviceId(@RequestParam Long deviceId) throws XException {
        Device device = iDeviceService.getById(deviceId);
        if (device == null) {
            throw new BizException("Device not exist", "设备不存在");
        }
        List<Module> moduleList = iModuleService.list(new QueryWrapper<Module>().eq("device_id", device.getId()));
        List<Map<String, Object>> moduleDataDefineList = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object>> featureCmdKeyDefineList = new ArrayList<>();

        for (Module module : moduleList) {
            Long moduleId = module.getId();
            List<DataArea> dataAreas = module.getDataAreas();
            Map<String, Object> moduleMap = new HashMap<>();
            moduleMap.put("moduleId", moduleId);
            moduleMap.put("deviceId", deviceId);
            moduleMap.put("structure", dataAreas);
            List<Feature> featureList = iFeatureService.list(new QueryWrapper<Feature>().eq("product_id", device.getProductId()));
            for (Feature feature : featureList) {
                Map<String, Object> featureMap = new HashMap<>();
                featureMap.put("featureId", feature.getId());
                featureMap.put("cmdKey", feature.getCmdKey());
                featureMap.put("productId", device.getProductId());
                featureCmdKeyDefineList.add(featureMap);
            }

            moduleDataDefineList.add(moduleMap);
        }
        data.put("modules", moduleDataDefineList);
        data.put("features", featureCmdKeyDefineList);
        data.put("parameter", device.getParameters());
        return data(data);
    }
}

