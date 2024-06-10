package com.project.bridge.service;

import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;

public interface RedisService {
    //key, value저장
    void setValues(String key, Object value);

    void setValues(String key, String data, Duration duration);

    //key를 통해 value 조회
    @Transactional(readOnly = true)
    String getValue(String key);

    //key를 통해 value 삭제
    void delValue(String key);

    void expire(String key, int seconds);

    void setHashOps(String key, Map<String, Object> map);

    @Transactional(readOnly = true)
    String getHash(String key, String hashKey);

    void deleteHash(String key, String hashKey);

    //조회데이터가 없으면 false 반환
    boolean checkExistsValue(String value);
}
