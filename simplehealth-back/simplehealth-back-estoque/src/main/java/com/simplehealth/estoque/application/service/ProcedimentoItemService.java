package com.simplehealth.estoque.application.service;

import com.simplehealth.estoque.domain.entity.ProcedimentoItem;
import com.simplehealth.estoque.infrastructure.repositories.ProcedimentoItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcedimentoItemService {

  private final ProcedimentoItemRepository repository;

  public ProcedimentoItem salvar(ProcedimentoItem p) {
    return repository.save(p);
  }

  public ProcedimentoItem buscarPorId(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("ProcedimentoItem não encontrado."));
  }

  public List<ProcedimentoItem> listarTodos() {
    return repository.findAll();
  }

  public void deletar(Long id) {
    repository.deleteById(id);
  }
}
