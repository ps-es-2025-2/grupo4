package com.simplehealth.cadastro.infrastructure.repositories;

import com.simplehealth.cadastro.domain.entity.Medico;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

  Optional<Medico> findByCrm(String crm);

  boolean existsByCrm(String crm);
}
