package com.simplehealth.cadastro.application.service;

import com.simplehealth.cadastro.application.exception.ResourceNotFoundException;
import com.simplehealth.cadastro.domain.entity.Medico;
import com.simplehealth.cadastro.infrastructure.repositories.MedicoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicoService {

  private final MedicoRepository repo;

  public MedicoService(MedicoRepository repo) {
    this.repo = repo;
  }

  @Transactional
  public Medico create(Medico medico) {
    if (repo.existsByCrm(medico.getCrm())) {
      throw new IllegalArgumentException("CRM já cadastrado");
    }
    return repo.save(medico);
  }

  @Transactional(readOnly = true)
  public Medico findById(Long id) {
    return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"));
  }

  @Transactional(readOnly = true)
  public List<Medico> findAll() {
    return repo.findAll();
  }

  @Transactional
  public Medico update(Long id, Medico medico) {
    Medico existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"));
    if (!existing.getCrm().equals(medico.getCrm()) && repo.existsByCrm(medico.getCrm())) {
      throw new IllegalArgumentException("CRM já existe para outro médico");
    }
    existing.setNomeCompleto(medico.getNomeCompleto());
    existing.setCrm(medico.getCrm());
    existing.setEspecialidade(medico.getEspecialidade());
    existing.setTelefone(medico.getTelefone());
    existing.setEmail(medico.getEmail());
    return repo.save(existing);
  }

  @Transactional
  public void delete(Long id) {
    if (!repo.existsById(id)) {
      throw new ResourceNotFoundException("Médico não encontrado");
    }
    repo.deleteById(id);
  }
}
