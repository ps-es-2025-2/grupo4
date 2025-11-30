package com.simplehealth.estoque.application.service;

import com.simplehealth.estoque.domain.entity.Fornecedor;
import com.simplehealth.estoque.infrastructure.repositories.FornecedorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FornecedorService {

  private final FornecedorRepository repository;

  public Fornecedor salvar(Fornecedor f) {
    return repository.save(f);
  }

  public Fornecedor buscarPorId(Long id) {
    var item = repository.findById(id);
    if (item == null) {
      throw new IllegalArgumentException("Fornecedor não encontrado.");
    }
    return item;
  }

  public List<Fornecedor> listarTodos() {
    return repository.findAll();
  }

  public void deletar(Long id) {
    repository.deleteById(id);
  }
}
