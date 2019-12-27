package com.ezlinker.app.modules.scheduleinfo.controller;


import com.ezlinker.app.common.CurdController;
import com.ezlinker.app.config.quartz.QuartzService;
import com.ezlinker.app.modules.scheduleinfo.job.ScheduleSendDataJob;
import com.ezlinker.app.modules.scheduleinfo.model.ScheduleInfo;
import com.ezlinker.app.modules.scheduleinfo.service.IScheduleInfoService;
import com.ezlinker.common.exception.BizException;
import com.ezlinker.common.exception.XException;
import com.ezlinker.common.exchange.R;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangwenhai
 * @since 2019-12-27
 */
@RestController
@RequestMapping("/scheduleInfos")
public class ScheduleInfoController extends CurdController<ScheduleInfo> {

    @Resource
    IScheduleInfoService iScheduleInfoService;

    @Resource
    QuartzService quartzService;

    public ScheduleInfoController(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
    }

    /**
     * 创建一个定时任务
     *
     * @param scheduleInfo
     * @return
     * @throws XException
     * @throws XException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    protected R add(@RequestBody ScheduleInfo scheduleInfo) throws XException {
        //创建触发器

        boolean ok = iScheduleInfoService.save(scheduleInfo);
        if (ok) {
            try {
                Map<String, Object> dataMap = new HashMap<>(16);
                dataMap.put("data", scheduleInfo.getScheduleDataList());
                quartzService.addJob(ScheduleSendDataJob.class,
                        scheduleInfo.getId().toString(),
                        // 作用在哪个木块 group就是表名
                        "module",
                        scheduleInfo.getTaskDescription(),
                        scheduleInfo.getTriggerCronExpression(),
                        dataMap);
                return data(scheduleInfo);
            } catch (Exception e) {
                throw new BizException("Create job error", "创建任务时异常");
            }

        } else {
            return fail();
        }


    }

    /**
     * 删除
     *
     * @param ids
     * @return
     * @throws XException
     */
    @Override
    protected R delete(Integer[] ids) throws XException {

        return super.delete(ids);
    }
}

