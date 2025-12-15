package com.simplehealth.cadastro.web.subscribers;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.simplehealth.cadastro.infrastructure.redis.subscribers.HistoricoResponseSubscriber;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

  @Bean
  public MessageListenerAdapter historicoResponseListener(HistoricoResponseSubscriber subscriber) {
    return new MessageListenerAdapter(subscriber, "onMessage");
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    return template;
  }

  @Bean
  public RedisMessageListenerContainer redisContainer(
      RedisConnectionFactory connectionFactory,
      MessageListenerAdapter historicoResponseListener) {

    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setRecoveryInterval(5000L); // Tenta reconectar a cada 5 segundos

    container.addMessageListener(historicoResponseListener, new PatternTopic("historico.consulta.response"));
    container.addMessageListener(historicoResponseListener, new PatternTopic("historico.exame.response"));
    container.addMessageListener(historicoResponseListener, new PatternTopic("historico.agendamento.response"));
    container.addMessageListener(historicoResponseListener, new PatternTopic("historico.procedimento.response"));
    container.addMessageListener(historicoResponseListener, new PatternTopic("historico.estoque.response"));
    container.addMessageListener(historicoResponseListener, new PatternTopic("historico.pagamento.response"));

    return container;
  }

  @Bean
  public ConcurrentHashMap<String, Object> cache() {
    return new ConcurrentHashMap<>();
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }

}
