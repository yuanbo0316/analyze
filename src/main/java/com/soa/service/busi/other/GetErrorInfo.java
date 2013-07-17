/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.other;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * S34501
 * 获取异常日志信息
 * @author lianzt
 */
@Service
public class GetErrorInfo extends BaseService {

    private final String[] KEY = new String[]{"begin", "开始时间", "end", "结束时间"};

    @Override
    public String[] keys() {
        return KEY;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        //select * from log_error_info where save_time>=? and save_time<=? and server=nvl(?,server)
        in.putStringValue("sql", "get_error_info");
        Object[] args = new Object[]{in.getDateValue("begin"), in.getDateValue("end"), in.getStringValue("server")};
        in.putObjectValue("args", args);
        runService("S10001", in, inHead, out, outHead);
    }
}
