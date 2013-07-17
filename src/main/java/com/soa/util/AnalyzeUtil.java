/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.util;

import com.soa.plugin.impl.AnalyzePlugin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lianzt
 */
public class AnalyzeUtil {

    public static Map<String, String> serverSql = new HashMap<String, String>();        //其它服务的sql语句
    public static Map<String, List<AnalyzePlugin>> serverAnalyze = new HashMap<String, List<AnalyzePlugin>>();        //服务对应的分析器
}
