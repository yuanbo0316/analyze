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
 * 获取全部异常信息 S34203
 *
 * @author chen
 */
@Service
public class GetAllWarnDetail extends BaseService {
    
    @Override
    public String[] keys() {
        return null;
    }
    
    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        Object[] args = new Object[]{in.getDateValue("begin"), in.getDateValue("end"), in.getStringValue("server"), in.getStringValue("service_code_detail")};
        List<AbstractCommonData> list = queryList("get_all_warn", args);
        out.putArrayValue("result", list);
    }
}
