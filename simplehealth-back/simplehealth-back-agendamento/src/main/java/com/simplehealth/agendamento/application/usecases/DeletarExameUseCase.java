package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.exception.AgendamentoException;
import com.simplehealth.agendamento.domain.entity.Exame;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.infrastructure.repositories.ExameRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletarExameUseCase {

  private final ExameRepository exameRepository;

  public void execute(String id) {
    Exame exame = exameRepository.findById(id)
        .orElseThrow(() -> new AgendamentoException("Exame não encontrado com ID: " + id));

    if (exame.getStatus() == StatusAgendamentoEnum.CANCELADO) {
      exameRepository.deleteById(id);
      return;
    }

    if (exame.getDataHoraInicioExecucao() != null) {
      throw new IllegalStateException("Não é possível deletar um exame que já foi iniciado");
    }

    exameRepository.deleteById(id);
  }
}

