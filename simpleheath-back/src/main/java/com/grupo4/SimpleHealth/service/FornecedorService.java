package com.grupo4.SimpleHealth.service;

import com.grupo4.SimpleHealth.entity.Fornecedor;
import com.grupo4.SimpleHealth.repository.FornecedorRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FornecedorService {

  private final FornecedorRepository fornecedorRepository;

  public List<Fornecedor> listar() {
    return fornecedorRepository.findAll();
  }

  public Optional<Fornecedor> buscarPorId(Long id) {
    return fornecedorRepository.findById(id);
  }

  public Fornecedor salvar(Fornecedor fornecedor) {
    return fornecedorRepository.save(fornecedor);
  }

  public Fornecedor atualizar(Long id, Fornecedor fornecedor) {
    return fornecedorRepository.findById(id)
        .map(f -> {
          fornecedor.setIdFornecedor(id);
          return fornecedorRepository.save(fornecedor);
        })
        .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
  }

  public void deletar(Long id) {
    fornecedorRepository.deleteById(id);
  }
}
