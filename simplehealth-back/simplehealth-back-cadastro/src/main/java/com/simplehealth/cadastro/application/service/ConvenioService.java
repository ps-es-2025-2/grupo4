package com.simplehealth.cadastro.application.service;

import com.simplehealth.cadastro.application.exception.ResourceNotFoundException;
import com.simplehealth.cadastro.domain.entity.Convenio;
import com.simplehealth.cadastro.infrastructure.repositories.ConvenioRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConvenioService {

  private final ConvenioRepository convenioRepository;

  public ConvenioService(ConvenioRepository convenioRepository) {
    this.convenioRepository = convenioRepository;
  }

  @Transactional
  public Convenio create(Convenio convenio) {
    return convenioRepository.save(convenio);
  }

  @Transactional(readOnly = true)
  public Convenio findById(Long id) {
    return convenioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Convênio não encontrado"));
  }

  @Transactional(readOnly = true)
  public List<Convenio> findAll() {
    return convenioRepository.findAll();
  }

  @Transactional
  public Convenio update(Long id, Convenio convenio) {
    Convenio existing = convenioRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Convênio não encontrado"));
    existing.setNome(convenio.getNome());
    existing.setPlano(convenio.getPlano());
    existing.setAtivo(convenio.getAtivo());
    return convenioRepository.save(existing);
  }

  @Transactional
  public void delete(Long id) {
    if (!convenioRepository.existsById(id)) {
      throw new ResourceNotFoundException("Convênio não encontrado");
    }
    convenioRepository.deleteById(id);
  }
}
