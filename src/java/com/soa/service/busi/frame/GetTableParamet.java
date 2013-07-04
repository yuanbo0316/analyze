/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.frame;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import org.springframework.stereotype.Service;

/**
 * S10001
 * 获取系统参数
 * @author lianzt
 */
@Service
public class GetTableParamet extends BaseService {

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        //select * from st_table_paramet
        out.putArrayValue("st_para", queryList("get_table_paramet"));
    }
}
