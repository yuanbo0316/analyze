/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.archive;

import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.util.DateUtil;
import com.soa.service.BaseService;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * P32005
 * url请求数据归档
 * 说明：对3天前的归档，整理成IP访问；
 * 3天前的数据整理成每小时一条，归档级别为0，然后删除原始数据；
 * 10天前的数据整理成每天一条，归档级别为1，然后删除原始数据；
 * 1个月前的数据整理成每月一条，归档级别为2，然后删除原始数据；
 * @author lianzt
 */
@Service
public class ReqArchiveTask extends BaseService {

    private final int ARCHIVE_HOUR = -3;     //归档级别小时，3天内的日志
    private final int ARCHIVE_DAY = -10;     //归档级别天，3~10天内的日志
    private final int ARCHIVE_MONTH = -1;     //归档级别月，一个月前的日志

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    @Transactional
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        //先归档，再删除

        Date time = new Date();
        boolean archiveMonth = false;
        if (time.getDate() == 1) {
            //每月第一天，归档之前第二个月的日志
            archiveMonth = true;
        }
        time = DateUtil.setHour(time, 0);
        time = DateUtil.setMinute(time, 0);
        time = DateUtil.setSecond(time, 0);
        time = DateUtil.addDay(time, ARCHIVE_HOUR);
        update("req_archive_hour", time);     //插入快照计算结果
        update("delete_req_run", time);     //删除快照

        time = DateUtil.addDay(time, ARCHIVE_DAY);
        update("req_archive_day", time);      //归档之前第3~10天的数据
        update("delete_req_hour", time);      //把已归档的日志删除

        if (archiveMonth) {
            //归档月日志
            time = DateUtil.setDay(time, 1);
            time = DateUtil.addMonth(time, ARCHIVE_MONTH);
            update("req_archive_month", time);
            update("delete_req_day", time);
        }
    }
}
