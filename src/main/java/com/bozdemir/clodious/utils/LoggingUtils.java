package com.bozdemir.clodious.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Enumeration;

public class LoggingUtils {

    public static String getRequestParams(HttpServletRequest request) {
        StringBuilder params = new StringBuilder();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            params.append(paramName).append("=");
            for (String paramValue : paramValues) {
                params.append(paramValue).append(",");
            }
            params.deleteCharAt(params.length() - 1); // Remove trailing comma
            params.append(";");
        }
        if (params.length() > 0) {
            params.deleteCharAt(params.length() - 1); // Remove trailing semicolon
        }
        return params.toString();
    }
}
