package com.simplehealth.estoque.application.service;

import com.simplehealth.estoque.domain.entity.Medicamento;
import com.simplehealth.estoque.infrastructure.repositories.MedicamentoRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicamentoService {

  private final MedicamentoRepository repository;

  public Medicamento salvar(Medicamento m) {
    return repository.save(m);
  }

  public Medicamento buscarPorId(UUID id) {
    return repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Medicamento não encontrado."));
  }

  public List<Medicamento> listarTodos() {
    return repository.findAll();
  }

  public void deletar(UUID id) {
    repository.deleteById(id);
  }
}
