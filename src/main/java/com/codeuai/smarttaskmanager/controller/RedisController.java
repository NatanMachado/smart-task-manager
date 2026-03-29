package com.codeuai.smarttaskmanager.controller;

import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
@Profile("dev") // Only enable this controller in the 'dev' profile
public class RedisController {

    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/set")
    public Object set(@RequestParam String key, @RequestParam String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return redisTemplate.opsForValue().get(key); // Output: value
        } catch (Exception e) {
            return "Redis error: " + e.getMessage();
        }
    }

    @GetMapping("/keys")
    public Object getAll() {
        try {
            Set<String> keys = redisTemplate.keys("*");
            if (keys == null || keys.isEmpty()) {
                return "No keys found";
            }
            return keys;
        } catch (Exception e) {
            return "Redis error: " + e.getMessage();
        }
    }

    @GetMapping("/delete")
    public Object deletePattern(@RequestParam String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys == null || keys.isEmpty())
            return "No keys found";
        
        redisTemplate.delete(keys);
        return "Deleted " + keys.size() + " keys";
    }

    @PostMapping("/flush")
    public void flush() {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
    }

}
