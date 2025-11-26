package com.simplehealth.agendamento.web.subscribers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

  @Bean
  RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
      MessageListenerAdapter agendamentoListener,
      MessageListenerAdapter bloqueioListener,
      MessageListenerAdapter encaixeListener) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(agendamentoListener, new PatternTopic("agendamento.criado"));
    container.addMessageListener(bloqueioListener, new PatternTopic("bloqueio.criado"));
    container.addMessageListener(encaixeListener, new PatternTopic("encaixe.solicitado"));
    return container;
  }

  @Bean
  MessageListenerAdapter agendamentoListener(AgendamentoSubscriber subscriber) {
    return new MessageListenerAdapter(subscriber);
  }

  @Bean
  MessageListenerAdapter bloqueioListener(BloqueioAgendaSubscriber subscriber) {
    return new MessageListenerAdapter(subscriber);
  }

  @Bean
  MessageListenerAdapter encaixeListener(EncaixeSubscriber subscriber) {
    return new MessageListenerAdapter(subscriber);
  }
}
