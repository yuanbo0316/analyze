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
 *
 * @author chen
 */
@Service
public class GetPicRequestRunTime extends BaseService{

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        List<AbstractCommonData> list = queryList("get_pic_req_run_time", new Object[]{in.getStringValue("end"),in.getStringValue("begin")});
        out.putArrayValue("list", list);
    }
    
}
