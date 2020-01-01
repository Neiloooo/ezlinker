package com.ezlinker.app.modules.analyse.controller;

import com.ezlinker.app.common.web.CurdController;
import com.ezlinker.app.modules.device.service.IDeviceService;
import com.ezlinker.app.modules.product.service.IProductService;
import com.ezlinker.app.modules.project.service.IProjectService;
import com.ezlinker.app.modules.user.service.IUserService;
import com.ezlinker.app.common.exchange.R;
import com.ezlinker.app.common.utils.SystemPropertiesUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据统计
 *
 * @author wangwenhai
 */
@RestController
@RequestMapping("/analyse")
public class AnalyseController extends CurdController {
    public AnalyseController(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
    }

    @Resource
    IProjectService iProjectService;

    @Resource
    IProductService iProductService;

    @Resource
    IUserService iUserService;
    @Resource
    IDeviceService iDeviceService;

    /**
     * 获取一些统计数据
     * TODO: 后期会加入更多统计数据
     *
     * @return
     */
    @GetMapping("/data")
    public R data() {
        Map<String, Object> data = new HashMap<>();
        data.put("projects", iProjectService.count());
        data.put("users", iUserService.count());
        data.put("products", iProductService.count());
        data.put("devices", iDeviceService.count());

        return data(data);
    }

    /**
     * 系统参数
     * @return
     */
    @GetMapping("/properties")
    public R getSystemProperties() {
        return data(SystemPropertiesUtil.getSystemProperties());
    }


    /**
     * 运行参数
     * @return
     */
    @GetMapping("/running")
    public R running() {
        return data(SystemPropertiesUtil.getRunning());
    }


}
