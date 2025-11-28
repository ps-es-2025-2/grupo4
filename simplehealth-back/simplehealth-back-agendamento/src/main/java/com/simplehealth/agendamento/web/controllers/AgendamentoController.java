package com.simplehealth.agendamento.web.controllers;

import com.simplehealth.agendamento.application.dtos.AgendarConsultaDTO;
import com.simplehealth.agendamento.application.dtos.CancelarAgendamentoDTO;
import com.simplehealth.agendamento.application.usecases.AgendarConsultaUseCase;
import com.simplehealth.agendamento.application.usecases.CancelarAgendamentoUseCase;
import com.simplehealth.agendamento.domain.entity.Consulta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

  private final AgendarConsultaUseCase agendarConsultaUseCase;
  private final CancelarAgendamentoUseCase cancelarAgendamentoUseCase;

  public AgendamentoController(
      AgendarConsultaUseCase agendarConsultaUseCase,
      CancelarAgendamentoUseCase cancelarAgendamentoUseCase
  ) {
    this.agendarConsultaUseCase = agendarConsultaUseCase;
    this.cancelarAgendamentoUseCase = cancelarAgendamentoUseCase;
  }

  @PostMapping
  public ResponseEntity<Consulta> agendar(@RequestBody AgendarConsultaDTO dto) throws Exception {
    return ResponseEntity.ok(agendarConsultaUseCase.execute(dto));
  }

  @PostMapping("/cancelar")
  public ResponseEntity<Consulta> cancelar(@RequestBody CancelarAgendamentoDTO dto) throws Exception {
    return ResponseEntity.ok(cancelarAgendamentoUseCase.execute(dto));
  }
}
