/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.analyze;

import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.commondata.DataConvertFactory;
import com.lianzt.util.StringUtil;
import com.soa.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 保存照片请求 P33004
 *
 * @author chen
 */
@Service
public class SavePicRequest extends BaseService{
    
    private final int TIMEOUT = 10000;       //超时预警时间 10秒

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
        //insert into log_req_run (req_time,req_url,server,username,ip,args,user_agent,run_time,is_timeout,response_code,response_desc) value (?,?,?,?,?,?,?,?,?,?,?)
        Object[] args = new Object[13];
        args[0] = in.getDateValue("req_time");
        args[1] = jsonHead.getStringValue("_url");
        args[2] = in.getStringValue("server");
        args[3] = username;
        args[4] = jsonHead.getStringValue("_ip");
        args[5] = DataConvertFactory.praseNormJson(json);       //所有情况都保存参数
        args[6] = jsonHead.getStringValue("_user_agent");
        args[7] = jsonHead.getIntValue("_runTime");
        if (TIMEOUT <= jsonHead.getIntValue("_runTime")) {
            args[8] = "1";
        } else {
            args[8] = "0";
        }
        args[9] = jsonHead.getStringValue("response_code");
        args[10] = jsonHead.getStringValue("response_desc");
        args[11] = json.getStringValue("xh");
        args[12] = json.getIntValue("pic_length");
        update("save_pic_req", args);
    }
}
