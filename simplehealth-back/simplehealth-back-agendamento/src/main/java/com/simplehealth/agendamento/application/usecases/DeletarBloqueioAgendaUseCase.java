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
    BloqueioAgenda bloqueio = bloqueioAgendaRepository.findById(id)
        .orElseThrow(() -> new AgendamentoException("Bloqueio de agenda não encontrado com ID: " + id));

    // Permite deletar bloqueios inativos
    if (!bloqueio.getAtivo()) {
      bloqueioAgendaRepository.deleteById(id);
      return;
    }

    // Validar se o bloqueio já passou
    if (bloqueio.getDataInicio().isBefore(LocalDateTime.now())) {
      throw new IllegalStateException("Não é possível deletar um bloqueio que já iniciou");
    }

    // Deletar fisicamente
    bloqueioAgendaRepository.deleteById(id);
  }
}

