package com.simplehealth.estoque.application.service;

import com.simplehealth.estoque.domain.entity.Hospitalar;
import com.simplehealth.estoque.infrastructure.repositories.HospitalarRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HospitalarService {

  private final HospitalarRepository repository;

  public Hospitalar salvar(Hospitalar h) {
    return repository.save(h);
  }

  public Hospitalar buscarPorId(Long id) {
    var item = repository.findById(id);
    if (item == null) {
      throw new IllegalArgumentException("Item hospitalar não encontrado.");
    }
    return item;
  }

  public List<Hospitalar> listarTodos() {
    return repository.findAll();
  }

  public void deletar(Long id) {
    repository.deleteById(id);
  }
}
