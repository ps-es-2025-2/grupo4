package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.exception.AgendamentoException;
import com.simplehealth.agendamento.domain.entity.BloqueioAgenda;
import com.simplehealth.agendamento.infrastructure.repositories.BloqueioAgendaRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletarBloqueioAgendaUseCase {

  private final BloqueioAgendaRepository bloqueioAgendaRepository;

  public void execute(String id) {
    bloqueioAgendaRepository.findById(id)
        .orElseThrow(() -> new AgendamentoException("Bloqueio de agenda não encontrado com ID: " + id));

    bloqueioAgendaRepository.deleteById(id);
  }
}

