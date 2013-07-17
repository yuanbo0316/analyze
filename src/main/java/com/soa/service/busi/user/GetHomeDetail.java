/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.user;

import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.util.DateUtil;
import com.soa.service.BaseService;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/**获取首页展示信息
 * S34103
 * @author chen
 */
@Service
public class GetHomeDetail extends BaseService{

    @Override
    public String[] keys() {
       return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
       List<AbstractCommonData> all_list = queryList("get_home_all_detail");
        Date end = new Date();
        Date begin = new Date();
        begin = DateUtil.setHour(begin, 0);
        begin = DateUtil.setMinute(begin, 0);
        begin = DateUtil.setSecond(begin, 0);
        List<AbstractCommonData> today_list = queryList("get_home_detail", new Object[]{end,begin});
        out.putArrayValue("today", today_list);
        out.putArrayValue("all", all_list);
    }
    
}
