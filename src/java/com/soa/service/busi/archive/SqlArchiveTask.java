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
 * P32002
 * SQL运行情况归档
 * 说明：快照表保存3天内的数据，
 * 3天后的数据转移到归档表，每小时整理一条数据，归档级别为0，删除归档前的数据；
 * 10天后的数据每天整理一条，归档级别为1，删除归档前的数据；
 * 一个月前的数据，每月整理一条，归档级别为2，删除归档前的数据；
 * @author lianzt
 */
@Service
public class SqlArchiveTask extends BaseService {

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
        Date time = new Date();
        boolean archiveMonth = false;
        if(time.getDate() == 1){
            //每月第一天，归档之前第二个月的日志
            archiveMonth = true;
        }
        time = DateUtil.setHour(time, 0);
        time = DateUtil.setMinute(time, 0);
        time = DateUtil.setSecond(time, 0);
        time = DateUtil.addDay(time, ARCHIVE_HOUR);
        update("sql_archive_hour", time);     //插入快照计算结果
        update("delete_sql_flash", time);     //删除快照

        time = DateUtil.addDay(time, ARCHIVE_DAY);
        update("sql_archive_day", time);      //归档之前第3~10天的数据
        update("delete_sql_archive_hour", time);      //把已归档的日志删除

        if(archiveMonth){
            //归档月日志
            time = DateUtil.setDay(time, 1);
            time = DateUtil.addMonth(time, ARCHIVE_MONTH);
            update("sql_archive_month", time);
            update("delete_sql_archive_day", time);
        }
    }
}
