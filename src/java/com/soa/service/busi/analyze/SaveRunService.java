/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.analyze;

import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.commondata.DataConvertFactory;
import com.lianzt.util.StringUtil;
import com.soa.service.BaseService;
import com.soa.util.NodeUtil;
import java.util.Date;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * P31003
 * 保存服务请求
 * @author lianzt
 */
@Service
public class SaveRunService extends BaseService {

    @Resource
    private NodeUtil nodeUtil;
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
        //insert into log_service_run (exec_time,service_code,server,username,ip,args,run_time,from_node,is_timeout,response_code,response_desc) value (?,?,?,?,?,?,?,?,?,?,?)
        Object[] args = new Object[11];
        args[0] = new Date();
        args[1] = jsonHead.getStringValue("service_code");
        args[2] = in.getStringValue("server");
        args[3] = jsonHead.getStringValue("username");
        if (StringUtil.isNull((String) args[3]) && session != null) {
            args[3] = session.getStringValue("username");
        }
        args[4] = jsonHead.getStringValue("_ip");
        if (jsonHead.get("response_code") != null) {
            args[5] = DataConvertFactory.praseNormJson(json);       //请求异常时保存参数
        } else {
            args[5] = "";
        }
        args[6] = json.getIntValue("_runTime");
        args[7] = nodeUtil.determineServiceNode(json);
        if (TIMEOUT <= json.getIntValue("_runTime")) {
            args[8] = "1";      //超时
        } else {
            args[8] = "0";      //正常
        }
        args[9] = jsonHead.getStringValue("response_code");
        args[10] = jsonHead.getStringValue("response_desc");
        update("save_run_service", args);
    }
}
