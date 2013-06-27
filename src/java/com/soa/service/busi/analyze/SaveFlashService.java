/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.analyze;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * P31004
 * 保存服务运行快照
 * @author lianzt
 */
@Service
public class SaveFlashService extends BaseService {

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    @Transactional
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        AbstractCommonData flash = in.getDataValue("flash");
        if (flash == null || flash.isEmpty()) {
            return;
        }
        //insert into log_service_flash (save_time,service_code,server,min_run_time,max_run_time,avg_run_time,times) value (?,?,?,?,?,?,?)
        List<Object[]> argsList = new LinkedList<Object[]>();
        Set<String> keySet = flash.keySet();
        AbstractCommonData serviceFlash = null;
        String[] nameArr = null;
        Object[] args = null;
        Date saveTime = new Date();
        for (String key : keySet) {
            args = new Object[7];
            serviceFlash = flash.getDataValue(key);
            nameArr = key.split("\\.");
            args[0] = saveTime;
            args[1] = nameArr[1];
            args[2] = nameArr[0];
            args[3] = serviceFlash.getIntValue("min");
            args[4] = serviceFlash.getIntValue("max");
            args[5] = serviceFlash.getIntValue("avg");
            args[6] = serviceFlash.getIntValue("times");
            argsList.add(args);
        }
        batchUpdate("save_flash_service", argsList);
    }
}
