/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.user;

import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.util.DateUtil;
import com.soa.service.BaseService;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 用户注册分析
 * S34101
 * @author lianzt
 */
@Service
public class RegJtgzfwUser extends BaseService {

    private final String REG_SERVICE = "S23002";        //注册服务
    private final String REG_SERVER = "jtgzfw";        //

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        List<AbstractCommonData> regTable = queryList("get_reg_jtgzfw_user", new Object[]{REG_SERVICE, REG_SERVER});
        out.putArrayValue("reg_table", regTable);
    }
}
