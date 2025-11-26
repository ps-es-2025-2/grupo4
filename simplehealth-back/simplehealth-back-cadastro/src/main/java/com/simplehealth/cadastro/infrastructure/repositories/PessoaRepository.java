package com.simplehealth.cadastro.infrastructure.repositories;

import com.simplehealth.cadastro.domain.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}