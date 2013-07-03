/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.archive;

import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.util.DateUtil;
import com.lianzt.util.StringUtil;
import com.soa.service.BaseService;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * P32009
 * 用户登录信息归档
 * 说明：每次执行对前一天的日志进行归档，整理成用户的登录历史和系统用户登录人次表，然后删除30天之前的数据；
 * （用户登录历史表：每个用户从一类终端登录记录一条数据，记录最后登录时间和登录总次数）
 * （用户登录人次表：每个监控子系统每天记录一条数据，记录用户数和登录次数）
 * @author lianzt
 */
@Service
public class LoginArchiveTask extends BaseService{

    private final int DEL_DAY = -30;     //删除1天前的日志
    @Override
    public String[] keys() {
        return null;
    }

    @Override
    @Transactional
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        Date time = new Date();
        time = DateUtil.setHour(time, 0);
        time = DateUtil.setMinute(time, 0);
        time = DateUtil.setSecond(time, 0);
        Date yesterday = DateUtil.addDay(time, -1);
        //归档前一天的用户登录数量
        //insert into log_login_times select ?,server,count(*),sum(c) from (select server, username,count(*) c from log_login_info where exec_time>=? and exec_time<? and opt in ('1','4') group by server,username) a group by server
        update("save_login_times", yesterday, yesterday, time);
        //获取昨天的登录用户
        //左外连接，如果 b.username u 项为空，表示为新数据
        List<AbstractCommonData> list = queryList("get_user_login_time", yesterday, time);
        //insert into log_login_his (server,username,from_node,last_login,times) value (?,?,?,?,?)
        List<Object[]> insertArgs = new LinkedList<Object[]>();
        //update log_login_his set last_login=?, times=times+? where server=? and username=? and from_node=?
        List<Object[]> updateArgs = new LinkedList<Object[]>();
        Object[] argsTemp = null;
        if(list != null && !list.isEmpty()){
            for(AbstractCommonData acd : list){
                argsTemp = new Object[5];
                if(StringUtil.isNull(acd.getStringValue("u"))){     //左外连接，如果 b.username u 项为空，表示为新数据
                    argsTemp[0] = acd.getStringValue("server");
                    argsTemp[1] = acd.getStringValue("username");
                    argsTemp[2] = acd.getStringValue("from_node");
                    argsTemp[3] = acd.getDateValue("last_login");
                    argsTemp[4] = acd.getIntValue("times");
                    insertArgs.add(argsTemp);
                }else{
                    argsTemp[0] = acd.getDateValue("last_login");
                    argsTemp[1] = acd.getIntValue("times");
                    argsTemp[2] = acd.getStringValue("server");
                    argsTemp[3] = acd.getStringValue("username");
                    argsTemp[4] = acd.getStringValue("from_node");
                    updateArgs.add(argsTemp);
                }
            }
            batchUpdate("insert_login_his", insertArgs);
            batchUpdate("update_login_his", updateArgs);
        }

        time = DateUtil.addDay(time, DEL_DAY);
        //删除30天之前的数据
        update("delete_login_info", time);
    }
}
