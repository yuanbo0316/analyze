/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.sysserver;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 获取服务运行时间 S34204
 *
 * @author chen
 */
@Service
public class GetSerivceRunTime extends BaseService{

    @Override
    public String[] keys() {
       return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
       List<AbstractCommonData> list = queryList("get_service_run_time", new Object[]{in.getStringValue("server"),in.getStringValue("service_code_detail")});
       out.putArrayValue("list", list);
    }
}
