package com.soa.task;

import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.commondata.DataConvertFactory;
import com.lianzt.util.DateUtil;
import com.lianzt.util.LogUtil;
import com.soa.exception.GlobalException;
import com.soa.service.BaseService;
import com.soa.service.impl.UtilService;
import com.soa.util.SystemUtil;
import java.util.Date;
import java.util.TimerTask;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 定时任务的基类，由于定时任务没有实现接口，不能使用Spring进行aop控制，所以定义一个基类做aop类
 * @author lianzt
 */
@Component
@Scope("prototype")
public class BaseTask extends TimerTask {

    private final Logger log = LoggerFactory.getLogger(BaseTask.class);
    private final String DEFAULT_USER =    "_timer";
    @Resource
    private UtilService utilServiceImpl;
    private String runTime;     //运行时刻
    private String cycle;         //周期
    private String serviceCode; //运行服务
    private String args;        //参数 json 字符串
    private String desc;        //说明
    private String delay;       //延时运行

    @Override
    public String toString() {
        return String.format("run-time:%s, cycle:%s, service-code:%s, args:%s, desc:%s", runTime, cycle, serviceCode, args, desc);
    }

    @Override
    public void run() {
        log.info("开始运行定时任务:{}-{}", desc, serviceCode);
        Date begin = new Date();
        try {
            AbstractCommonData req = DataConvertFactory.getInstance();
            AbstractCommonData ses = DataConvertFactory.getInstanceEmpty();
            ses.putStringValue(SystemUtil.loginRemark, DEFAULT_USER);
            req.putDataValue(BaseService.SESSION_NAME, ses);
            BaseService.runService(req, serviceCode);
        } catch (GlobalException e) {
            log.warn("任务 {}-{} 执行异常:{}", new Object[]{desc, serviceCode, e.toString()});
            log.error("error:", e);
            if (e.getErrorCode() > 900000) {
                utilServiceImpl.saveError(e);
            }
        } finally {
            Date end = new Date();
            log.info("任务 {}-{} 执行完成，共使用 {} ms", new Object[]{desc, serviceCode, (end.getTime() - begin.getTime())});
            LogUtil.analysisJsonLog(String.format("{\"desc\":\"%s\", \"service_code\":\"%s\", \"begin_time\":\"%s\", \"end_time\":\"%s\", \"_runTime\":%d}",
                    desc, serviceCode, DateUtil.detaledFormat(begin), DateUtil.detaledFormat(end), (end.getTime() - begin.getTime())), "task-time");
        }
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getRunTime() {
        return runTime;
    }

    public String getCycle() {
        return cycle;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getArgs() {
        return args;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }
}
