/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.user;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 获取用户行为 S34112
 *
 * @author chen
 */
@Service
public class GetUserTraking extends BaseService {

    private final Logger log = LoggerFactory.getLogger(GetUserTraking.class);

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        int i = 0;
        String ip = in.getStringValue("ip_addr");
        String username = in.getStringValue("username_trake");
        List<AbstractCommonData> service_list;
        List<AbstractCommonData> req_list;
        if (ip != null && username != null) {
            service_list = queryList("get_user_service_by_all", new Object[]{in.getStringValue("end"), in.getStringValue("begin"),  ip,username});
            req_list = queryList("get_user_req_by_all", new Object[]{in.getStringValue("end"), in.getStringValue("begin"),  ip,username});
        } else if (ip == null && username != null) {
            service_list = queryList("get_user_service_by_username", new Object[]{in.getStringValue("end"), in.getStringValue("begin"),username});
            req_list = queryList("get_user_req_by_username", new Object[]{in.getStringValue("end"), in.getStringValue("begin"),username});
        } else if (ip != null && username == null) {
            service_list = queryList("get_user_service_by_ip", new Object[]{in.getStringValue("end"), in.getStringValue("begin"), ip});
            req_list = queryList("get_user_req_by_ip", new Object[]{in.getStringValue("end"), in.getStringValue("begin"), ip});
        } else {
            service_list = queryList("get_user_service", new Object[]{in.getStringValue("end"), in.getStringValue("begin")});
            req_list = queryList("get_user_req", new Object[]{in.getStringValue("end"), in.getStringValue("begin")});
        }
        List<AbstractCommonData> result = new ArrayList<AbstractCommonData>();
        Date req_date, service_date;
        for (int j = 0; j < req_list.size(); j++) {
            req_date = req_list.get(j).getDateValue("req_time");
            for (i = 0; i < service_list.size(); i++) {
                service_date = service_list.get(i).getDateValue("req_time");
                if (service_date.before(req_date) || service_date.equals(req_date)) {
                } else {
                    break;
                }
            }
            result.add(req_list.get(j));
            for (int k = i - 1; k >= 0; k--) {
                result.add(service_list.get(k));
                service_list.remove(k);
            }
        }
        out.putArrayValue("result", result);
    }
}
