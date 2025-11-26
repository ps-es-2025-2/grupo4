package com.simplehealth.cadastro.application.mapper;

import com.simplehealth.cadastro.application.dto.ConvenioDTO;
import com.simplehealth.cadastro.application.dto.MedicoDTO;
import com.simplehealth.cadastro.application.dto.PacienteDTO;
import com.simplehealth.cadastro.application.dto.UsuarioDTO;
import com.simplehealth.cadastro.domain.entity.Convenio;
import com.simplehealth.cadastro.domain.entity.Medico;
import com.simplehealth.cadastro.domain.entity.Paciente;
import com.simplehealth.cadastro.domain.entity.Usuario;

public class EntityDtoMapper {

  // Medico <-> MedicoDTO
  public static MedicoDTO toDto(Medico entity) {
    if (entity == null) {
      return null;
    }
    return MedicoDTO.builder()
        .id(entity.getId())
        .nomeCompleto(entity.getNomeCompleto())
        .crm(entity.getCrm())
        .especialidade(entity.getEspecialidade())
        .telefone(entity.getTelefone())
        .email(entity.getEmail())
        .build();
  }

  public static Medico toEntity(MedicoDTO dto) {
    if (dto == null) {
      return null;
    }
    Medico medico = new Medico();
    medico.setId(dto.getId());
    medico.setNomeCompleto(dto.getNomeCompleto());
    medico.setCrm(dto.getCrm());
    medico.setEspecialidade(dto.getEspecialidade());
    medico.setTelefone(dto.getTelefone());
    medico.setEmail(dto.getEmail());
    return medico;
  }

  // Paciente <-> PacienteDTO
  public static PacienteDTO toDto(Paciente entity) {
    if (entity == null) {
      return null;
    }
    return PacienteDTO.builder()
        .id(entity.getId())
        .nomeCompleto(entity.getNomeCompleto())
        .dataNascimento(entity.getDataNascimento())
        .cpf(entity.getCpf())
        .telefone(entity.getTelefone())
        .email(entity.getEmail())
        .build();
  }

  public static Paciente toEntity(PacienteDTO dto) {
    if (dto == null) {
      return null;
    }
    Paciente paciente = new Paciente();
    paciente.setId(dto.getId());
    paciente.setNomeCompleto(dto.getNomeCompleto());
    paciente.setDataNascimento(dto.getDataNascimento());
    paciente.setCpf(dto.getCpf());
    paciente.setTelefone(dto.getTelefone());
    paciente.setEmail(dto.getEmail());
    return paciente;
  }

  // Usuario <-> UsuarioDTO
  public static UsuarioDTO toDto(Usuario entity) {
    if (entity == null) {
      return null;
    }
    return UsuarioDTO.builder()
        .id(entity.getId())
        .nomeCompleto(entity.getNomeCompleto())
        .login(entity.getLogin())
        .senha(entity.getSenha())
        .telefone(entity.getTelefone())
        .email(entity.getEmail())
        .perfil(entity.getPerfil())
        .build();
  }

  public static Usuario toEntity(UsuarioDTO dto) {
    if (dto == null) {
      return null;
    }
    Usuario usuario = new Usuario();
    usuario.setId(dto.getId());
    usuario.setNomeCompleto(dto.getNomeCompleto());
    usuario.setLogin(dto.getLogin());
    usuario.setSenha(dto.getSenha());
    usuario.setTelefone(dto.getTelefone());
    usuario.setEmail(dto.getEmail());
    usuario.setPerfil(dto.getPerfil());
    return usuario;
  }

  // Convenio <-> ConvenioDTO
  public static ConvenioDTO toDto(Convenio entity) {
    if (entity == null) {
      return null;
    }

    return ConvenioDTO.builder()
        .id(entity.getId())
        .nome(entity.getNome())
        .plano(entity.getPlano())
        .ativo(entity.getAtivo())
        .build();
  }

  public static Convenio toEntity(ConvenioDTO dto) {
    if (dto == null) {
      return null;
    }

    return Convenio.builder()
        .id(dto.getId())
        .nome(dto.getNome())
        .plano(dto.getPlano())
        .ativo(dto.getAtivo())
        .build();
  }
}

