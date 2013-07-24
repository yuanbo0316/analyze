/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.reqfx;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import org.springframework.stereotype.Service;

/**
 * 获取照片请求执行成功率 S34305
 *
 * @author chen
 */
@Service
public class GetPicRequestSusFail extends BaseService{

    @Override
    public String[] keys() {
       return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
       AbstractCommonData acd = queryData("get_pic_sus_fail_num", new Object[]{in.getStringValue("end"),in.getStringValue("begin")});
       out.putDataValue("result", acd);
    }
}
