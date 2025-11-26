package com.simplehealth.agendamento.application.services;

import com.simplehealth.agendamento.domain.entity.Exame;
import com.simplehealth.agendamento.infrastructure.repositories.ExameRepository;
import org.springframework.stereotype.Service;

@Service
public class ExameService {

  private final ExameRepository exameRepository;

  public ExameService(ExameRepository exameRepository) {
    this.exameRepository = exameRepository;
  }

  public Exame salvar(Exame exame) {
    return exameRepository.save(exame);
  }
}
