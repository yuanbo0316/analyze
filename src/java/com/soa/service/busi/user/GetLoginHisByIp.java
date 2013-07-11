/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.user;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 获取IP登录历史 S34111
 *
 * @author chen
 */
@Service
public class GetLoginHisByIp extends BaseService {

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
//        List<AbstractCommonData> username_list = queryList("get_username_by_ip", in.getStringValue("ip_addr"));
//        Object[] args = new Object[username_list.size()];
//        int i = 0;
//        for (AbstractCommonData a : username_list) {
//            args[i] = a.getStringValue("username");
//            i++;
//        }
       in.putStringValue("sql", "get_username_by_ip");
       in.putObjectValue("args", new Object[]{in.getStringValue("ip_addr"),in.getStringValue("server")});
       runService("S10001", in,inHead,out,outHead);
    }
}
