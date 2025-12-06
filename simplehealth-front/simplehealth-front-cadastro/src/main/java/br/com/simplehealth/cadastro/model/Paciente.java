package br.com.simplehealth.cadastro.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

/**
 * Modelo de dados para Paciente.
 */
public class Paciente {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("nomeCompleto")
    private String nomeCompleto;

    @JsonProperty("dataNascimento")
    private LocalDate dataNascimento;

    @JsonProperty("cpf")
    private String cpf;

    @JsonProperty("telefone")
    private String telefone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("convenioId")
    private Long convenioId;
    
    @JsonProperty("convenioNome")
    private String convenioNome;
    
    // Objeto convenio temporário para uso na interface (não enviado para API)
    private transient Convenio convenio;

    public Paciente() {
    }

    public Paciente(Long id, String nomeCompleto, LocalDate dataNascimento, String cpf, String telefone, String email) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
    }

    public Paciente(Long id, String nomeCompleto, LocalDate dataNascimento, String cpf, String telefone, String email, Long convenioId, String convenioNome) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.convenioId = convenioId;
        this.convenioNome = convenioNome;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getConvenioId() {
        return convenioId;
    }

    public void setConvenioId(Long convenioId) {
        this.convenioId = convenioId;
    }
    
    public String getConvenioNome() {
        return convenioNome;
    }

    public void setConvenioNome(String convenioNome) {
        this.convenioNome = convenioNome;
    }
    
    public Convenio getConvenio() {
        return convenio;
    }

    public void setConvenio(Convenio convenio) {
        this.convenio = convenio;
        if (convenio != null) {
            this.convenioId = convenio.getId();
            this.convenioNome = convenio.getNome();
        }
    }

    @Override
    public String toString() {
        return nomeCompleto + " - CPF: " + cpf;
    }
}
