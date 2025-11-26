package com.simplehealth.cadastro.infrastructure.redis.subscribers;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class AgendamentoEventSubscriber implements MessageListener {

  @Override
  public void onMessage(Message message, byte[] pattern) {
    String json = new String(message.getBody());
    System.out.println("[Cadastro] Evento de agendamento recebido: " + json);
  }
}
