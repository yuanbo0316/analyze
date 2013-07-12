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
 * 获取所有用户名 S34105
 *
 * @author chen
 */
@Service
public class GetUserLoginDetail extends BaseService {

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
       List<AbstractCommonData> list = queryList("get_user_login_detail", new Object[]{in.getStringValue("username"),in.getStringValue("from_node")});
       out.putArrayValue("list", list);
    }
}
