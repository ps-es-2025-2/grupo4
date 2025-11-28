package com.simplehealth.agendamento.web.controllers;

import com.simplehealth.agendamento.application.dtos.EncaixeDTO;
import com.simplehealth.agendamento.application.usecases.SolicitarEncaixeUseCase;
import com.simplehealth.agendamento.domain.entity.Consulta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/encaixe")
public class EncaixeController {

  private final SolicitarEncaixeUseCase solicitarEncaixeUseCase;

  public EncaixeController(SolicitarEncaixeUseCase solicitarEncaixeUseCase) {
    this.solicitarEncaixeUseCase = solicitarEncaixeUseCase;
  }

  @PostMapping
  public ResponseEntity<Consulta> solicitar(@RequestBody EncaixeDTO dto) throws Exception {
    return ResponseEntity.ok(solicitarEncaixeUseCase.execute(dto));
  }
}
