/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.pl;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.exception.GlobalException;
import com.soa.service.BaseService;
import com.soa.util.SystemUtil;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

/**
 * 用户登录
 * S34001
 * @author lianzt
 */
@Service
public class UserLoginPl extends BaseService {

    private final String[] KEY = new String[]{"username", "用户名", "password", "密码"};
    private final Map<String, String> userMap = new HashMap<String, String>();

    @PostConstruct
    public void init() {
        userMap.put("admin", "sys713.jtgzfw.com");
        userMap.put("lianzt", "17xanwtdwI");
    }

    @Override
    public String[] keys() {
        return KEY;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        String username = in.getStringValue("username");
        String password = in.getStringValue("password");
        if (!userMap.containsKey(username) || !password.equals(userMap.get(username))) {
            throw new GlobalException(310003);      //用户名或密码错误
        }
        getSession(in).putStringValue(SystemUtil.loginRemark, username);
    }
}
