package com.simplehealth.agendamento.application.services;

import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.infrastructure.repositories.ConsultaRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ConsultaService {

  private final ConsultaRepository consultaRepository;

  public ConsultaService(ConsultaRepository consultaRepository) {
    this.consultaRepository = consultaRepository;
  }

  public Consulta salvar(Consulta consulta) {
    return consultaRepository.save(consulta);
  }

  public Optional<Consulta> buscarPorId(Long id) {
    return consultaRepository.findById(String.valueOf(id));
  }
}
