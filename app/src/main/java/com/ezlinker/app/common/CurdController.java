package com.ezlinker.app.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ezlinker.common.exception.XException;
import com.ezlinker.common.exchange.R;
import com.ezlinker.common.query.QueryItem;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: ezlinker
 * @description: 基础Controller
 * @author: wangwenhai
 * @create: 2019-11-04 17:10
 **/
public abstract class CurdController<T> extends XController {


    public CurdController(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
    }

    /**
     * 添加一个T
     * @param t
     * @return
     */
    @PostMapping
    protected R add(T t) throws XException {
        return success();
    }


    /**
     * 批量删除T
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    protected R delete(Integer[] ids) throws XException {
        return success();
    }


    /**
     * 更新T
     *
     * @param t
     * @return
     */
    @PutMapping(value = "/{id}")
    protected R update(Long id, T t) throws XException {
        return success();
    }

    /**
     * 查询单个T
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    protected R get(Long id) throws XException {
        return success();
    }

    /**
     * GraphSql
     *
     * @param current
     * @param size
     * @param queryItems
     * @return
     * @throws XException
     */
    @PostMapping("/query")
    protected R query(Integer current,
                      Integer size,
                      List<QueryItem> queryItems) throws XException {

        return data(getQueryWrapper(queryItems));
    }

    /**
     * 获取查询条件
     *
     * @param queryItems
     * @return
     */
    private QueryWrapper<T> getQueryWrapper(List<QueryItem> queryItems) {
        List<String> fields = new ArrayList<>();
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        for (QueryItem queryItem : queryItems) {
            fields.add(queryItem.getK());

            switch (queryItem.getO()) {
                case IN:
                    switch (queryItem.getL()) {
                        case OR:
                            queryWrapper.or().in(queryItem.getK(), queryItem.getV());
                            break;
                        case AND:
                            queryWrapper.in(queryItem.getK(), queryItem.getV());
                            break;
                        default:
                            break;

                    }

                    break;
                case LIKE:
                    switch (queryItem.getL()) {
                        case OR:
                            queryWrapper.or().like(queryItem.getK(), queryItem.getV());
                            break;
                        case AND:
                            queryWrapper.like(queryItem.getK(), queryItem.getV());
                            break;
                        default:
                            break;
                    }
                    break;
                case EQUAL:
                    queryWrapper.eq(queryItem.getK(), queryItem.getV());
                    switch (queryItem.getL()) {
                        case OR:
                            queryWrapper.or().eq(queryItem.getK(), queryItem.getV());
                            break;
                        case AND:
                            queryWrapper.eq(queryItem.getK(), queryItem.getV());
                            break;
                        default:
                            break;

                    }

                    break;
                default:
                    break;
            }
        }
        String[] column = new String[fields.size()];
        fields.toArray(column);
        return queryWrapper.select(column);
    }

}
