package com.project.bridge;

import com.project.bridge.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@Slf4j
@SpringBootTest
class BridgeApplicationTests {


	final String KEY = "key";
	final String VALUE = "value";
	final Duration DURATION = Duration.ofMillis(5000);

	@Autowired
	private RedisService redisService;

/*	@BeforeEach
	void shutDown(){
		redisService.setValues(KEY, VALUE, DURATION);
	}*/

	@AfterEach
	void tearDown(){
		redisService.delValue(KEY);
	}

	@Test
	@DisplayName("Redis에 데이터 저장 후 정상 조회")
	void contextLoads() throws Exception{

		//when
		String findValue = redisService.getValue(KEY);

		//then
		assertThat(VALUE).isEqualTo(findValue);
	}
/*
	@Test
	@DisplayName("Redis에 저장된 데이터 수정")
	void updateTest() throws Exception{
		//given
		String updateValue = "updateValue";
		redisService.setValues(KEY, updateValue, DURATION);

		//when
		String findValue = redisService.getValue(KEY);

		//then
		assertThat(updateValue).isEqualTo(findValue);
		assertThat(VALUE).isNotEqualTo(findValue);
	}*/

	@Test
	@DisplayName("Redis에 저장된 데이터 삭제")
	void deleteTest() throws Exception{
		//when
		redisService.delValue(KEY);
		String findValue = redisService.getValue(KEY);

		//then
		assertThat(findValue).isEqualTo("false");
	}

	@Test
	@DisplayName("Redis에 저장된 데이터는 만료시간이 지나면 삭제")
	void expireTest() throws Exception{
		//when
		String findValue = redisService.getValue(KEY);
		await().pollDelay(Duration.ofMillis(6000)).untilAsserted(
				() ->{
					String expiredValue = redisService.getValue(KEY);
					assertThat(expiredValue).isNotEqualTo(findValue);
					assertThat(expiredValue).isEqualTo("false");
				}
		);
	}
}
