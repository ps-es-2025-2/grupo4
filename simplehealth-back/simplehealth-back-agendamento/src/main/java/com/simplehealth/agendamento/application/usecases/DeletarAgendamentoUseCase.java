package com.simplehealth.agendamento.application.usecases;

import com.simplehealth.agendamento.application.exception.AgendamentoException;
import com.simplehealth.agendamento.infrastructure.repositories.ConsultaRepository;
import com.simplehealth.agendamento.infrastructure.repositories.ExameRepository;
import com.simplehealth.agendamento.infrastructure.repositories.ProcedimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletarAgendamentoUseCase {

  private final ConsultaRepository consultaRepository;
  private final ExameRepository exameRepository;
  private final ProcedimentoRepository procedimentoRepository;

  public void execute(String id) {
    // Tentar deletar de cada repositório
    boolean deleted = false;
    
    if (consultaRepository.existsById(id)) {
      consultaRepository.deleteById(id);
      deleted = true;
    } else if (exameRepository.existsById(id)) {
      exameRepository.deleteById(id);
      deleted = true;
    } else if (procedimentoRepository.existsById(id)) {
      procedimentoRepository.deleteById(id);
      deleted = true;
    }
    
    if (!deleted) {
      throw new AgendamentoException("Agendamento não encontrado com ID: " + id);
    }
  }
}

