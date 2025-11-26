package com.simplehealth.cadastro.infrastructure.repositories;

import com.simplehealth.cadastro.domain.entity.Paciente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

  Optional<Paciente> findByCpf(String cpf);

  boolean existsByCpf(String cpf);
}
