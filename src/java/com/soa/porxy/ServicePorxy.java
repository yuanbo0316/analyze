package com.soa.porxy;

import com.lianzt.commondata.AbstractCommonData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.soa.exception.GlobalException;
import com.lianzt.util.LogUtil;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class ServicePorxy {

    private static final Logger log = LoggerFactory.getLogger(ServicePorxy.class);

    @Pointcut("execution (* com.soa.service..*.execute(..))")
    public void anyMethod() {
    }

    @Around("anyMethod()")
    public Object aroundService(ProceedingJoinPoint pjp) throws Throwable {
        Object obj = null;
        Date begin = new Date();
        AbstractCommonData in = null;
        String serviceCode = null;
        try {
            //提取第一个参数
            log.debug("---------------------------");
            Object arg = pjp.getArgs()[0];
            if (log.isDebugEnabled()) {
                log.debug("第一个参数的类型：" + arg.getClass().getSimpleName() + "。进行类型转换...");
            }
            in = (AbstractCommonData) arg;
            serviceCode = in.getDataValue("head").getStringValue("service_code");
            if (log.isInfoEnabled()) {
                log.info("aop拦截==========调用服务'" + serviceCode + "'，请求数据：" + arg);
            }
            obj = pjp.proceed();
            if (log.isInfoEnabled()) {
                AbstractCommonData out = (AbstractCommonData) pjp.getArgs()[2];
                log.info("aop拦截==========调用服务'" + serviceCode + "'，响应数据数据：" + out);
            }
            return obj;
        } catch (GlobalException e) {
            if (log.isDebugEnabled()) {
                log.debug("error : ", e);
            }
            log.warn("GlobalException : " + e);
            throw e;
        } catch (DataAccessException e) {
            if (log.isDebugEnabled()) {
                log.debug("error : ", e);
            }
            log.error("DataAccessException : " + e);
            Throwable t = e;
            String temp = null;
            while ((t = t.getCause()) != null) {
                temp = t.toString();
                if (temp.indexOf("Duplicate entry") > -1) {
                    throw new GlobalException(100011, e);        //数据已经存在，不能重复录入！
                }else if(temp.indexOf("Data too long for column") > -1){
                    throw new GlobalException(100012, e);        //录入的数据过长！
                }
            }
            throw new GlobalException(999998, e);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("error : ", e);
            }
            log.error("Exception : " + e);
            throw new GlobalException(999999, e);
        } finally {
            if (in != null) {
                in.putLongValue("_runTime", new Date().getTime() - begin.getTime());
                if (log.isDebugEnabled()) {
                    log.debug("开始记录服务调用的可分析日志。");
                }
                String oldServiceCode = in.getDataValue("head").getStringValue("service_code");     //暂存当前服务
                in.getDataValue("head").putStringValue("service_code", serviceCode);                //放入调用时的服务
                LogUtil.analysisCommanDataLog(in, "service-time");                                  //输出日志
                in.getDataValue("head").putStringValue("service_code", oldServiceCode);             //把暂存的服务放回
            } else {
                log.warn("获取请求参数错误，本次记录不进行记录");
            }
        }
    }
}
