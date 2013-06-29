/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.archive;

import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.util.DateUtil;
import com.soa.service.BaseService;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**P33003
 * 定时清理Task日志
 * @author chen
 */
@Service
public class DropTaskLog extends BaseService {
    Logger log = LoggerFactory.getLogger(DropTaskLog.class);

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    @Transactional
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
       Date date = new Date();
       date = DateUtil.setHour(date, 0);
       date = DateUtil.setMinute(date, 0);
       date = DateUtil.setSecond(date, 0);
       date = DateUtil.addDay(date, -60);
       update("delete_task_log", date);
    }
    
}
