/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.analyze;

import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.commondata.DataConvertFactory;
import com.soa.plugin.SqlRunAnalyze;
import com.soa.service.BaseService;
import com.soa.util.SystemUtil;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 保存运行超时的SQL
 * P31001
 * @author lianzt
 */
@Service
public class SaveTimeoutSql extends BaseService {

    private final Logger log = LoggerFactory.getLogger(SaveTimeoutSql.class);
    private final String[] KEY = new String[]{"log_mark", "日志类型标签",
                                              "sql", "SQL语句名",
                                              "_runTime", "运行时间"};

    @Override
    public String[] keys() {
        return KEY;
    }

    @Override
    @Transactional
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        //判断是否为批量更新的sql
        AbstractCommonData sqlArgs = DataConvertFactory.getInstanceEmpty();
        sqlArgs.putDateValue("save_time", new Date());
        sqlArgs.put("sql_value", in.get("sql_value"));
        sqlArgs.put("sql_name", in.get("sql"));
        sqlArgs.put("server", in.get("server"));
        if (SqlRunAnalyze.CUT_PAGE_MARK.equals(in.getStringValue("log_mark"))) {
            sqlArgs.putStringValue("cut_page", "1");
        } else {
            sqlArgs.putStringValue("cut_page", "0");
        }
        if (in.get("times") == null) {
            sqlArgs.putIntValue("times", 1);
        } else {
            sqlArgs.put("times", in.get("times"));
        }
        sqlArgs.put("run_time", in.get("_runTime"));
        sqlArgs.put("args", in.get("args"));
        sqlArgs.put("update_line", in.get("update_line"));
        update("save_warn_sql", sqlArgs);
    }
}
