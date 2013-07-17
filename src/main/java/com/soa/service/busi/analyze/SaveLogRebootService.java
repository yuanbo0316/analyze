/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.analyze;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * P33001
 * 保存用户登录记录
 * @author chen
 */
@Service
public class SaveLogRebootService extends BaseService {

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    @Transactional
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        if (in.getStringValue("server") != null && in.getStringValue("opt") != null) {
//            Object[] args = new Object[3];
//            args[0] = new Date();
//            args[1] = in.getStringValue("server");
//            args[2] = in.getStringValue("opt");
            update("save_log_reboot", in);
        }
    }
}