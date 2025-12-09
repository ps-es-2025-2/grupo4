package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.exception.AgendamentoException;
import com.simplehealth.agendamento.domain.entity.Procedimento;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.infrastructure.repositories.ProcedimentoRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletarProcedimentoUseCase {

  private final ProcedimentoRepository procedimentoRepository;

  public void execute(String id) {
    Procedimento procedimento = procedimentoRepository.findById(id)
        .orElseThrow(() -> new AgendamentoException("Procedimento não encontrado com ID: " + id));

    if (procedimento.getStatus() == StatusAgendamentoEnum.CANCELADO) {
      procedimentoRepository.deleteById(id);
      return;
    }

    if (procedimento.getDataHoraInicioExecucao() != null) {
      throw new IllegalStateException("Não é possível deletar um procedimento que já foi iniciado");
    }

    procedimentoRepository.deleteById(id);
  }
}

