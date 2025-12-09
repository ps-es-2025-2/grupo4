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
    procedimentoRepository.findById(id)
        .orElseThrow(() -> new AgendamentoException("Procedimento não encontrado com ID: " + id));

    procedimentoRepository.deleteById(id);
  }
}

