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

/**
 * 获取异常信息图标 S34202
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
        Object[] args = new Object[]{in.getDateValue("begin"), in.getDateValue("end"), in.getStringValue("server"), in.getStringValue("service_code_detail")};
        List<AbstractCommonData> list = queryList("get_warn_info", args);
        List<Date> date_list = new ArrayList<Date>();
        List<String> s_list = new ArrayList<String>();
        List<Integer> i_list = new ArrayList<Integer>();
        for (AbstractCommonData a : list) {
            Date date = getDate(a.getDateValue("exec_time"));
            if (date_list.indexOf(date) == -1) {
                date_list.add(date);                
                i_list.add(0);
            }
            if(s_list.indexOf(a.getStringValue("response_desc"))==-1){
                s_list.add(a.getStringValue("response_desc"));
            }
        }
        
        List<AbstractCommonData> l_acd = new ArrayList<AbstractCommonData>();
        for (String date : s_list) {
            AbstractCommonData acd = DataConvertFactory.getInstance();
            List<Integer> in_list = i_list;
            acd.putStringValue("name", date);
            for (AbstractCommonData a : list) {
                if (date.equals(a.getStringValue("response_desc"))) {
                    if (date_list.indexOf(getDate(a.getDateValue("exec_time"))) == -1) {
                    } else {
                        int i = date_list.indexOf(getDate(a.getDateValue("exec_time")));
                        in_list.set(i, in_list.get(i) + 1);
                    }
                }
            }
            acd.putArrayValue("counts", in_list);
            l_acd.add(acd);
        }
        out.putArrayValue("time_detail", date_list);
        List<AbstractCommonData> l_acd_date = new ArrayList<AbstractCommonData>();
        for(Date a:date_list){
            AbstractCommonData asvd = DataConvertFactory.getInstance();
           asvd.putStringValue("save_time", 1900+a.getYear()+"-"+(a.getMonth()+1)+"-"+a.getDay());
            l_acd_date.add(asvd);
        }
        out.putArrayValue("time", l_acd_date);
        out.putArrayValue("result", l_acd);
    }

    public Date getDate(Date date) {
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return date;
    }
}
