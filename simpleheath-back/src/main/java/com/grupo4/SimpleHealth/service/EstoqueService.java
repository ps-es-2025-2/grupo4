package com.grupo4.SimpleHealth.service;

import com.grupo4.SimpleHealth.entity.Estoque;
import com.grupo4.SimpleHealth.repository.EstoqueRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstoqueService {

  private final EstoqueRepository estoqueRepository;

  public List<Estoque> listar() {
    return estoqueRepository.findAll();
  }

  public Optional<Estoque> buscarPorId(Long id) {
    return estoqueRepository.findById(id);
  }

  public Estoque salvar(Estoque estoque) {
    return estoqueRepository.save(estoque);
  }

  public Estoque atualizar(Long id, Estoque estoque) {
    return estoqueRepository.findById(id)
        .map(e -> {
          estoque.setIdEstoque(id);
          return estoqueRepository.save(estoque);
        })
        .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));
  }

  public void deletar(Long id) {
    estoqueRepository.deleteById(id);
  }
}
