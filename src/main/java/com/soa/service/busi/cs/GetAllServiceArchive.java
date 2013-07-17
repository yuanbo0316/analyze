/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.cs;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**获取全部服务请求情况
 * S33005
 * @author chen
 */
@Service
public class GetAllServiceArchive extends BaseService{

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
       List<AbstractCommonData> list = queryList("get_all_service_archive");
       List<String> sql_name = new ArrayList<String>();
       List<Integer> avg_time = new ArrayList<Integer>();
       for(AbstractCommonData a:list){
           sql_name.add(a.getStringValue("service_code"));
           avg_time.add(Integer.parseInt(a.getStringValue("avg_run_time")));
       }
       out.putArrayValue("sql_name", sql_name);
       out.putArrayValue("avg_time", avg_time);
    }
    
}
