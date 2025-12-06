package com.simplehealth.cadastro.application.usecases;

import com.simplehealth.cadastro.application.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeletarPacienteUseCase {

  private final PacienteService pacienteService;

  @Transactional
  public void execute(Long id) {
    pacienteService.delete(id);
  }
}
