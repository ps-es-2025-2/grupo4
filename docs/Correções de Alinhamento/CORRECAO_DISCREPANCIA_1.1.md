# Correção da Discrepância 1.1 - Hierarquia Médico vs Usuario

**Data:** 14/12/2025  
**Responsável:** Equipe de Documentação  
**Status:** ✅ Concluída

---

## 1. Discrepância Identificada

### Problema
A documentação anterior mostrava **`Medico extends Usuario`**, mas a implementação real no backend possui:
- `Medico extends Pessoa`
- `Usuario extends Pessoa`

Ou seja, **Medico e Usuario são classes irmãs independentes**, ambas herdando de `Pessoa`.

---

## 2. Análise de Impactos (Backend)

### Implementação Atual
```java
@MappedSuperclass
public abstract class Pessoa {
    private Long id;
    private String nomeCompleto;
    private String telefone;
    private String email;
}

@Entity
public class Medico extends Pessoa {
    private String crm;
    private String especialidade;
}

@Entity
public class Usuario extends Pessoa {
    private String login;
    private String senha;
    private EPerfilUsuario perfil;
}
```

### Impacto se Alterássemos para Medico extends Usuario

**ALTO IMPACTO** - Não recomendado:

1. **Modelo de Dados**
   - Alteração completa da estrutura de herança no PostgreSQL
   - Mudança de estratégia de herança (JOINED, SINGLE_TABLE ou TABLE_PER_CLASS)
   - Possível necessidade de migração de dados

2. **Camada de Domínio**
   - Refatoração de `Medico.java`
   - Todo médico teria obrigatoriamente `login` e `senha` (conceito incorreto)
   - Quebra de encapsulamento e responsabilidades

3. **Camada de Aplicação**
   - Alteração em `MedicoService`, `MedicoController`
   - Mudança em `MedicoDTO` e mapeamentos
   - Refatoração de `GerenciarMedicoUseCase`

4. **Infraestrutura**
   - Alteração em `MedicoRepository`
   - Queries e relacionamentos afetados

5. **Testes**
   - Quebra de todos os testes unitários e de integração de Medico
   - Necessidade de reescrever casos de teste

6. **Integração**
   - Risco de quebra de integração com módulo de **Agendamento**
   - API REST pode precisar de versionamento

### Complexidade Estimada
- **Esforço:** ~40-60 horas de desenvolvimento
- **Risco:** Alto (regressão em funcionalidades críticas)
- **Testes:** ~20 horas adicionais

---

## 3. Decisão Técnica

### ✅ Decisão: Manter Arquitetura Atual

**Justificativa:**
1. A arquitetura atual está **correta conceitualmente**
2. Um médico pode existir no cadastro sem ter acesso ao sistema
3. A separação de responsabilidades está adequada
4. Impacto da mudança é desproporcional ao benefício

**Ação:** Corrigir a documentação para refletir a implementação real.

---

## 4. Arquivos Corrigidos

### 4.1. Diagramas de Classes de Análise
**Arquivo:** `docs/documentos-finais-definitivos/3.4. Classes de Análise/3.4. Classes de Análise_Diagrama de Classes.md`

**Alterações:**
- ✅ Adicionada nota técnica no início do documento
- ✅ Corrigido diagrama completo: `class Medico extends Pessoa`
- ✅ Corrigido diagrama do módulo de cadastro: `class Medico extends Pessoa`

### 4.2. Modelagem de Classes de Projeto
**Arquivo:** `docs/documentos-finais-definitivos/3.7_3.9_3.10_Modelagens/3.7. Modelagem de Classes de Projeto/3.7. Modelagem de Classes de Projeto.md`

**Alterações:**
- ✅ Adicionada nota técnica no início do documento
- ✅ Diagrama já estava correto mostrando `Pessoa <|-- Medico`

### 4.3. Casos de Uso
**Arquivo:** `docs/documentos-finais-definitivos/3.2_3.3_Casos de uso/3.3. Descrição detalhada de cada Caso de Uso/3.3. Descrição detalhada de cada Caso de Uso.md`

**Alterações:**
- ✅ Atualizada pós-condição do UC04 (Gerenciar Usuários)
- ✅ Clarificado que Medico e Usuario são registros separados

### 4.4. Boas Práticas
**Arquivo:** `docs/documentos-finais-definitivos/3.8 Boas Práticas/3.8 Boas praticas.md`

**Alterações:**
- ✅ Corrigida descrição do Strategy Pattern
- ✅ Adicionada nota explicativa sobre a arquitetura

### 4.5. Arquitetura do Sistema
**Arquivo:** `docs/documentos-finais-definitivos/3.6. Arquitetura do Sistema - Lógica e Física/3.6. Arquitetura do Sistema - Lógica e Física.md`

**Alterações:**
- ✅ Adicionada nota sobre o modelo de dados no módulo de Cadastro

---

## 5. Validação

### 5.1. Código Backend Revisado
- ✅ `Medico.java` - confirma `extends Pessoa`
- ✅ `Usuario.java` - confirma `extends Pessoa`
- ✅ `Pessoa.java` - confirma classe abstrata base
- ✅ `MedicoRepository.java` - sem relação com Usuario
- ✅ `MedicoService.java` - sem dependência de Usuario

### 5.2. Documentação Alinhada
- ✅ Todos os diagramas PlantUML corrigidos
- ✅ Notas técnicas adicionadas
- ✅ Análise de impactos documentada
- ✅ Decisão justificada

---

## 6. Próximos Passos

### Recomendações
1. ✅ Documentação corrigida - **Concluído**
2. ⚠️ Atualizar diagramas visuais (imagens PNG/SVG) - **Pendente**
3. ⚠️ Revisar outros módulos (Agendamento, Estoque) - **Recomendado**
4. ✅ Manter consistência em futuras atualizações

### Outros Pontos de Atenção
- Verificar se há outras discrepâncias similares em outros módulos
- Garantir que novos desenvolvedores entendam a arquitetura correta
- Considerar adicionar testes de arquitetura (ArchUnit) para prevenir desvios

---

## 7. Referências

### Arquivos Relacionados
- `simplehealth-back/simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/domain/entity/Pessoa.java`
- `simplehealth-back/simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/domain/entity/Medico.java`
- `simplehealth-back/simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/domain/entity/Usuario.java`

### Documentação
- [Classes de Análise](./documentos-finais-definitivos/3.4.%20Classes%20de%20Análise/3.4.%20Classes%20de%20Análise_Diagrama%20de%20Classes.md)
- [Modelagem de Classes de Projeto](./documentos-finais-definitivos/3.7_3.9_3.10_Modelagens/3.7.%20Modelagem%20de%20Classes%20de%20Projeto/3.7.%20Modelagem%20de%20Classes%20de%20Projeto.md)
- [Boas Práticas](./documentos-finais-definitivos/3.8%20Boas%20Práticas/3.8%20Boas%20praticas.md)

---

**Assinatura Digital:**  
Correção realizada e validada em 14/12/2025  
Documentação sincronizada com implementação real do backend
