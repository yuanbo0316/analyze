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
 * 删除系统异常日志
 * P32007
 * @author lianzt
 */
@Service
public class ErrorDelStTask extends BaseService{

    private final int DEL_DAY = -60;     //删除1天前的日志
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
        //delete from st_err_log where err_time<?
        update("delete_st_err_log", time);

        //delete from st_err_json where save_time<?
        update("delete_st_err_json", time);
    }

}
