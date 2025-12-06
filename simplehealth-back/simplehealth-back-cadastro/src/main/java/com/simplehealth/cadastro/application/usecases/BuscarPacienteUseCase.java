package com.simplehealth.cadastro.application.usecases;

import com.simplehealth.cadastro.application.dto.PacienteDTO;
import com.simplehealth.cadastro.application.service.PacienteService;
import com.simplehealth.cadastro.domain.entity.Paciente;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BuscarPacienteUseCase {

  private final PacienteService pacienteService;

  @Transactional(readOnly = true)
  public PacienteDTO buscarPorId(Long id) {
    Paciente paciente = pacienteService.findById(id);
    return convertToDTO(paciente);
  }

  @Transactional(readOnly = true)
  public List<PacienteDTO> listarTodos() {
    return pacienteService.findAll()
        .stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  private PacienteDTO convertToDTO(Paciente paciente) {
    return new PacienteDTO(
        paciente.getId(),
        paciente.getNomeCompleto(),
        paciente.getDataNascimento(),
        paciente.getCpf(),
        paciente.getTelefone(),
        paciente.getEmail(),
        paciente.getConvenio() != null ? paciente.getConvenio().getId() : null,
        paciente.getConvenio() != null ? paciente.getConvenio().getNome() : null);
  }
}
