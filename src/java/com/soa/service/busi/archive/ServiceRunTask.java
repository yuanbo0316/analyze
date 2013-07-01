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
 * P32003
 * 对service运行记录归档
 * @author lianzt
 */
@Service
public class ServiceRunTask extends BaseService{

    private final int DEL_DAY = -10;     //删除1天前的日志
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
        //delete from log_service_run where exec_time < ?
        update("service_run_archive", time);
    }

}
