package com.simplehealth.estoque.application.service;

import com.simplehealth.estoque.domain.entity.Pedido;
import com.simplehealth.estoque.infrastructure.repositories.PedidoRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoService {

  private final PedidoRepository repository;

  public Pedido salvar(Pedido p) {
    return repository.save(p);
  }

  public Pedido buscarPorId(UUID id) {
    return repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado."));
  }

  public List<Pedido> listarTodos() {
    return repository.findAll();
  }

  public void deletar(UUID id) {
    repository.deleteById(id);
  }
}
