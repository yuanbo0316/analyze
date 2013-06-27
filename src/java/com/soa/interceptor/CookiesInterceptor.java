/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.interceptor;

import com.lianzt.commondata.AbstractCommonData;
import com.soa.service.BaseService;
import com.lianzt.util.StringUtil;
import com.soa.util.SystemUtil;
import java.net.InetAddress;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 把sessionid放入cookies，如果cookie为空，就创建一个，该cookie在浏览器关闭时生命周期结束。<br />
 * 服务器会根据该cookie存取redis内存数据库，生成的session_id格式为 ip:pc-name/sessionId，<br />
 * 该过滤器必须放在Encoding过虑器之后
 * @author lianzt
 */
public class CookiesInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(CookiesInterceptor.class);
    /**
     * session id 的保存cookie
     */
    public static final String COOKIE_NAME = "ipt_session_id";
    /**
     * 登录用户的cookie
     */
    public static final String LOGIN_COOKIE = "phoneNum";

    static {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            SystemUtil.node = addr.getHostAddress() + ":" + addr.getHostName() + "/";
            if (log.isInfoEnabled()) {
                log.info(SystemUtil.node + "节点Cookies拦截器初始化完成。。");
            }
        } catch (Exception e) {
            log.warn("节点Cookies拦截器初始化失败，使用默认节点名 " + SystemUtil.node);
        }
    }

    /**
     * 请求执行前执行
     * @param request
     * @param response
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object o) throws Exception {
        AbstractCommonData pageData = (AbstractCommonData) request.getAttribute("page_data");
        if (SystemUtil.sessionCache) {
            if (log.isDebugEnabled()) {
                log.debug("使用session缓存客户状态，不使用cookie.直接退出。");
            }
            //登录状态，
            String phoneNum = BaseService.getSession(pageData).getStringValue(SystemUtil.loginRemark);
            pageData.getDataValue("head").putStringValue(SystemUtil.loginRemark, phoneNum);
            return true;
        }
        if (log.isDebugEnabled()) {
            log.debug("进入CookiesInterceptor....");
        }
        Cookie sesCookie = null;
        Cookie loginCookie = null;
        Cookie[] cookies = request.getCookies();
        //遍历cookie
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (COOKIE_NAME.equals(c.getName())) {
                    sesCookie = c;
                }
                if (LOGIN_COOKIE.equals(c.getName())) {
                    loginCookie = c;
                }
                if (log.isDebugEnabled()) {
                    log.debug("cookie " + c.getName() + "=" + c.getValue());
                }
            }
        }
        if (sesCookie == null) {
            //如果没有，使用sessionid创建一个新cookie，并放入response
            HttpSession session = request.getSession();
            sesCookie = new Cookie(COOKIE_NAME, SystemUtil.node + session.getId());
            sesCookie.setPath(request.getContextPath());
            if (log.isDebugEnabled()) {
                log.debug("cookie为空，创建一个新的cookie ：name=" + sesCookie.getName() + "\tvalue=" + sesCookie.getValue());
            }
            response.addCookie(sesCookie);
        } else {
            //如果找到目标cookie，
            if (log.isDebugEnabled()) {
                log.debug("存在cookie ：name=" + sesCookie.getName() + "\tvalue=" + sesCookie.getValue());
            }
        }
        //把cookie值放入请求头
        if (pageData != null) {
            if (log.isDebugEnabled()) {
                log.debug("把cookie——session_id放入请求报文...");
            }
            AbstractCommonData head = pageData.getDataValue("head");
            head.putStringValue(COOKIE_NAME, sesCookie.getValue());
            //登录状态
            if (loginCookie != null) {
                if (log.isDebugEnabled()) {
                    log.debug("从cookie中取用户登录状态：phone_num=" + loginCookie.getValue());
                }
                if (!StringUtil.isNull(loginCookie.getValue().trim())) {
                    head.putStringValue(SystemUtil.loginRemark, loginCookie.getValue());
                }
            }
        }

        return true;
    }

    /**
     * 请求正常结束时执行
     * @param request
     * @param response
     * @param o
     * @param mav
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object o, ModelAndView mav) throws Exception {
        //如果请求正常结束，而且是集群式部署，需要把修改后的缓存再写入内存数据库
        if (!SystemUtil.sessionCache) {
            //集群的情况
            AbstractCommonData acd = (AbstractCommonData) request.getAttribute("page_data");
            if (acd != null) {
                AbstractCommonData session = acd.getDataValue(COOKIE_NAME);
                if (session != null) {
                    //写入内存数据库。
                    if (log.isDebugEnabled()) {
                        log.debug("把session写入到内存数据库。");
                    }
                }
            }
        }
    }

    /**
     * 如果请求出现异常，不修改内存数据库，这样可以保证数据一致性
     * @param hsr
     * @param hsr1
     * @param o
     * @param excptn
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest hsr, HttpServletResponse hsr1,
                                Object o, Exception excptn) throws Exception {
        //如果请求出现异常，不修改内存数据库，这样可以保证数据一致性
    }
}
