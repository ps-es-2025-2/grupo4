package com.simplehealth.cadastro.application.service;

import com.simplehealth.cadastro.domain.entity.Pessoa;
import com.simplehealth.cadastro.infrastructure.repositories.PessoaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

  @Autowired
  private PessoaRepository repo;

  public Pessoa salvar(Pessoa pessoa) {
    return repo.save(pessoa);
  }

  public Pessoa buscarPorId(Long id) {
    return repo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));
  }
}
