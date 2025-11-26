package com.simplehealth.agendamento.application.services;

import com.simplehealth.agendamento.domain.entity.Procedimento;
import com.simplehealth.agendamento.infrastructure.repositories.ProcedimentoRepository;
import org.springframework.stereotype.Service;

@Service
public class ProcedimentoService {

  private final ProcedimentoRepository procedimentoRepository;

  public ProcedimentoService(ProcedimentoRepository procedimentoRepository) {
    this.procedimentoRepository = procedimentoRepository;
  }

  public Procedimento salvar(Procedimento procedimento) {
    return procedimentoRepository.save(procedimento);
  }
}
