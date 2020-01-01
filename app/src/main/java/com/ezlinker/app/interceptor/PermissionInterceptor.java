package com.ezlinker.app.interceptor;

import com.ezlinker.app.modules.user.model.PermissionDetail;
import com.ezlinker.app.modules.user.model.UserDetail;
import com.ezlinker.app.utils.UserTokenUtil;
import com.ezlinker.app.common.exception.NotFoundException;
import com.ezlinker.app.common.exception.XException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: ezlinker
 * @description: 认证拦截器
 * @author: wangwenhai
 * @create: 2019-11-07 10:02
 **/
public class PermissionInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("来自:" + getIpAddress(request));

        if (!hasToken(request)) {
            throw new XException(401, "Require token", "Token缺失");
        }

        /**
         * 如果匹配到了，开始检查权限
         */
        if (handler instanceof HandlerMethod) {
            String httpMethod = request.getMethod().toUpperCase();
            UserDetail userDetail = UserTokenUtil.parse(request.getHeader("token"));
            if (userDetail.getPermissions().size() < 1) {
                throw new XException(402, "No permission", "没有权限");
            }
            String resource = request.getServletPath();
            System.out.println("HttpMethod:" + httpMethod + " resource:" + resource);

            Map<String, Object> allow = new HashMap<>();
            Map<String, Object> methods = new HashMap<>();

            for (PermissionDetail permissionDetail : userDetail.getPermissions()) {
                allow.put(permissionDetail.getResource(), permissionDetail.getAllow());
                methods.put(permissionDetail.getResource(), permissionDetail.getMethods());
            }

            return true;


        } else {
            throw new NotFoundException();
        }
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }

        return ip;
    }

    /**
     * 是否包含Token
     *
     * @param request
     * @return
     */
    private boolean hasToken(HttpServletRequest request) {

        String token = request.getHeader("token");
        return token != null && token.length() >= 20;

    }
}
