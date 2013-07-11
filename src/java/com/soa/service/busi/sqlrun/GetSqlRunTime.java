/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.sqlrun;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 获取SQL运行时间 S34401
 *
 * @author chen
 */
@Service
public class GetSqlRunTime extends BaseService{

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        List<AbstractCommonData> list = queryList("get_sql_run_time", new Object[]{in.getStringValue("server"),in.getStringValue("sql_name")});
        out.putArrayValue("list", list);
    }
}
