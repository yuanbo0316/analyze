/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.plugin;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.commondata.DataConvertFactory;
import com.lianzt.util.StringUtil;
import com.soa.exception.GlobalException;
import com.soa.plugin.impl.AnalyzePlugin;
import com.soa.service.BaseService;
import com.soa.util.SystemUtil;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * sql语句运行情况分析
 * @author lianzt
 */
@Component
public class SqlRunAnalyze implements AnalyzePlugin {

    private final Logger log = LoggerFactory.getLogger(SqlRunAnalyze.class);
    private final AbstractCommonData flash = DataConvertFactory.getInstanceEmpty();
    private final int TIMEOUT = 2000;       //超时预警时间 2秒
    public static final String NORMAL = "normal";       //用于判断sql类型
    public static final String CUT_PAGE = "cutpage";       //用于判断sql类型
    public static final String NORMAL_MARK = "['log-sql-time']";
    public static final String CUT_PAGE_MARK = "['log-cutpage-time']";

//    @PostConstruct      //相当于 init-method
//    @PreDestroy       //相当于 destroy-method
//    public void init() {
//        AbstractCommonData flash = DataConvertFactory.getInstanceEmpty();
//    }
    @Override
    public synchronized void analyze(AbstractCommonData json, ILoggingEvent event, String logMark, String server) {
        if (json == null || (!NORMAL_MARK.equals(logMark)) && !CUT_PAGE_MARK.equals(logMark)) {
            //不是我的菜
            return;
        }
        log.debug("收到的sql: mark{}, json {}", logMark, json);

        String sql = json.getStringValue("sql");
        if (StringUtil.isNull(sql)) {
            throw new GlobalException(310001);     //没有找到sql字段
        }
        if (NORMAL_MARK.equals(logMark)) {
            sql = server + "." + NORMAL + "." + sql;
        } else {
            sql = server + "." + CUT_PAGE + "." + sql;
        }
        boolean isTimeout = false;
        int runTime = json.getIntValue("_runTime");
        synchronized (flash) {      //对快照进行更新时，需要加锁
            AbstractCommonData res = flash.getDataValue(sql);       //历史数据
            if (res == null) {
                res = DataConvertFactory.getInstanceEmpty();
                res.putIntValue("min", Integer.MAX_VALUE);
                res.putIntValue("max", 0);
                if (json.get("times") == null) {        //批量执行sql的情况
                    res.putIntValue("avg", runTime);
                } else {
                    res.putIntValue("avg", runTime / json.getIntValue("times"));
                }
                res.putIntValue("timeout_times", 0);
                res.putIntValue("times", 0);
                flash.putDataValue(sql, res);
            }
            if (json.get("times") != null) {        //批量执行sql的情况
                res.putIntValue("times", res.getIntValue("times") + json.getIntValue("times"));
                //使用平均值做为单条执行时间
                int avg = runTime / json.getIntValue("times");
                if (avg >= TIMEOUT) {       //超时
                    isTimeout = true;
                    res.putIntValue("timeout_times", res.getIntValue("timeout_times") + json.getIntValue("times"));
                }
                res.putIntValue("min", Math.min(avg, res.getIntValue("min")));
                res.putIntValue("max", Math.max(avg, res.getIntValue("max")));
                res.putIntValue("avg", (avg + res.getIntValue("avg")) >> 1);        //右移一位，代替除2
            } else {
                res.putIntValue("times", res.getIntValue("times") + 1);
                res.putIntValue("min", Math.min(runTime, res.getIntValue("min")));
                res.putIntValue("max", Math.max(runTime, res.getIntValue("max")));
                res.putIntValue("avg", (runTime + res.getIntValue("avg")) >> 1);        //右移一位，代替除2
                if (runTime >= TIMEOUT) {
                    res.putIntValue("timeout_times", res.getIntValue("timeout_times") + 1);
                    isTimeout = true;
                }
            }
        }
        if (isTimeout) {
            AbstractCommonData in = DataConvertFactory.getInstance();
            in.putAll(json);
            in.putStringValue("server", server);
            in.putStringValue("log_mark", logMark);
            in.putStringValue("sql_value", SystemUtil.serverSql.get(server + "." + json.getStringValue("sql")));
            BaseService.runService(in, "P31001");     //保存超时的SQL
        }
    }

    @Override
    public AbstractCommonData getFlash() {
        return (AbstractCommonData) flash.clone();
    }

    @Override
    public void clear() {
        flash.clear();
    }

    @Override
    public void saveFlash() {
        AbstractCommonData in = DataConvertFactory.getInstance();
        in.putDataValue("flash", flash);
        log.info("开始保存快照 {} 条数据。", flash.size());
        synchronized (flash) {          //保存时加锁
            BaseService.runService(in, "P31002");        //保存
            clear();        //保存后
        }
    }
}
