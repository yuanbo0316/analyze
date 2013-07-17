/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.sysserver;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import org.springframework.stereotype.Service;

/**获取异常服务
 * S34201
 * @author chen
 */
@Service
public class GetWarnInfo extends BaseService {

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
             in.putStringValue("sql", "get_warn_info");
        Object[] args = new Object[]{in.getDateValue("begin"), in.getDateValue("end"), in.getStringValue("server"),in.getStringValue("service_code_detail")};
        in.putObjectValue("args", args);
        runService("S10001", in, inHead, out, outHead);
    }
    
}
