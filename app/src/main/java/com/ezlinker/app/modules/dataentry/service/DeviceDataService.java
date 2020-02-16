package com.ezlinker.app.modules.dataentry.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.ezlinker.app.modules.dataentry.model.DeviceData;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangwenhai
 * @date 2020/2/16
 * File description: 设备数据service
 */
@Service
public class DeviceDataService {

    @Resource
    MongoTemplate mongoTemplate;

    public void save(DeviceData entity) {
        mongoTemplate.insert(entity, "device_data");
    }

    public IPage<DeviceData> queryForPage(Long deviceId, org.springframework.data.domain.Pageable pageable) {
        Query query = new Query();
        Criteria criteria = Criteria.where("deviceId").is(deviceId);
        query.fields().include("createTime").include("data");
        query.addCriteria(criteria);

        query.with(Sort.by(Sort.Direction.DESC, "id"));
        query.with(pageable);

        List<DeviceData> list = mongoTemplate.find(query, DeviceData.class, "device_data");
        long total = mongoTemplate.count(query, "device_data");

        return new IPage<DeviceData>() {

            @Override
            public List<OrderItem> orders() {
                return OrderItem.descs("id");
            }


            @Override
            public List<DeviceData> getRecords() {
                return list;
            }

            @Override
            public IPage<DeviceData> setRecords(List<DeviceData> records) {
                return this;
            }

            @Override
            public long getTotal() {
                return total;
            }

            @Override
            public IPage<DeviceData> setTotal(long total) {
                return this;
            }

            @Override
            public long getSize() {
                return list.size();
            }

            @Override
            public IPage<DeviceData> setSize(long size) {
                return this;
            }

            @Override
            public long getCurrent() {
                return pageable.getPageNumber();
            }

            @Override
            public IPage<DeviceData> setCurrent(long current) {
                return this;
            }
        };
    }
}
