/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.reqfx;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import org.springframework.stereotype.Service;

/**获取照片请求
 *  S34303
 * @author chen
 */
@Service
public class GetPicRequest extends BaseService{

    @Override
    public String[] keys() {
       return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        String flag = in.getStringValue("is_timeout");
        String sql="";
        if(flag.equals("2")){
            sql="get_all_pic";
        }else if(flag.equals("1")){
            sql="get_sus_pic";
        }else{
            sql="get_fail_pic";
        }
        in.putStringValue("sql", sql);
        in.putObjectValue("args", new Object[]{in.getDateValue("end"),in.getDateValue("begin")});
        runService("S10001",in,inHead,out,outHead);
    }
    
}
