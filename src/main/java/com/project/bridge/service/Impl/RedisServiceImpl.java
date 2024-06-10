package com.project.bridge.service.Impl;

import com.project.bridge.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional
@Component
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    //key, value저장
    public void setValues(String key, Object value) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, value);
    }

    @Override
    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    //key를 통해 value 조회
    @Override
    @Transactional(readOnly = true)
    public String getValue(String key) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        if(ops.get(key) == null){
            return "false";
        }
        return (String)ops.get(key);
    }

    //key를 통해 value 삭제
    @Override
    public void delValue(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void expire(String key, int seconds) {
        redisTemplate.expire(key, seconds, TimeUnit.MICROSECONDS);
    }

    @Override
    public void setHashOps(String key, Map<String, Object> map) {
        HashOperations<String, Object, Object> ops = redisTemplate.opsForHash();
        ops.putAll(key, map);
    }

    @Override
    @Transactional(readOnly = true)
    public String getHash(String key, String hashKey) {
        HashOperations<String, Object, Object> ops = redisTemplate.opsForHash();
        return Boolean.TRUE.equals(ops.hasKey(key, hashKey)) ? (String) redisTemplate.opsForHash().get(key, hashKey) : "";
    }

    @Override
    public void deleteHash(String key, String hashKey) {
        HashOperations<String, Object, Object> ops = redisTemplate.opsForHash();
        ops.delete(key, hashKey);
    }

    //조회데이터가 없으면 false 반환
    @Override
    public boolean checkExistsValue(String value) {
        return !value.equals("false");
    }

}
