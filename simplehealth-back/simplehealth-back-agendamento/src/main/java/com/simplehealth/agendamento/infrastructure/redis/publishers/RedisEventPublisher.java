package com.simplehealth.agendamento.infrastructure.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisEventPublisher {

  private final RedisTemplate<String, Object> redisTemplate;

  public RedisEventPublisher(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void publicar(String canal, Object evento) {
    redisTemplate.convertAndSend(canal, evento);
  }
}
