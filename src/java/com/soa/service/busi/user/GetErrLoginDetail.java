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
 * 获取失败登录统计 S34110
 *
 * @author chen
 */
@Service
public class GetErrLoginDetail extends BaseService{

    @Override
    public String[] keys() {
       return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        List<AbstractCommonData> list = queryList("get_err_login_detail",new Object[]{in.getStringValue("end"),in.getStringValue("begin"),in.getStringValue("server"),in.getStringValue("ip_addr"),in.getStringValue("username_err")});
        out.putArrayValue("result", list);
    }
}
