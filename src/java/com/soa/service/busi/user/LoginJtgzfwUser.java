/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.user;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import java.util.List;
import org.springframework.stereotype.Service;

/**登录终端分布
 *  S34102
 * @author chen
 */
@Service
public class LoginJtgzfwUser extends BaseService {

    @Override
    public String[] keys() {
       return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        List<AbstractCommonData> list = queryList("get_login_jtgzfw_user", "jtgzfw");
        out.putArrayValue("login_data", list);
    }
    
}
