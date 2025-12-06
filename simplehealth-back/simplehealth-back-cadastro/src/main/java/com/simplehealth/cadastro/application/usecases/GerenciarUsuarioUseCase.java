package com.simplehealth.cadastro.application.usecases;

import com.simplehealth.cadastro.application.dto.UsuarioDTO;
import com.simplehealth.cadastro.application.service.UsuarioService;
import com.simplehealth.cadastro.domain.entity.Usuario;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GerenciarUsuarioUseCase {

  private final UsuarioService usuarioService;

  @Transactional
  public UsuarioDTO criar(UsuarioDTO dto) {
    Usuario usuario = convertToEntity(dto);
    Usuario created = usuarioService.create(usuario);
    return convertToDTO(created);
  }

  @Transactional
  public UsuarioDTO atualizar(Long id, UsuarioDTO dto) {
    Usuario usuario = convertToEntity(dto);
    Usuario updated = usuarioService.update(id, usuario);
    return convertToDTO(updated);
  }

  @Transactional(readOnly = true)
  public UsuarioDTO buscarPorId(Long id) {
    Usuario usuario = usuarioService.findById(id);
    return convertToDTO(usuario);
  }

  @Transactional(readOnly = true)
  public List<UsuarioDTO> listarTodos() {
    return usuarioService.findAll()
        .stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  @Transactional
  public void deletar(Long id) {
    usuarioService.delete(id);
  }

  private UsuarioDTO convertToDTO(Usuario usuario) {
    return UsuarioDTO.builder()
        .id(usuario.getId())
        .nomeCompleto(usuario.getNomeCompleto())
        .login(usuario.getLogin())
        .senha(usuario.getSenha())
        .telefone(usuario.getTelefone())
        .email(usuario.getEmail())
        .perfil(usuario.getPerfil())
        .build();
  }

  private Usuario convertToEntity(UsuarioDTO dto) {
    return Usuario.builder()
        .id(dto.getId())
        .nomeCompleto(dto.getNomeCompleto())
        .login(dto.getLogin())
        .senha(dto.getSenha())
        .telefone(dto.getTelefone())
        .email(dto.getEmail())
        .perfil(dto.getPerfil())
        .build();
  }
}
