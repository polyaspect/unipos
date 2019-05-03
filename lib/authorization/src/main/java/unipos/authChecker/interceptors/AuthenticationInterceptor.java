package unipos.authChecker.interceptors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;
import unipos.authChecker.annotations.RequiresAuthentication;
import unipos.authChecker.annotations.RequiresPermissions;
import unipos.authChecker.domain.AuthTokenManager;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Dominik on 19.06.2015.
 */
@Aspect
@Component
public class AuthenticationInterceptor {

    @Autowired
    AuthTokenManager tokenManager;

    @Autowired
    AuthenticationManager authenticationManager;

    @Pointcut("@annotation(unipos.authChecker.annotations.RequiresAuthentication)")
    public void requiresPermission() {}

    @Pointcut("@annotation(unipos.authChecker.annotations.RequiresPermissions)")
    public void anyAnnotatedMethod() {}

    @Around("anyAnnotatedMethod() && @annotation(requiresPermissions)")
    public Object doAuthenticationCheck(ProceedingJoinPoint thisJoinPoint, RequiresPermissions requiresPermissions) throws IOException{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        boolean isAuthorized = authenticationManager.checkPermissionsWithErrorResponse(request, response, requiresPermissions.value());

        if(isAuthorized){
            try {
                Object returnValue = thisJoinPoint.proceed();
                return returnValue;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        return null;
    }

    @Around("requiresPermission() && @annotation(requiresAuthentication)")
    public Object checkForAuthentication(ProceedingJoinPoint thisJoinPoint, RequiresAuthentication requiresAuthentication) {
//        System.out.print("It Works!!! PermissionValue: " + requiresPermissions.value());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        Cookie cookie = WebUtils.getCookie(request, "AuthToken");

        if(cookie == null) {
            response.setStatus(401);
            return null;
        }

        boolean isAuthorized = tokenManager.isAuthorized(cookie.getValue());

        if(isAuthorized) {
            try {
                Object returnValue = thisJoinPoint.proceed();
                return returnValue;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            response.setStatus(401);
        }
        return null;

    }
}
