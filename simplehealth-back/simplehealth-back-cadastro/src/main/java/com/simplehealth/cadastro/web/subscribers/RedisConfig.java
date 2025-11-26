package com.simplehealth.cadastro.web.subscribers;

import com.simplehealth.cadastro.infrastructure.redis.subscribers.HistoricoResponseSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory();
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
      HistoricoResponseSubscriber subscriber) {

    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);

    container.addMessageListener(subscriber,
        new PatternTopic("historico.agendamento.response"));
    container.addMessageListener(subscriber,
        new PatternTopic("historico.procedimento.response"));
    container.addMessageListener(subscriber,
        new PatternTopic("historico.estoque.response"));
    container.addMessageListener(subscriber,
        new PatternTopic("historico.pagamento.response"));

    return container;
  }

  @Bean
  public MessageListenerAdapter historicoResponseListener(HistoricoResponseSubscriber subscriber) {
    return new MessageListenerAdapter(subscriber);
  }
}


