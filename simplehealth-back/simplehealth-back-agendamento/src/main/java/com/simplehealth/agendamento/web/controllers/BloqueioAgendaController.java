package com.simplehealth.agendamento.web.controllers;

import com.simplehealth.agendamento.application.dtos.AtualizarBloqueioAgendaDTO;
import com.simplehealth.agendamento.application.dtos.BloqueioAgendaDTO;
import com.simplehealth.agendamento.application.dtos.BloqueioAgendaResponseDTO;
import com.simplehealth.agendamento.application.usecases.AtualizarBloqueioAgendaUseCase;
import com.simplehealth.agendamento.application.usecases.BuscarBloqueioAgendaPorIdUseCase;
import com.simplehealth.agendamento.application.usecases.BuscarBloqueiosPorMedicoUseCase;
import com.simplehealth.agendamento.application.usecases.DeletarBloqueioAgendaUseCase;
import com.simplehealth.agendamento.application.usecases.DesativarBloqueioAgendaUseCase;
import com.simplehealth.agendamento.application.usecases.ListarBloqueiosAgendaUseCase;
import com.simplehealth.agendamento.application.usecases.RegistrarBloqueioAgendaUseCase;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bloqueio-agenda")
public class BloqueioAgendaController {

  private final RegistrarBloqueioAgendaUseCase registrarBloqueioAgendaUseCase;
  private final BuscarBloqueioAgendaPorIdUseCase buscarBloqueioAgendaPorIdUseCase;
  private final ListarBloqueiosAgendaUseCase listarBloqueiosAgendaUseCase;
  private final BuscarBloqueiosPorMedicoUseCase buscarBloqueiosPorMedicoUseCase;
  private final AtualizarBloqueioAgendaUseCase atualizarBloqueioAgendaUseCase;
  private final DesativarBloqueioAgendaUseCase desativarBloqueioAgendaUseCase;
  private final DeletarBloqueioAgendaUseCase deletarBloqueioAgendaUseCase;

  public BloqueioAgendaController(
      RegistrarBloqueioAgendaUseCase registrarBloqueioAgendaUseCase,
      BuscarBloqueioAgendaPorIdUseCase buscarBloqueioAgendaPorIdUseCase,
      ListarBloqueiosAgendaUseCase listarBloqueiosAgendaUseCase,
      BuscarBloqueiosPorMedicoUseCase buscarBloqueiosPorMedicoUseCase,
      AtualizarBloqueioAgendaUseCase atualizarBloqueioAgendaUseCase,
      DesativarBloqueioAgendaUseCase desativarBloqueioAgendaUseCase,
      DeletarBloqueioAgendaUseCase deletarBloqueioAgendaUseCase) {
    this.registrarBloqueioAgendaUseCase = registrarBloqueioAgendaUseCase;
    this.buscarBloqueioAgendaPorIdUseCase = buscarBloqueioAgendaPorIdUseCase;
    this.listarBloqueiosAgendaUseCase = listarBloqueiosAgendaUseCase;
    this.buscarBloqueiosPorMedicoUseCase = buscarBloqueiosPorMedicoUseCase;
    this.atualizarBloqueioAgendaUseCase = atualizarBloqueioAgendaUseCase;
    this.desativarBloqueioAgendaUseCase = desativarBloqueioAgendaUseCase;
    this.deletarBloqueioAgendaUseCase = deletarBloqueioAgendaUseCase;
  }

  @PostMapping
  public ResponseEntity<BloqueioAgendaResponseDTO> criar(@RequestBody BloqueioAgendaDTO dto) throws Exception {
    return ResponseEntity.ok(registrarBloqueioAgendaUseCase.registrar(dto));
  }

  @GetMapping
  public ResponseEntity<List<BloqueioAgendaResponseDTO>> listarTodos() {
    return ResponseEntity.ok(listarBloqueiosAgendaUseCase.execute());
  }

  @GetMapping("/{id}")
  public ResponseEntity<BloqueioAgendaResponseDTO> buscarPorId(@PathVariable String id) {
    return ResponseEntity.ok(buscarBloqueioAgendaPorIdUseCase.execute(id));
  }

  @GetMapping("/medico/{medicoCrm}")
  public ResponseEntity<List<BloqueioAgendaResponseDTO>> buscarPorMedico(@PathVariable String medicoCrm) {
    return ResponseEntity.ok(buscarBloqueiosPorMedicoUseCase.execute(medicoCrm));
  }

  @PutMapping("/{id}")
  public ResponseEntity<BloqueioAgendaResponseDTO> atualizar(
      @PathVariable String id,
      @RequestBody AtualizarBloqueioAgendaDTO dto) throws Exception {
    dto.setId(id);
    return ResponseEntity.ok(atualizarBloqueioAgendaUseCase.execute(dto));
  }

  @PatchMapping("/{id}/desativar")
  public ResponseEntity<BloqueioAgendaResponseDTO> desativar(@PathVariable String id) {
    return ResponseEntity.ok(desativarBloqueioAgendaUseCase.execute(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletar(@PathVariable String id) {
    deletarBloqueioAgendaUseCase.execute(id);
    return ResponseEntity.noContent().build();
  }
}
