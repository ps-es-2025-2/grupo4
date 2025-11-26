package com.simplehealth.agendamento.web.subscribers;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class BloqueioAgendaSubscriber implements MessageListener {

  @Override
  public void onMessage(Message message, byte[] pattern) {
    String json = new String(message.getBody());
    System.out.println("Evento de bloqueio de agenda recebido: " + json);
  }
}
