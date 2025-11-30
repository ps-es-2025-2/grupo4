package com.simplehealth.estoque.application.service;

import com.simplehealth.estoque.domain.entity.Alimento;
import com.simplehealth.estoque.infrastructure.repositories.AlimentoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlimentoService {

  private final AlimentoRepository repository;

  public Alimento salvar(Alimento a) {
    return repository.save(a);
  }

  public Alimento buscarPorId(Long id) {
    var item = repository.findById(id);
    if (item == null) {
      throw new IllegalArgumentException("Item não encontrado no estoque.");
    }
    return item;
  }

//  public List<Alimento> listarTodos() {
//    return repository.findAll();
//  }
//
//  public void deletar(Long id) {
//    repository.deleteById(id);
//  }
}
