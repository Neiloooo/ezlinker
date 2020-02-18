package com.ezlinker.app.modules.module.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.ezlinker.app.modules.module.model.ModuleData;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangwenhai
 * @date 2020/2/17
 * File description: 模块的☁️原始数据
 */
@Service
public class ModuleDataService {

    @Resource
    MongoTemplate mongoTemplate;

    public void save(ModuleData entity) {
        mongoTemplate.insert(entity, "module_data");
    }

    public IPage<ModuleData> queryForPage(Long moduleId, org.springframework.data.domain.Pageable pageable) {
        Query query = new Query();
        Criteria criteria = Criteria.where("moduleId").is(moduleId);
        query.fields().include("createTime").include("data");
        query.addCriteria(criteria);

        query.with(Sort.by(Sort.Direction.DESC, "id"));
        query.with(pageable);

        List<ModuleData> list = mongoTemplate.find(query, ModuleData.class, "module_data");
        long total = mongoTemplate.count(query, "module_data");

        return new IPage<ModuleData>() {

            @Override
            public List<OrderItem> orders() {
                return OrderItem.descs("id");
            }


            @Override
            public List<ModuleData> getRecords() {
                return list;
            }

            @Override
            public IPage<ModuleData> setRecords(List<ModuleData> records) {
                return this;
            }

            @Override
            public long getTotal() {
                return total;
            }

            @Override
            public IPage<ModuleData> setTotal(long total) {
                return this;
            }

            @Override
            public long getSize() {
                return list.size();
            }

            @Override
            public IPage<ModuleData> setSize(long size) {
                return this;
            }

            @Override
            public long getCurrent() {
                return pageable.getPageNumber();
            }

            @Override
            public IPage<ModuleData> setCurrent(long current) {
                return this;
            }
        };
    }
}
