package com.simplehealth.cadastro.application.usecases;

import com.simplehealth.cadastro.application.dto.PacienteDTO;
import com.simplehealth.cadastro.application.service.ConvenioService;
import com.simplehealth.cadastro.application.service.PacienteService;
import com.simplehealth.cadastro.domain.entity.Convenio;
import com.simplehealth.cadastro.domain.entity.Paciente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AtualizarPacienteUseCase {

  private final PacienteService pacienteService;
  private final ConvenioService convenioService;

  @Transactional
  public PacienteDTO execute(Long id, PacienteDTO dto) throws Exception {
    Paciente paciente = pacienteService.findById(id);

    if (!paciente.getCpf().equals(dto.getCpf()) && pacienteService.existsByCpf(dto.getCpf())) {
      throw new Exception("CPF já cadastrado por outro paciente.");
    }

    paciente.setNomeCompleto(dto.getNomeCompleto());
    paciente.setCpf(dto.getCpf());
    paciente.setDataNascimento(dto.getDataNascimento());
    paciente.setTelefone(dto.getTelefone());
    paciente.setEmail(dto.getEmail());

    if (dto.getConvenioId() != null) {
      Convenio convenio = convenioService.findById(dto.getConvenioId());

      if (convenio.getAtivo() == null || !convenio.getAtivo()) {
        throw new Exception("Convênio inativo não pode ser associado a pacientes.");
      }

      paciente.setConvenio(convenio);
    } else {
      paciente.setConvenio(null);
    }

    paciente = pacienteService.update(id, paciente);

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
