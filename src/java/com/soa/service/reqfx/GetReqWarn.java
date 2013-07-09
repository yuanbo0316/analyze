/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.reqfx;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 获取请求异常信息 S34301
 *
 * @author chen
 */
@Service
public class GetReqWarn extends BaseService {

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        String flag = in.getStringValue("is_timeout");
        String sql;
        if (flag.equals("0")){
            sql = "get_req_warn";
        } else  {
            sql = "get_timeout_req_warn";
        }
        in.putObjectValue("args", new Object[]{ in.getDateValue("end"), in.getStringValue("begin"),in.getStringValue("server"),in.getStringValue("req_url")});
        in.putStringValue("sql", sql);
        runService("S10001", in, inHead, out, outHead);
    }
}
