package com.aiyolo.controller.aspect;

import com.aiyolo.entity.UserLog;
import com.aiyolo.service.UserLogService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by xujun on 2017/5/12.
 */
@Aspect
@Component
public class UserLogAspect {

    private static final Log errorLogger = LogFactory.getLog("errorLog");

    private static final String authPointcutExpr = "execution(* org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider.authenticate(..))";
    private static final String dataPointcutExpr =
            "execution(* com.aiyolo.repository..*.save(..)) || " +
            "execution(* org.springframework.data.repository.CrudRepository.save(..)) || " +
            "execution(* com.aiyolo.repository..*.delete(..)) || " +
            "execution(* org.springframework.data.repository.CrudRepository.delete(..))";

    @Autowired
    UserLogService userLogService;

    @AfterReturning(dataPointcutExpr)
    public void writeDataLog(JoinPoint joinPoint) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                UserLog userLog = new UserLog(
                        request.getRemoteUser(),
                        request.getRequestURI(),
                        Arrays.toString(joinPoint.getArgs()),
                        request.getRemoteAddr());

                userLogService.store(userLog);
            }
        } catch (Exception e) {
            errorLogger.error("Write Data Log Exception.", e);
        }
    }

    @AfterReturning(returning = "result", pointcut = authPointcutExpr)
    public void writeAuthLog(Object result) {
        try {
            if (result instanceof UsernamePasswordAuthenticationToken) {
                User principal = (User) ((UsernamePasswordAuthenticationToken) result).getPrincipal();

                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = attributes.getRequest();

                UserLog userLog = new UserLog(
                        principal.getUsername(),
                        request.getRequestURI(),
                        "",
                        request.getRemoteAddr());

                userLogService.store(userLog);
            }
        } catch (Exception e) {
            errorLogger.error("Write Auth Log Exception.", e);
        }
    }

}
