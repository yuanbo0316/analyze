/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.analyze;

import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.commondata.CommonDataElement;
import com.soa.plugin.SqlRunAnalyze;
import com.soa.service.BaseService;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * P31002
 * 保存SQL运行情况快照
 * @author lianzt
 */
@Service
public class SaveFlashSql extends BaseService {

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
        //save_time,sql_name,server,min_run_time,max_run_time,avg_run_time,times,timeout_times,cut_page
        List<Object[]> argsList = new LinkedList<Object[]>();
        Set<String> keySet = flash.keySet();
        AbstractCommonData sqlFlash = null;
        String[] sqlArr = null;
        Object[] args = null;
        Date saveTime = new Date();
        for (String s : keySet) {
            args = new Object[9];
            sqlFlash = flash.getDataValue(s);
            sqlArr = s.split("\\.");
            args[0] = saveTime;
            args[1] = sqlArr[2];
            args[2] = sqlArr[0];
            args[3] = sqlFlash.getIntValue("min");
            args[4] = sqlFlash.getIntValue("max");
            args[5] = sqlFlash.getIntValue("avg");
            args[6] = sqlFlash.getIntValue("times");
            args[7] = sqlFlash.getIntValue("timeout_times");
            args[8] = SqlRunAnalyze.CUT_PAGE.equals(sqlArr[1]) ? "1" : "0";
            argsList.add(args);
        }
        batchUpdate("save_flash_sql", argsList);
    }
}
