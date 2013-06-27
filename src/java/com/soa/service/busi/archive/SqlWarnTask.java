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
 * 对warn sql语句归档
 * P32001
 * @author lianzt
 */
@Service
public class SqlWarnTask extends BaseService {

    private final int DEL_DAY = -30;     //删除30天前的日志

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    @Transactional
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        Date time = new Date();
        time = DateUtil.setHour(time, 0);
        time = DateUtil.setMinute(time, 0);
        time = DateUtil.setSecond(time, 0);
        time = DateUtil.addDay(time, DEL_DAY);
        //delete from log_sql_warn where save_time < ?
        update("sql_warn_archive", time);
    }
}
