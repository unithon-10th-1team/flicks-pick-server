package com.flickspick.common.utils;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;

public class HttpUtil {
    public static String getClientIpAddress(HttpServletRequest request) {
        var headers =
                new String[] {
                    "X-Forwarded-For",
                    "Proxy-Client-IP",
                    "WL-Proxy-Client-IP",
                    "HTTP_CLIENT_IP",
                    "HTTP_X_FORWARDED_FOR"
                };

        return Arrays.stream(headers)
                .map(request::getHeader)
                .filter(ip -> ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip))
                .findFirst()
                .orElse(request.getRemoteAddr());
    }
}
