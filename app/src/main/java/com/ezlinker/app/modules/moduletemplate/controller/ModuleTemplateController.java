package com.ezlinker.app.modules.moduletemplate.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ezlinker.app.common.CurdController;
import com.ezlinker.app.modules.moduletemplate.model.ModuleTemplate;
import com.ezlinker.app.modules.moduletemplate.service.IModuleTemplateService;
import com.ezlinker.app.common.exception.BizException;
import com.ezlinker.app.common.exception.XException;
import com.ezlinker.app.common.exchange.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;

/**
 * <p>
 * 产品上面的模块模板 前端控制器
 * </p>
 *
 * @author wangwenhai
 * @since 2019-12-19
 */
@RestController
@RequestMapping("/moduleTemplates")
public class ModuleTemplateController extends CurdController<ModuleTemplate> {

    @Resource
    IModuleTemplateService iModuleTemplateService;

    public ModuleTemplateController(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
    }

    /**
     * 创建模块
     *
     * @param moduleTemplate
     * @return
     * @throws XException
     */
    @Override
    protected R add(@RequestBody @Valid ModuleTemplate moduleTemplate) {
        iModuleTemplateService.save(moduleTemplate);
        return data(moduleTemplate);

    }

    /**
     * 更新
     *
     * @param id
     * @param form
     * @return
     */
    @Override
    @PutMapping("/{id}")
    protected R update(@PathVariable Long id, @RequestBody ModuleTemplate form) {
        ModuleTemplate module = iModuleTemplateService.getById(id);
        module.setName(form.getName()).setDescription(form.getDescription());
        boolean ok = iModuleTemplateService.updateById(module);
        return ok ? data(module) : fail();
    }

    /**
     * 删除模块
     *
     * @param ids
     * @return
     */
    @Override
    protected R delete(@PathVariable Integer[] ids) {
        boolean ok = iModuleTemplateService.removeByIds(Arrays.asList(ids));
        return ok ? success() : fail();
    }

    /**
     * 获取字典项列表
     *
     * @param current
     * @param size
     * @param productId
     * @return
     */
    @GetMapping
    public R queryForPage(
            @RequestParam Long current,
            @RequestParam Long size,
            @RequestParam Long productId) {
        QueryWrapper<ModuleTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        queryWrapper.orderByDesc("create_time");
        IPage<ModuleTemplate> iPage = iModuleTemplateService.page(new Page<>(current, size), queryWrapper);
        return data(iPage);
    }

    /**
     * 获取详情
     *
     * @param id
     * @return
     * @throws XException
     */
    @Override
    protected R get(@PathVariable Long id) throws XException {
        ModuleTemplate module = iModuleTemplateService.getById(id);
        if (module == null) {
            throw new BizException("Component not exists", "模块不存在");

        }
        return data(module);
    }
}

