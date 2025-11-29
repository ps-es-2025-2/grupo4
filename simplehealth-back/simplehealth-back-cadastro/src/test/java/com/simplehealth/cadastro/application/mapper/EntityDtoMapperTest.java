package com.simplehealth.cadastro.application.mapper;

import com.simplehealth.cadastro.application.dto.MedicoDTO;
import com.simplehealth.cadastro.application.dto.PacienteDTO;
import com.simplehealth.cadastro.application.dto.UsuarioDTO;
import com.simplehealth.cadastro.domain.entity.Medico;
import com.simplehealth.cadastro.domain.entity.Paciente;
import com.simplehealth.cadastro.domain.entity.Usuario;
import com.simplehealth.cadastro.domain.enums.EPerfilUsuario;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EntityDtoMapperTest {

  @Test
  void testMedicoToDto() {
    Medico medico = Medico.builder()
        .id(1L)
        .nomeCompleto("Dr. Carlos")
        .crm("123456")
        .especialidade("Cardiologia")
        .telefone("11999999999")
        .email("carlos@email.com")
        .build();

    MedicoDTO dto = EntityDtoMapper.toDto(medico);

    assertNotNull(dto);
    assertEquals(1L, dto.getId());
    assertEquals("Dr. Carlos", dto.getNomeCompleto());
    assertEquals("123456", dto.getCrm());
    assertEquals("Cardiologia", dto.getEspecialidade());
    assertEquals("11999999999", dto.getTelefone());
    assertEquals("carlos@email.com", dto.getEmail());
  }

  @Test
  void testMedicoToDto_Null() {
    MedicoDTO dto = EntityDtoMapper.toDto((Medico) null);
    assertNull(dto);
  }

  @Test
  void testMedicoDtoToEntity() {
    MedicoDTO dto = MedicoDTO.builder()
        .id(1L)
        .nomeCompleto("Dr. Carlos")
        .crm("123456")
        .especialidade("Cardiologia")
        .telefone("11999999999")
        .email("carlos@email.com")
        .build();

    Medico medico = EntityDtoMapper.toEntity(dto);

    assertNotNull(medico);
    assertEquals(1L, medico.getId());
    assertEquals("Dr. Carlos", medico.getNomeCompleto());
    assertEquals("123456", medico.getCrm());
    assertEquals("Cardiologia", medico.getEspecialidade());
    assertEquals("11999999999", medico.getTelefone());
    assertEquals("carlos@email.com", medico.getEmail());
  }

  @Test
  void testMedicoDtoToEntity_Null() {
    Medico medico = EntityDtoMapper.toEntity((MedicoDTO) null);
    assertNull(medico);
  }

  @Test
  void testPacienteToDto() {
    Paciente paciente = Paciente.builder()
        .id(1L)
        .nomeCompleto("João Silva")
        .dataNascimento(LocalDate.of(1990, 1, 1))
        .cpf("12345678901")
        .telefone("11988888888")
        .email("joao@email.com")
        .build();

    PacienteDTO dto = EntityDtoMapper.toDto(paciente);

    assertNotNull(dto);
    assertEquals(1L, dto.getId());
    assertEquals("João Silva", dto.getNomeCompleto());
    assertEquals(LocalDate.of(1990, 1, 1), dto.getDataNascimento());
    assertEquals("12345678901", dto.getCpf());
    assertEquals("11988888888", dto.getTelefone());
    assertEquals("joao@email.com", dto.getEmail());
  }

  @Test
  void testPacienteToDto_Null() {
    PacienteDTO dto = EntityDtoMapper.toDto((Paciente) null);
    assertNull(dto);
  }

  @Test
  void testPacienteDtoToEntity() {
    PacienteDTO dto = PacienteDTO.builder()
        .id(1L)
        .nomeCompleto("João Silva")
        .dataNascimento(LocalDate.of(1990, 1, 1))
        .cpf("12345678901")
        .telefone("11988888888")
        .email("joao@email.com")
        .build();

    Paciente paciente = EntityDtoMapper.toEntity(dto);

    assertNotNull(paciente);
    assertEquals(1L, paciente.getId());
    assertEquals("João Silva", paciente.getNomeCompleto());
    assertEquals(LocalDate.of(1990, 1, 1), paciente.getDataNascimento());
    assertEquals("12345678901", paciente.getCpf());
    assertEquals("11988888888", paciente.getTelefone());
    assertEquals("joao@email.com", paciente.getEmail());
  }

  @Test
  void testPacienteDtoToEntity_Null() {
    Paciente paciente = EntityDtoMapper.toEntity((PacienteDTO) null);
    assertNull(paciente);
  }

  @Test
  void testUsuarioToDto() {
    Usuario usuario = Usuario.builder()
        .id(1L)
        .nomeCompleto("Admin User")
        .login("admin")
        .senha("hashedPassword")
        .telefone("11977777777")
        .email("admin@email.com")
        .perfil(EPerfilUsuario.GESTOR)
        .build();

    UsuarioDTO dto = EntityDtoMapper.toDto(usuario);

    assertNotNull(dto);
    assertEquals(1L, dto.getId());
    assertEquals("Admin User", dto.getNomeCompleto());
    assertEquals("admin", dto.getLogin());
    assertEquals("hashedPassword", dto.getSenha());
    assertEquals("11977777777", dto.getTelefone());
    assertEquals("admin@email.com", dto.getEmail());
    assertEquals(EPerfilUsuario.GESTOR, dto.getPerfil());
  }

  @Test
  void testUsuarioToDto_Null() {
    UsuarioDTO dto = EntityDtoMapper.toDto((Usuario) null);
    assertNull(dto);
  }

  @Test
  void testUsuarioDtoToEntity() {
    UsuarioDTO dto = UsuarioDTO.builder()
        .id(1L)
        .nomeCompleto("Admin User")
        .login("admin")
        .senha("hashedPassword")
        .telefone("11977777777")
        .email("admin@email.com")
        .perfil(EPerfilUsuario.GESTOR)
        .build();

    Usuario usuario = EntityDtoMapper.toEntity(dto);

    assertNotNull(usuario);
    assertEquals(1L, usuario.getId());
    assertEquals("Admin User", usuario.getNomeCompleto());
    assertEquals("admin", usuario.getLogin());
    assertEquals("hashedPassword", usuario.getSenha());
    assertEquals("11977777777", usuario.getTelefone());
    assertEquals("admin@email.com", usuario.getEmail());
    assertEquals(EPerfilUsuario.GESTOR, usuario.getPerfil());
  }

  @Test
  void testUsuarioDtoToEntity_Null() {
    Usuario usuario = EntityDtoMapper.toEntity((UsuarioDTO) null);
    assertNull(usuario);
  }
}

