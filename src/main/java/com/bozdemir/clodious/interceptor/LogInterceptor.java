package com.bozdemir.clodious.interceptor;

import com.bozdemir.clodious.controller.FileController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

public class LogInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(FileController.class);
    @Override public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        logger.info("Method: " + method.getName());
        return true;
    }
}
