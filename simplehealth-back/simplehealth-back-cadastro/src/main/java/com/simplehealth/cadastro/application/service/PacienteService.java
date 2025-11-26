package com.simplehealth.cadastro.application.service;

import com.simplehealth.cadastro.application.exception.ResourceNotFoundException;
import com.simplehealth.cadastro.domain.entity.Paciente;
import com.simplehealth.cadastro.infrastructure.repositories.PacienteRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {

  private final PacienteRepository pacienteRepository;

  public PacienteService(PacienteRepository pacienteRepository) {
    this.pacienteRepository = pacienteRepository;
  }

  @Transactional
  public Paciente save(Paciente paciente) {
    if (pacienteRepository.existsByCpf(paciente.getCpf())) {
      throw new IllegalArgumentException("CPF já cadastrado");
    }
    return pacienteRepository.save(paciente);
  }

  @Transactional(readOnly = true)
  public Paciente findById(Long id) {
    return pacienteRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));
  }

  @Transactional(readOnly = true)
  public List<Paciente> findAll() {
    return pacienteRepository.findAll();
  }

  @Transactional
  public Paciente update(Long id, Paciente paciente) {
    Paciente existing = pacienteRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));
    existing.setNomeCompleto(paciente.getNomeCompleto());
    existing.setDataNascimento(paciente.getDataNascimento());
    if (!existing.getCpf().equals(paciente.getCpf()) && pacienteRepository.existsByCpf(paciente.getCpf())) {
      throw new IllegalArgumentException("CPF já cadastrado por outro paciente");
    }
    existing.setCpf(paciente.getCpf());
    existing.setTelefone(paciente.getTelefone());
    existing.setEmail(paciente.getEmail());
    return pacienteRepository.save(existing);
  }

  @Transactional
  public void delete(Long id) {
    if (!pacienteRepository.existsById(id)) {
      throw new ResourceNotFoundException("Paciente não encontrado");
    }
    pacienteRepository.deleteById(id);
  }

  public boolean existsByCpf(String cpf) {
    return pacienteRepository.existsByCpf(cpf);
  }
}
