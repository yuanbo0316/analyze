/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.reqfx;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 获取请求异常统计 S34302
 *
 * @author chen
 */
@Service
public class GetReqWarnCount extends BaseService {

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        List<AbstractCommonData> list;
        String flag = in.getStringValue("is_timeout");
        String sql;
        if (flag.equals("0")) {
            sql = "get_req_warn_detail";
        } else{
            sql = "get_timeout_req_warn_detail";
        }
        list = queryList(sql, new Object[]{ in.getDateValue("end"), in.getStringValue("begin"),in.getStringValue("server")});
        out.putArrayValue("result", list);
    }
}
