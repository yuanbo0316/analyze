/*
 * 日志分析插件
 * Copyright 2011-2020 the original author or authors.
 */
package com.soa.plugin.impl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.lianzt.commondata.AbstractCommonData;

/**
 * 日志分析插件的接口，所有插件必须实现该接口，而且具体的实现类要要使用spring的Component注解标注<br />
 * 需要注意的是，分析器需要注意多线程的情况。
 *
 * 框架通过该接口调用具体的实现类。
 * @author lianzt
 */
public interface AnalyzePlugin {

    /**
     * 插件中执行运算的部分，具体的分析器可以选择使用任意一个参数，该函数可以增加 synchronized 关键字
     * @param json 日志的json部分转为commondata
     * @param event 日志的原始事件
     * @param logMark 可分析的日志标签
     * @param server 系统
     */
    public void analyze(AbstractCommonData json, ILoggingEvent event, String logMark, String server);

    /**
     * 获取快照
     * @return commondata结构的快照
     */
    public AbstractCommonData getFlash();

    /**
     * 清除计算结果
     */
    public void clear();
    /**
     * 保存快照
     */
    public void saveFlash();
}
