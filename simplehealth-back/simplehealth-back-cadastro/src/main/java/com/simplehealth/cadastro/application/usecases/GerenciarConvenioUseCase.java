package com.simplehealth.cadastro.application.usecases;

import com.simplehealth.cadastro.application.dto.ConvenioDTO;
import com.simplehealth.cadastro.application.service.ConvenioService;
import com.simplehealth.cadastro.domain.entity.Convenio;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GerenciarConvenioUseCase {

  private final ConvenioService convenioService;

  @Transactional
  public ConvenioDTO criar(ConvenioDTO dto) {
    Convenio convenio = convertToEntity(dto);
    Convenio created = convenioService.create(convenio);
    return convertToDTO(created);
  }

  @Transactional
  public ConvenioDTO atualizar(Long id, ConvenioDTO dto) {
    Convenio convenio = convertToEntity(dto);
    Convenio updated = convenioService.update(id, convenio);
    return convertToDTO(updated);
  }

  @Transactional(readOnly = true)
  public ConvenioDTO buscarPorId(Long id) {
    Convenio convenio = convenioService.findById(id);
    return convertToDTO(convenio);
  }

  @Transactional(readOnly = true)
  public List<ConvenioDTO> listarTodos() {
    return convenioService.findAll()
        .stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<ConvenioDTO> listarAtivos() {
    return convenioService.findAll()
        .stream()
        .filter(c -> c.getAtivo() != null && c.getAtivo())
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  @Transactional
  public void deletar(Long id) {
    convenioService.delete(id);
  }

  private ConvenioDTO convertToDTO(Convenio convenio) {
    return ConvenioDTO.builder()
        .id(convenio.getId())
        .nome(convenio.getNome())
        .plano(convenio.getPlano())
        .ativo(convenio.getAtivo())
        .build();
  }

  private Convenio convertToEntity(ConvenioDTO dto) {
    return Convenio.builder()
        .id(dto.getId())
        .nome(dto.getNome())
        .plano(dto.getPlano())
        .ativo(dto.getAtivo())
        .build();
  }
}
