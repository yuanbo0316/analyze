/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.analyze;

import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.util.StringUtil;
import com.soa.service.BaseService;
import com.soa.util.NodeUtil;
import java.util.Date;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * P31005
 * 保存用户登录记录
 * @author lianzt
 */
@Service
public class SaveLoginService extends BaseService {

    @Resource
    private NodeUtil nodeUtil;

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    @Transactional
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        AbstractCommonData json = in.getDataValue("json");
        AbstractCommonData jsonHead = json.getDataValue("head");
        AbstractCommonData session = json.getDataValue("ipt_session_id");
        String username = StringUtil.changeNull(json.getStringValue("username"), jsonHead.getStringValue("username"));
        if (StringUtil.isNull(username) && session != null) {
            username = session.getStringValue("username");
        }
        //insert into log_login_info (exec_time,server,username,ip,user_agent,from_node,opt,response_code,response_desc) value (?,?,?,?,?,?,?,?,?)
        Object[] args = new Object[9];
        args[0] = new Date();
        args[1] = in.getStringValue("server");
        args[2] = username;
        args[3] = jsonHead.getStringValue("_ip");
        args[4] = jsonHead.getStringValue("_user_agent");
        args[5] = nodeUtil.determineServiceNode(json);
        args[6] = in.getStringValue("opt");
        args[7] = jsonHead.getStringValue("response_code");
        args[8] = jsonHead.getStringValue("response_desc");
        update("save_login_info", args);
    }
}
