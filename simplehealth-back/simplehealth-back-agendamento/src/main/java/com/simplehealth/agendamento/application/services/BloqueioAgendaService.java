package com.simplehealth.agendamento.application.services;

import com.simplehealth.agendamento.domain.entity.BloqueioAgenda;
import com.simplehealth.agendamento.infrastructure.repositories.BloqueioAgendaRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class BloqueioAgendaService {

  private final BloqueioAgendaRepository bloqueioRepository;

  public BloqueioAgendaService(BloqueioAgendaRepository bloqueioRepository) {
    this.bloqueioRepository = bloqueioRepository;
  }

  public boolean existemAgendamentosAtivos(String crm, LocalDateTime inicio, LocalDateTime fim) {
    return bloqueioRepository.existemAgendamentosAtivosNoPeriodo(crm, inicio, fim);
  }

  public BloqueioAgenda salvar(BloqueioAgenda entidade) {
    return bloqueioRepository.save(entidade);
  }
}
