package com.grupo4.SimpleHealth.service;

import com.grupo4.SimpleHealth.entity.Pedido;
import com.grupo4.SimpleHealth.repository.PedidoRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoService {

  private final PedidoRepository pedidoRepository;

  public List<Pedido> listar() {
    return pedidoRepository.findAll();
  }

  public Optional<Pedido> buscarPorId(Long id) {
    return pedidoRepository.findById(id);
  }

  public Pedido salvar(Pedido pedido) {
    return pedidoRepository.save(pedido);
  }

  public Pedido atualizar(Long id, Pedido pedido) {
    return pedidoRepository.findById(id)
        .map(p -> {
          pedido.setIdPedido(id);
          return pedidoRepository.save(pedido);
        })
        .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
  }

  public void deletar(Long id) {
    pedidoRepository.deleteById(id);
  }
}
