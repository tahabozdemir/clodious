package com.bozdemir.clodious.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LogInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
        if (handler instanceof HandlerMethod handlerMethod) {
            String methodName = handlerMethod.getMethod().getName();
            String requestMethod = request.getMethod();
            String requestURI = request.getRequestURI();
            String responseType = handlerMethod.getReturnType().getParameterType().getName();
            logger.info("Request Method: {} | Method: {} | Request URI: {} | Response Type: {} | View: {} ",
                    requestMethod, methodName, requestURI, responseType, modelAndView != null ? modelAndView.getViewName() : "N/A");
        } else {
            logger.warn("Handler is not a HandlerMethod. Logging may be incomplete.");
        }
    }
}
