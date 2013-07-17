/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.user;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import org.springframework.stereotype.Service;

/**
 * 获取登录历史 S34107
 *
 * @author chen
 */
@Service
public class GetLoginHis extends BaseService {

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
       in.putStringValue("sql", "get_login_his");
       in.putObjectValue("args", new Object[]{in.getStringValue("server"),in.getStringValue("from_node"),in.getStringValue("username_his")});
        runService("S10001", in, inHead, out, outHead);
    }
}
