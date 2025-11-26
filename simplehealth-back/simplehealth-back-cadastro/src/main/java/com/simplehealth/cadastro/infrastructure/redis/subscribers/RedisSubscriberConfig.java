package com.simplehealth.cadastro.infrastructure.redis.subscribers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisSubscriberConfig {

  @Bean
  RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
      MessageListenerAdapter agendamentoListener) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(agendamentoListener, new PatternTopic("agendamento.criado"));
    return container;
  }

  @Bean
  MessageListenerAdapter agendamentoListener(AgendamentoEventSubscriber subscriber) {
    return new MessageListenerAdapter(subscriber);
  }
}
