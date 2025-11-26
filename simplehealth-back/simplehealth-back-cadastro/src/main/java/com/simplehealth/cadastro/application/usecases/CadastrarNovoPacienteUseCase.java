package com.simplehealth.cadastro.application.usecases;


import com.simplehealth.cadastro.application.dto.PacienteDTO;
import com.simplehealth.cadastro.application.service.PacienteService;
import com.simplehealth.cadastro.domain.entity.Paciente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class CadastrarNovoPacienteUseCase {

  private final PacienteService pacienteService;

  @Transactional
  public PacienteDTO execute(PacienteDTO dto) throws Exception {
    if (pacienteService.existsByCpf(dto.getCpf())) {
      throw new Exception("CPF já cadastrado no sistema.");
    }

    Paciente paciente = new Paciente();
    paciente.setNomeCompleto(dto.getNomeCompleto());
    paciente.setCpf(dto.getCpf());
    paciente.setDataNascimento(dto.getDataNascimento());
    paciente.setTelefone(dto.getTelefone());
    paciente.setEmail(dto.getEmail());

    paciente = pacienteService.save(paciente);

    return new PacienteDTO(
        paciente.getId(),
        paciente.getNomeCompleto(),
        paciente.getDataNascimento(),
        paciente.getCpf(),
        paciente.getTelefone(),
        paciente.getEmail()
    );
  }
}
