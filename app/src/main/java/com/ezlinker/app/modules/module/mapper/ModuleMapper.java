package com.ezlinker.app.modules.module.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ezlinker.app.modules.dataentry.model.WebHookInterfaceInfo;
import com.ezlinker.app.modules.feature.model.Feature;
import com.ezlinker.app.modules.module.model.Module;

import java.util.List;

/**
 * <p>
 * 设备上面的模块，和设备是多对一关系 Mapper 接口
 * </p>
 *
 * @author wangwenhai
 * @since 2019-11-15
 */
public interface ModuleMapper extends BaseMapper<Module> {

    /**
     * 获取功能列表
     *
     * @param moduleId
     * @return
     */
    List<Feature> getFeatureList(Long moduleId);

    /**
     * 根据产品获取模块
     *
     * @param productId
     * @return
     */
    List<Module> listByProduct(Long productId);


    /**
     * 查询信息
     * @param clientId
     * @return
     */

    WebHookInterfaceInfo getWebHookInterfaceInfo(String clientId);

}
