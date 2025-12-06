package com.simplehealth.cadastro.application.usecases;

import com.simplehealth.cadastro.application.dto.MedicoDTO;
import com.simplehealth.cadastro.application.service.MedicoService;
import com.simplehealth.cadastro.domain.entity.Medico;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GerenciarMedicoUseCase {

  private final MedicoService medicoService;

  @Transactional
  public MedicoDTO criar(MedicoDTO dto) {
    Medico medico = convertToEntity(dto);
    Medico created = medicoService.create(medico);
    return convertToDTO(created);
  }

  @Transactional
  public MedicoDTO atualizar(Long id, MedicoDTO dto) {
    Medico medico = convertToEntity(dto);
    Medico updated = medicoService.update(id, medico);
    return convertToDTO(updated);
  }

  @Transactional(readOnly = true)
  public MedicoDTO buscarPorId(Long id) {
    Medico medico = medicoService.findById(id);
    return convertToDTO(medico);
  }

  @Transactional(readOnly = true)
  public List<MedicoDTO> listarTodos() {
    return medicoService.findAll()
        .stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  @Transactional
  public void deletar(Long id) {
    medicoService.delete(id);
  }

  private MedicoDTO convertToDTO(Medico medico) {
    return MedicoDTO.builder()
        .id(medico.getId())
        .nomeCompleto(medico.getNomeCompleto())
        .crm(medico.getCrm())
        .especialidade(medico.getEspecialidade())
        .telefone(medico.getTelefone())
        .email(medico.getEmail())
        .build();
  }

  private Medico convertToEntity(MedicoDTO dto) {
    return Medico.builder()
        .id(dto.getId())
        .nomeCompleto(dto.getNomeCompleto())
        .crm(dto.getCrm())
        .especialidade(dto.getEspecialidade())
        .telefone(dto.getTelefone())
        .email(dto.getEmail())
        .build();
  }
}
