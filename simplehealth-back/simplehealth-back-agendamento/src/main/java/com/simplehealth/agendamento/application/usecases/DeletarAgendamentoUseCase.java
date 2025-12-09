package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.exception.AgendamentoException;
import com.simplehealth.agendamento.application.services.AgendamentoService;
import com.simplehealth.agendamento.domain.entity.Agendamento;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.infrastructure.repositories.AgendamentoRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletarAgendamentoUseCase {

  private final AgendamentoService agendamentoService;
  private final AgendamentoRepository agendamentoRepository;

  public void execute(String id) {
    agendamentoService.buscarPorId(id)
        .orElseThrow(() -> new AgendamentoException("Agendamento não encontrado com ID: " + id));

    agendamentoRepository.deleteById(id);
  }
}

