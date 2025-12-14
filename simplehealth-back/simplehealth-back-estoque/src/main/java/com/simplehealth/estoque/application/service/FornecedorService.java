package com.simplehealth.estoque.application.service;

import com.simplehealth.estoque.domain.entity.Fornecedor;
import com.simplehealth.estoque.infrastructure.repositories.FornecedorRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FornecedorService {

  private final FornecedorRepository repository;

  public Fornecedor salvar(Fornecedor f) {
    return repository.save(f);
  }

  public Fornecedor buscarPorId(UUID id) {
    return repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrado."));
  }

  public List<Fornecedor> listarTodos() {
    return repository.findAll();
  }

  public void deletar(UUID id) {
    repository.deleteById(id);
  }

  public List<Fornecedor> buscarPorNome(String nome) {
    return repository.findAll().stream()
        .filter(f -> f.getNome() != null &&
            f.getNome().toLowerCase().contains(nome.toLowerCase()))
        .toList();
  }
}
