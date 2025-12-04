package com.simplehealth.estoque.application.service;

import com.simplehealth.estoque.domain.entity.Alimento;
import com.simplehealth.estoque.infrastructure.repositories.AlimentoRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlimentoService {

  private final AlimentoRepository repository;

  public Alimento salvar(Alimento a) {
    return repository.save(a);
  }

  public Alimento buscarPorId(UUID id) {
    return repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Item não encontrado no estoque."));
  }

  public List<Alimento> listarTodos() {
    return repository.findAll();
  }

  public void deletar(UUID id) {
    repository.deleteById(id);
  }
}

