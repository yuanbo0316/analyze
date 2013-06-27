/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.analyze;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * P31008
 * 保存warn级别日志信息
 * @author lianzt
 */
@Service
public class SaveWarnInfo extends BaseService {

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    @Transactional
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        //insert into log_warn_info (save_time,server,class_name,msg,err_code,err_msg) value (?,?,?,?,?,?)
        update("save_warn_info", in);
    }
}
