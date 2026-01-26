package com.codeuai.smarttaskmanager.config;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("userKeyGenerator")
public class UserKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "user:" + username;
    }
}

