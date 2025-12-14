# Correção da Discrepância 1.3 - Redis para Cache Não Implementado no Módulo Cadastro

**Data:** 14/12/2025  
**Responsável:** Equipe de Documentação  
**Status:** ✅ Concluída

---

## 1. Discrepância Identificada

### Problema
A documentação indicava que o **módulo de Cadastro** utilizaria **Redis 7** para **cache** (listas de médicos, sessões de usuários), mas esta funcionalidade **não foi totalmente implementada**.

### Realidade da Implementação
- ✅ **Redis ESTÁ implementado** no módulo de Cadastro
- ✅ **Pub/Sub ESTÁ implementado** (comunicação entre módulos)
- ❌ **Cache NÃO está implementado**
- ❌ **Session Storage NÃO está implementado**

### Evidências

#### Implementado (Pub/Sub):
```java
// RedisConfig.java
@Configuration
@EnableRedisRepositories
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(...) { ... }
    
    @Bean
    public RedisMessageListenerContainer redisContainer(...) {
        // Listeners para comunicação entre módulos
        container.addMessageListener(..., new PatternTopic("historico.consulta.response"));
        container.addMessageListener(..., new PatternTopic("historico.exame.response"));
        // ...
    }
}

// HistoricoPublisher.java
@Component
public class HistoricoPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    
    public void solicitarHistoricoConsultas(String cpfPaciente) {
        redisTemplate.convertAndSend("historico.consulta.request", request);
    }
}
```

#### NÃO Implementado (Cache):
- ❌ Nenhuma anotação `@Cacheable`, `@CacheEvict`, `@CachePut`
- ❌ Nenhuma configuração de cache manager
- ❌ Nenhum código de cache em services
- ❌ Sem `@EnableCaching`

---

## 2. Análise da Situação

### Funcionalidade Implementada: Pub/Sub

**Propósito:** Comunicação assíncrona entre microsserviços

**Casos de Uso:**
1. ✅ Consultar histórico do paciente em outros módulos
2. ✅ Receber respostas de consultas, exames, procedimentos
3. ✅ Notificar alertas de estoque crítico
4. ✅ Event-driven architecture

**Benefícios:**
- Desacoplamento entre módulos
- Comunicação assíncrona eficiente
- Baixa latência em mensagens

### Funcionalidade NÃO Implementada: Cache

**Propósito (Planejado):** Melhorar performance de consultas frequentes

**Casos de Uso NÃO Implementados:**
- ❌ Cache de lista de médicos disponíveis
- ❌ Cache de especialidades
- ❌ Session storage (usuários logados)
- ❌ Cache de consultas frequentes

**Motivo:** Redução de escopo - não crítico para MVP

---

## 3. Decisão Técnica

### ✅ Decisão: Documentar Implementação Parcial

**Ação:** Atualizar documentação para refletir que Redis está implementado **apenas para Pub/Sub**, não para cache.

**Nota Importante:** A funcionalidade de cache pode ser implementada futuramente adicionando:
- `@EnableCaching` na configuração
- Anotações `@Cacheable` nos services
- Configuração de `CacheManager`

---

## 4. Arquivos Corrigidos

### 4.1. Arquitetura do Sistema - Lógica e Física
**Arquivo:** `docs/documentos-finais-definitivos/3.6. Arquitetura do Sistema - Lógica e Física/3.6. Arquitetura do Sistema - Lógica e Física.md`

**Alterações:**
- ✅ Título da seção corrigido: "Redis 7 (Pub/Sub para Comunicação entre Módulos)"
- ✅ Nota técnica de Discrepância 1.3 adicionada
- ✅ Funcionalidades implementadas listadas (Pub/Sub)
- ✅ Funcionalidades NÃO implementadas listadas (Cache, Session Storage)
- ✅ Justificativa atualizada (comunicação entre módulos)
- ✅ Trade-offs atualizados (mensageria vs cache)

**Antes:**
```markdown
#### Redis 7 (Cache)
**Por quê?**
1. Performance: Cache de listas de médicos disponíveis
2. Session Storage: Sessões de usuários logados
3. Pub/Sub: Comunicação entre módulos
```

**Depois:**
```markdown
#### Redis 7 (Pub/Sub para Comunicação entre Módulos)

> ⚠️ NOTA TÉCNICA - Discrepância 1.3 Resolvida:
> Redis está implementado APENAS para Pub/Sub, NÃO para cache.
> 
> Funcionalidades Implementadas:
> - ✅ Pub/Sub para comunicação assíncrona
> 
> Funcionalidades NÃO Implementadas:
> - ❌ Cache de listas de médicos
> - ❌ Session storage
```

---

### 4.2. Documento de Visão do Projeto
**Arquivo:** `docs/documentos-finais-definitivos/3.1. Documento de Visão do Projeto/Documento de visão do projeto.md`

**Alterações:**
- ✅ Tecnologias do módulo Cadastro: "Cache: Redis 7" → "Comunicação entre Módulos: Redis 7 (Pub/Sub)"
- ✅ Nota explicativa adicionada
- ✅ Tabela de Persistência Poliglota: "Cache" → "Comunicação"
- ✅ Notas de discrepâncias consolidadas

**Antes:**
```markdown
**Tecnologias**:
- Cache: Redis 7
```

**Depois:**
```markdown
**Tecnologias**:
- Comunicação entre Módulos: Redis 7 (Pub/Sub)

> 📝 Nota (Discrepância 1.3): Redis implementado apenas para Pub/Sub, não para cache.
```

**Tabela de Persistência Poliglota - Antes:**
```
| **Cache** | Redis 7 | Performance em leituras frequentes, pub/sub entre módulos |
```

**Tabela de Persistência Poliglota - Depois:**
```
| **Comunicação** | Redis 7 | Pub/Sub entre módulos (event-driven) |

> Notas:
> - 1.2: Auditoria com Cassandra removida
> - 1.3: Redis no Cadastro apenas para Pub/Sub, não cache
```

---

## 5. Validação

### 5.1. Código Backend Revisado

#### Implementado (Pub/Sub):
- ✅ `RedisConfig.java` - configuração completa
- ✅ `HistoricoPublisher.java` - publisher de mensagens
- ✅ `EstoqueAlertaPublisher.java` - publisher de alertas
- ✅ `HistoricoResponseSubscriber.java` - subscriber de respostas
- ✅ `RedisTemplate<String, Object>` configurado
- ✅ `RedisMessageListenerContainer` com listeners
- ✅ Dependência `spring-boot-starter-data-redis` no pom.xml

#### NÃO Implementado (Cache):
- ❌ Sem `@EnableCaching`
- ❌ Sem `CacheManager` bean
- ❌ Sem anotações `@Cacheable` em services
- ❌ Sem configurações de cache (TTL, eviction policy)

### 5.2. Documentação Alinhada
- ✅ Arquitetura: Redis para Pub/Sub claramente especificado
- ✅ Visão do Projeto: tecnologias e tabelas atualizadas
- ✅ Notas técnicas explicativas adicionadas
- ✅ Funcionalidades implementadas vs não implementadas listadas

---

## 6. Arquitetura Atual (Corrigida)

### Comunicação entre Módulos via Redis Pub/Sub

```
┌─────────────────────────────┐
│   simplehealth-back-cadastro│
│      (Spring Boot 3.5.6)    │
└──────────┬──────────────────┘
           │
           ├─────────► PostgreSQL 16:5430
           │           (Dados principais)
           │
           └─────────► Redis 7:6380
                       (Pub/Sub)
                       
                       ┌──────────────────┐
                       │ Topics (Pub/Sub) │
                       ├──────────────────┤
                       │ • historico.*    │
                       │ • estoque.*      │
                       │ • alertas.*      │
                       └──────────────────┘
```

### Fluxo de Comunicação

```
Cadastro (Publisher)
    │
    │ publish("historico.consulta.request", {cpf: "123"})
    ├──────────────────────────────► Redis Pub/Sub
                                          │
                                          │ subscribe("historico.consulta.request")
                                          ├──────────────────────────► Agendamento
                                          │                                 │
                                          │                                 │ processa
                                          │                                 │
                                          │ publish("historico.consulta.response", {...})
                                          ◄─────────────────────────────────┤
    │ subscribe("historico.consulta.response")                             
    ◄───────────────────────────────┤
    │
    │ processa resposta
```

---

## 7. Impacto da Mudança

### Impacto na Documentação
- ✅ **Baixo:** Apenas clarificação do uso real
- ✅ **Positivo:** Documentação agora reflete implementação

### Impacto no Sistema
- ✅ **Nenhum:** Funcionalidade de cache nunca foi implementada
- ✅ **Sem quebras:** Pub/Sub funciona corretamente
- ✅ **Performance:** Aceitável sem cache para MVP

### Funcionalidades Afetadas
- ❌ **Cache de médicos:** Consulta direta ao PostgreSQL (sem impacto significativo)
- ❌ **Session storage:** Gerenciado pela aplicação (Spring Security)
- ✅ **Comunicação entre módulos:** Funcional via Pub/Sub

---

## 8. Implementação Atual do Redis

### Classes Implementadas

#### 1. RedisConfig.java
```java
@Configuration
@EnableRedisRepositories
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
    
    @Bean
    public RedisMessageListenerContainer redisContainer(
        RedisConnectionFactory factory,
        MessageListenerAdapter historicoListener
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(historicoListener, 
            new PatternTopic("historico.consulta.response"));
        // ... outros listeners
        return container;
    }
}
```

#### 2. HistoricoPublisher.java
```java
@Component
public class HistoricoPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    
    public void solicitarHistoricoConsultas(String cpfPaciente) {
        Map<String, String> request = Map.of(
            "cpf", cpfPaciente,
            "timestamp", LocalDateTime.now().toString()
        );
        redisTemplate.convertAndSend("historico.consulta.request", request);
    }
    
    public void solicitarHistoricoExames(String cpfPaciente) {
        redisTemplate.convertAndSend("historico.exame.request", ...);
    }
    // ... outros métodos
}
```

#### 3. EstoqueAlertaPublisher.java
```java
@Component
public class EstoqueAlertaPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    
    public void enviarAlertaEstoqueCritico(String medicamento, int quantidade) {
        Map<String, Object> alerta = Map.of(
            "medicamento", medicamento,
            "quantidadeAtual", quantidade,
            "timestamp", LocalDateTime.now()
        );
        redisTemplate.convertAndSend("estoque.alerta.critico", alerta);
    }
}
```

#### 4. HistoricoResponseSubscriber.java
```java
@Component
public class HistoricoResponseSubscriber implements MessageListener {
    private final Map<String, CompletableFuture<String>> pendingRequests = 
        new ConcurrentHashMap<>();
    
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = new String(message.getChannel());
            String body = new String(message.getBody());
            
            // Processa resposta e completa Future
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> response = mapper.readValue(body, Map.class);
            
            String requestId = (String) response.get("requestId");
            CompletableFuture<String> future = pendingRequests.remove(requestId);
            if (future != null) {
                future.complete(body);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar mensagem do Redis", e);
        }
    }
}
```

---

## 9. Próximos Passos (Implementação de Cache - Opcional)

### Se cache for necessário no futuro:

#### Passo 1: Habilitar Cache
```java
@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer()
                )
            );
        
        return RedisCacheManager.builder(factory)
            .cacheDefaults(config)
            .build();
    }
}
```

#### Passo 2: Usar Anotações nos Services
```java
@Service
public class MedicoService {
    
    @Cacheable(value = "medicos", key = "#root.methodName")
    public List<Medico> findAll() {
        return repo.findAll();
    }
    
    @Cacheable(value = "medicos", key = "#id")
    public Medico findById(Long id) {
        return repo.findById(id).orElseThrow(...);
    }
    
    @CacheEvict(value = "medicos", allEntries = true)
    public Medico create(Medico medico) {
        return repo.save(medico);
    }
    
    @CachePut(value = "medicos", key = "#id")
    public Medico update(Long id, Medico medico) {
        // ...
    }
}
```

#### Passo 3: Configurar TTL e Eviction
```yaml
# application.yml
spring:
  cache:
    type: redis
    redis:
      time-to-live: 600000  # 10 minutos
      cache-null-values: false
  redis:
    host: localhost
    port: 6380
    timeout: 2000ms
```

---

## 10. Lições Aprendidas

### Gestão de Escopo
- ✅ Priorizar funcionalidades críticas (Pub/Sub > Cache)
- ✅ MVP pode funcionar sem cache se performance for aceitável
- ✅ Redis Pub/Sub é suficiente para comunicação entre módulos

### Arquitetura
- ✅ Pub/Sub desacopla módulos eficientemente
- ✅ Cache é otimização, não requisito crítico
- ✅ Event-driven architecture facilita escalabilidade

### Documentação
- ⚠️ Diferenciar claramente funcionalidades planejadas vs implementadas
- ⚠️ Especificar o uso real de cada tecnologia
- ✅ Documentar decisões de redução de escopo

---

## 11. Comparação: Planejado vs Implementado

| Funcionalidade | Planejado | Implementado | Status |
|----------------|-----------|--------------|--------|
| Redis (geral) | ✅ | ✅ | Parcial |
| Pub/Sub | ✅ | ✅ | ✅ Completo |
| Cache de médicos | ✅ | ❌ | ❌ Não implementado |
| Session storage | ✅ | ❌ | ❌ Não implementado |
| Cache de consultas | ✅ | ❌ | ❌ Não implementado |
| Event-driven | ✅ | ✅ | ✅ Completo |
| Comunicação entre módulos | ✅ | ✅ | ✅ Completo |

---

## 12. Checklist de Validação

- [x] Código backend verificado (Redis Pub/Sub implementado)
- [x] Código backend verificado (Cache NÃO implementado)
- [x] pom.xml verificado (dependência Redis presente)
- [x] RedisConfig.java analisado (Pub/Sub configurado)
- [x] Arquitetura documentada corrigida
- [x] Visão do Projeto atualizada
- [x] Notas técnicas adicionadas
- [x] Funcionalidades implementadas vs não implementadas clarificadas
- [x] Tabelas de persistência poliglota atualizadas

---

## 13. Referências

### Arquivos Relacionados (Implementados)
- `simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/web/subscribers/RedisConfig.java`
- `simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/infrastructure/redis/publishers/HistoricoPublisher.java`
- `simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/infrastructure/redis/publishers/EstoqueAlertaPublisher.java`
- `simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/infrastructure/redis/subscribers/HistoricoResponseSubscriber.java`

### Documentação Corrigida
- [Arquitetura do Sistema](./documentos-finais-definitivos/3.6.%20Arquitetura%20do%20Sistema%20-%20Lógica%20e%20Física/3.6.%20Arquitetura%20do%20Sistema%20-%20Lógica%20e%20Física.md)
- [Documento de Visão](./documentos-finais-definitivos/3.1.%20Documento%20de%20Visão%20do%20Projeto/Documento%20de%20visão%20do%20projeto.md)

### Outras Discrepâncias
- [Discrepância 1.1 - Médico vs Usuario](./CORRECAO_DISCREPANCIA_1.1.md)
- [Discrepância 1.2 - EventoAuditoria com Cassandra](./CORRECAO_DISCREPANCIA_1.2.md)

---

**Assinatura Digital:**  
Correção realizada e validada em 14/12/2025  
Documentação sincronizada com implementação real do backend  
Redis Pub/Sub funcional - Cache não implementado (redução de escopo)
