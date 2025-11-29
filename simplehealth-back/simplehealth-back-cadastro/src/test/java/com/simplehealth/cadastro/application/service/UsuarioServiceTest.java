package com.simplehealth.cadastro.application.service;

import com.simplehealth.cadastro.application.exception.ResourceNotFoundException;
import com.simplehealth.cadastro.domain.entity.Usuario;
import com.simplehealth.cadastro.domain.enums.EPerfilUsuario;
import com.simplehealth.cadastro.infrastructure.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

  @Mock
  private UsuarioRepository usuarioRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UsuarioService usuarioService;

  private Usuario usuario;

  @BeforeEach
  void setUp() {
    usuario = Usuario.builder()
        .id(1L)
        .nomeCompleto("Admin User")
        .login("admin")
        .senha("senha123")
        .perfil(EPerfilUsuario.GESTOR)
        .telefone("11955555555")
        .email("admin@email.com")
        .build();
  }

  @Test
  void testCreate_Success() {
    when(usuarioRepository.existsByLogin(anyString())).thenReturn(false);
    when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
    when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

    Usuario created = usuarioService.create(usuario);

    assertNotNull(created);
    assertEquals("Admin User", created.getNomeCompleto());
    verify(usuarioRepository, times(1)).existsByLogin(anyString());
    verify(passwordEncoder, times(1)).encode(anyString());
    verify(usuarioRepository, times(1)).save(any(Usuario.class));
  }

  @Test
  void testCreate_LoginJaExiste() {
    when(usuarioRepository.existsByLogin(anyString())).thenReturn(true);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      usuarioService.create(usuario);
    });

    assertEquals("Login já existe", exception.getMessage());
    verify(usuarioRepository, times(1)).existsByLogin(anyString());
    verify(passwordEncoder, never()).encode(anyString());
    verify(usuarioRepository, never()).save(any(Usuario.class));
  }

  @Test
  void testFindById_Success() {
    when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));

    Usuario found = usuarioService.findById(1L);

    assertNotNull(found);
    assertEquals(1L, found.getId());
    assertEquals("Admin User", found.getNomeCompleto());
    verify(usuarioRepository, times(1)).findById(anyLong());
  }

  @Test
  void testFindById_NotFound() {
    when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
      usuarioService.findById(1L);
    });

    assertEquals("Usuário não encontrado", exception.getMessage());
    verify(usuarioRepository, times(1)).findById(anyLong());
  }

  @Test
  void testFindAll() {
    Usuario usuario2 = Usuario.builder()
        .id(2L)
        .nomeCompleto("Secretaria User")
        .login("secretaria")
        .perfil(EPerfilUsuario.SECRETARIA)
        .build();

    when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario, usuario2));

    List<Usuario> usuarios = usuarioService.findAll();

    assertNotNull(usuarios);
    assertEquals(2, usuarios.size());
    verify(usuarioRepository, times(1)).findAll();
  }

  @Test
  void testUpdate_Success() {
    Usuario updatedData = Usuario.builder()
        .nomeCompleto("Admin User Atualizado")
        .login("admin")
        .senha("novaSenha123")
        .perfil(EPerfilUsuario.GESTOR)
        .telefone("11944444444")
        .email("admin.novo@email.com")
        .build();

    when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
    when(passwordEncoder.encode(anyString())).thenReturn("hashedNewPassword");
    when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

    Usuario updated = usuarioService.update(1L, updatedData);

    assertNotNull(updated);
    verify(usuarioRepository, times(1)).findById(anyLong());
    verify(passwordEncoder, times(1)).encode(anyString());
    verify(usuarioRepository, times(1)).save(any(Usuario.class));
  }

  @Test
  void testUpdate_SemAlterarSenha() {
    Usuario updatedData = Usuario.builder()
        .nomeCompleto("Admin User Atualizado")
        .login("admin")
        .senha("")
        .perfil(EPerfilUsuario.GESTOR)
        .telefone("11944444444")
        .email("admin.novo@email.com")
        .build();

    when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
    when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

    Usuario updated = usuarioService.update(1L, updatedData);

    assertNotNull(updated);
    verify(usuarioRepository, times(1)).findById(anyLong());
    verify(passwordEncoder, never()).encode(anyString());
    verify(usuarioRepository, times(1)).save(any(Usuario.class));
  }

  @Test
  void testUpdate_NotFound() {
    when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
      usuarioService.update(1L, usuario);
    });

    assertEquals("Usuário não encontrado", exception.getMessage());
    verify(usuarioRepository, times(1)).findById(anyLong());
    verify(usuarioRepository, never()).save(any(Usuario.class));
  }

  @Test
  void testUpdate_LoginJaExiste() {
    Usuario updatedData = Usuario.builder()
        .nomeCompleto("Admin User")
        .login("outro_login")
        .senha("senha123")
        .perfil(EPerfilUsuario.GESTOR)
        .build();

    when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
    when(usuarioRepository.existsByLogin("outro_login")).thenReturn(true);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      usuarioService.update(1L, updatedData);
    });

    assertEquals("Login já existe", exception.getMessage());
    verify(usuarioRepository, times(1)).findById(anyLong());
    verify(usuarioRepository, never()).save(any(Usuario.class));
  }

  @Test
  void testDelete_Success() {
    when(usuarioRepository.existsById(anyLong())).thenReturn(true);
    doNothing().when(usuarioRepository).deleteById(anyLong());

    usuarioService.delete(1L);

    verify(usuarioRepository, times(1)).existsById(anyLong());
    verify(usuarioRepository, times(1)).deleteById(anyLong());
  }

  @Test
  void testDelete_NotFound() {
    when(usuarioRepository.existsById(anyLong())).thenReturn(false);

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
      usuarioService.delete(1L);
    });

    assertEquals("Usuário não encontrado", exception.getMessage());
    verify(usuarioRepository, times(1)).existsById(anyLong());
    verify(usuarioRepository, never()).deleteById(anyLong());
  }
}

