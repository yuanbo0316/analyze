/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.service.busi.archive;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.exception.GlobalException;
import com.soa.plugin.impl.AnalyzePlugin;
import com.soa.service.BaseService;
import com.soa.service.impl.UtilService;
import com.soa.util.SystemUtil;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * S31001
 * 保存所有快照
 * @author lianzt
 */
@Service
public class SaveAllFlashTask extends BaseService {

    private final Logger log = LoggerFactory.getLogger(SaveAllFlashTask.class);
    @Resource
    private UtilService utilServiceImpl;

    @Override
    public String[] keys() {
        return null;
    }

    @Override
    public void execute(AbstractCommonData in, AbstractCommonData inHead, AbstractCommonData out, AbstractCommonData outHead) {
        Map<String, AnalyzePlugin> analyzes = SystemUtil.wac.getBeansOfType(AnalyzePlugin.class);
        log.debug("分析器:{}", analyzes.keySet());
        for (Map.Entry<String, AnalyzePlugin> e : analyzes.entrySet()) {
            try {
                e.getValue().saveFlash();
            } catch (GlobalException ge) {
                log.warn("分析器 {} 保存快照异常 :", e.getKey(), ge);
                if (ge.getErrorCode() > 900000) {
                    utilServiceImpl.saveError(ge);      //此处每次执行都是一个单独的事务，一条异常不影响其它保存
                }
            }
        }
    }
}
