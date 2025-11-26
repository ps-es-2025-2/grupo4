package com.simplehealth.cadastro.infrastructure.repositories;

import com.simplehealth.cadastro.domain.entity.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

  Optional<Usuario> findByLogin(String login);

  boolean existsByLogin(String login);
}
