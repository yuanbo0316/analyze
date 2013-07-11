/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.user;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import org.springframework.stereotype.Service;

/**
 * 获取失败登录 S34109
 *
 * @author chen
 */
@Service
public class GetErrLogin extends BaseService{

    @Override
    public String[] keys() {
       return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
       in.putStringValue("sql", "get_err_login");
       in.putObjectValue("args", new Object[]{in.getStringValue("end"),in.getStringValue("begin"),in.getStringValue("server"),in.getStringValue("ip_err"),in.getStringValue("username_err")});
       runService("S10001", in,inHead,out,outHead);
    }
}
