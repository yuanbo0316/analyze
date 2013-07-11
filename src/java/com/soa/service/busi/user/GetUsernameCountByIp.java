/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.user;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import org.springframework.stereotype.Service;

/**
 * 获取不同ip用户名个数 S34108
 *
 * @author chen
 */
@Service
public class GetUsernameCountByIp extends BaseService{

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        in.putStringValue("sql", "get_distinct_username_sum");
        in.putObjectValue("args", new Object[]{in.getStringValue("username_ip"),in.getStringValue("server")});
        runService("S10001", in,inHead,out,outHead);
    }
}
