/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.sysserver;

import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.commondata.DataConvertFactory;
import com.soa.service.BaseService;
import com.sun.corba.se.impl.orb.DataCollectorFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**获取异常信息图标
 *  S34202
 *
 * @author chen
 */
@Service
public class GetWarnDetail extends BaseService {

    private final Logger log = LoggerFactory.getLogger(GetWarnDetail.class);

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        String flag = "";
        Object[] args = new Object[]{in.getDateValue("begin"), in.getDateValue("end"), in.getStringValue("server"), in.getStringValue("service_code_detail")};
        List<AbstractCommonData> list =  queryList("get_warn_detail", args);        
        out.putArrayValue("result", list);
        out.putStringValue("flag", flag);
    }
}
