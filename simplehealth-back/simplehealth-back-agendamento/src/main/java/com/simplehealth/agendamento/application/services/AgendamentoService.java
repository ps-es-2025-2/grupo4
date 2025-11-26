package com.simplehealth.agendamento.application.services;

import com.simplehealth.agendamento.domain.entity.Agendamento;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.infrastructure.repositories.AgendamentoRepository;
import com.simplehealth.agendamento.infrastructure.repositories.BloqueioAgendaRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class AgendamentoService {

  private final AgendamentoRepository agendamentoRepository;
  private final BloqueioAgendaRepository bloqueioRepository;

  public AgendamentoService(
      AgendamentoRepository agendamentoRepository,
      BloqueioAgendaRepository bloqueioRepository
  ) {
    this.agendamentoRepository = agendamentoRepository;
    this.bloqueioRepository = bloqueioRepository;
  }

  public void verificarDisponibilidade(
      String medicoCrm,
      LocalDateTime inicio,
      LocalDateTime fim
  ) throws Exception {

    if (!agendamentoRepository.verificarConflitoHorario(medicoCrm, inicio, fim).isEmpty()) {
      throw new Exception("Horário indisponível.");
    }

    if (!bloqueioRepository.findBloqueiosConflitantes(medicoCrm, inicio, fim).isEmpty()) {
      throw new Exception("Horário bloqueado.");
    }
  }

  public void validarCancelamento(Agendamento agendamento, String motivo) {

    if (agendamento.getStatus() == StatusAgendamentoEnum.CANCELADO) {
      throw new IllegalStateException("O agendamento já está cancelado.");
    }

    if (motivo == null || motivo.isBlank()) {
      throw new IllegalArgumentException("O motivo do cancelamento é obrigatório.");
    }

    if (agendamento.getDataHoraInicio().isBefore(LocalDateTime.now())) {
      throw new IllegalStateException("Não é possível cancelar um agendamento que já ocorreu.");
    }
  }
}
