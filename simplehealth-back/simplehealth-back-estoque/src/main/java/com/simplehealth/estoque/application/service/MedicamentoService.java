package com.simplehealth.estoque.application.service;

import com.simplehealth.estoque.domain.entity.Medicamento;
import com.simplehealth.estoque.infrastructure.repositories.MedicamentoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicamentoService {

  private final MedicamentoRepository repository;

  public Medicamento salvar(Medicamento m) {
    return repository.save(m);
  }

  public Medicamento buscarPorId(Long id) {
    var item = repository.findById(id);
    if (item == null) {
      throw new IllegalArgumentException("Medicamento não encontrado.");
    }
    return item;
  }

  public List<Medicamento> listarTodos() {
    return repository.findAll();
  }

  public void deletar(Long id) {
    repository.deleteById(id);
  }
}
