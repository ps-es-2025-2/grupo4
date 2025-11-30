package com.simplehealth.estoque.application.service;

import com.simplehealth.estoque.domain.entity.Pedido;
import com.simplehealth.estoque.infrastructure.repositories.PedidoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoService {

  private final PedidoRepository repository;

  public Pedido salvar(Pedido p) {
    return repository.save(p);
  }

  public Pedido buscarPorId(Long id) {
    var item = repository.findById(id);
    if (item == null) {
      throw new IllegalArgumentException("Pedido não encontrado.");
    }
    return item;
  }

  public List<Pedido> listarTodos() {
    return repository.findAll();
  }

  public void deletar(Long id) {
    repository.deleteById(id);
  }
}
