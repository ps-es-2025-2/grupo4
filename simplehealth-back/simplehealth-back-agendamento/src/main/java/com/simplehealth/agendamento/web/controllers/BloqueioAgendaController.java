package com.simplehealth.agendamento.web.controllers;

import com.simplehealth.agendamento.application.dtos.BloqueioAgendaDTO;
import com.simplehealth.agendamento.application.usecases.RegistrarBloqueioAgendaUseCase;
import com.simplehealth.agendamento.domain.entity.BloqueioAgenda;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bloqueio-agenda")
public class BloqueioAgendaController {

  private final RegistrarBloqueioAgendaUseCase registrarBloqueioAgendaUseCase;

  public BloqueioAgendaController(RegistrarBloqueioAgendaUseCase registrarBloqueioAgendaUseCase) {
    this.registrarBloqueioAgendaUseCase = registrarBloqueioAgendaUseCase;
  }

  @PostMapping
  public ResponseEntity<BloqueioAgenda> criar(@RequestBody BloqueioAgendaDTO dto) {
    return ResponseEntity.ok(registrarBloqueioAgendaUseCase.registrar(dto));
  }
}
