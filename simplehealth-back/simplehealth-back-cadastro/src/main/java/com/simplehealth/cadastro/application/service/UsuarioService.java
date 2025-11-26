package com.simplehealth.cadastro.application.service;

import com.simplehealth.cadastro.application.exception.ResourceNotFoundException;
import com.simplehealth.cadastro.domain.entity.Usuario;
import com.simplehealth.cadastro.infrastructure.repositories.UsuarioRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

  private final UsuarioRepository repo;
  private final PasswordEncoder passwordEncoder;

  public UsuarioService(UsuarioRepository repo, PasswordEncoder passwordEncoder) {
    this.repo = repo;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public Usuario create(Usuario usuario) {
    if (repo.existsByLogin(usuario.getLogin())) {
      throw new IllegalArgumentException("Login já existe");
    }
    usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
    return repo.save(usuario);
  }

  @Transactional(readOnly = true)
  public Usuario findById(Long id) {
    return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
  }

  @Transactional(readOnly = true)
  public List<Usuario> findAll() {
    return repo.findAll();
  }

  @Transactional
  public Usuario update(Long id, Usuario usuario) {
    Usuario existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    existing.setNomeCompleto(usuario.getNomeCompleto());
    if (!existing.getLogin().equals(usuario.getLogin()) && repo.existsByLogin(usuario.getLogin())) {
      throw new IllegalArgumentException("Login já existe");
    }
    existing.setLogin(usuario.getLogin());
    if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
      existing.setSenha(passwordEncoder.encode(usuario.getSenha()));
    }
    existing.setPerfil(usuario.getPerfil());
    existing.setTelefone(usuario.getTelefone());
    existing.setEmail(usuario.getEmail());
    return repo.save(existing);
  }

  @Transactional
  public void delete(Long id) {
    if (!repo.existsById(id)) {
      throw new ResourceNotFoundException("Usuário não encontrado");
    }
    repo.deleteById(id);
  }
}
