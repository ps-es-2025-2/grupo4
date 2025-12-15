# 🗂️ SimpleHealth – Perguntas ao Stakeholder (Módulo de Cadastro Base)

---

## I. Estrutura e Tipos de Cadastro (Perspectiva Arquitetura/TI)

1. **Quais são as entidades de cadastro consideradas fundamentais (ex: Pacientes, Médicos, Fornecedores, Itens de Estoque, Serviços/Procedimentos)? O sistema deve ter um módulo centralizado para gerenciar a duplicação e integridade desses dados?**  
   **R1:** "Todas essas são fundamentais. Precisamos de uma **visão centralizada** (um Módulo de Cadastro Mestre) para garantir que um mesmo fornecedor ou item de estoque, por exemplo, não seja cadastrado duas vezes com pequenas variações. A **integridade dos dados** é a prioridade aqui."

2. **Como o sistema deve lidar com a diferenciação entre Itens de Estoque (produtos físicos) e Serviços (procedimentos médicos, taxas)? É necessário um campo obrigatório de Classificação Fiscal (ex: NCM) apenas para os itens físicos?**  
   **R2:** "Devem ser tratados como entidades separadas, embora possam compartilhar alguns campos básicos. A **Classificação Fiscal (NCM)** e informações como Unidade de Medida são **obrigatórias apenas** para os **Itens Físicos/Estoque**. Para **Serviços**, precisamos de campos específicos como a Tabela TUSS/TISS (padrões de saúde)."

---

## II. Regras de Validação e Obrigatoriedade (Perspectiva Qualidade de Dados)

3. **Quais campos são considerados obrigatórios para o cadastro de um Item de Estoque (ex: Descrição, Unidade de Medida, NCM, Categoria)? O sistema deve permitir criar "templates" ou perfis de obrigatoriedade por Categoria (ex: Medicamentos vs. Materiais de Escritório)?**  
   **R3:** "Descrição, Unidade, Categoria e NCM são mínimos. Seria ideal ter **templates/perfis** para a obrigatoriedade. Por exemplo, Medicamentos exigem Lote Mínimo e Máximo obrigatórios, enquanto Material de Escritório não exige. Isso simplificaria a entrada de dados e evitaria erros."

4. **Quais são os principais critérios de validação no cadastro de Fornecedores? (Ex: Validação de CNPJ/CPF, Inscrição Estadual, Obrigatoriedade de Contatos/E-mail). É necessário um campo para indicar se o fornecedor está Ativo ou Inativo?**  
   **R4:** "Obrigatório é a validação de **CNPJ/CPF**. Além disso, o e-mail e o contato do setor comercial/financeiro são essenciais. E sim, o campo **Ativo/Inativo** é crucial para bloquear a emissão de pedidos de compra para fornecedores que não utilizamos mais."

5. **No cadastro de Procedimentos/Serviços, quais códigos de padronização são essenciais para faturamento e integração? (Ex: TUSS/TISS, Código Próprio, Cód. SUS). O sistema deve garantir que cada serviço tenha pelo menos um código de padronização registrado?**  
   **R5:** "O padrão **TUSS/TISS** é vital para o faturamento de convênios, então é obrigatório para todos os serviços médicos. O **Código Próprio** é importante para controle interno. O sistema deve emitir um alerta se o serviço não tiver um código TUSS/TISS válido, impedindo o faturamento."

---

## III. Processos de Alteração e Auditoria (Perspectiva Segurança e Histórico)

6. **Qual é o processo para realizar alterações em cadastros já existentes (ex: mudar o preço de um item, alterar a categoria de um serviço)? O sistema deve exigir um registro de "Motivo da Alteração" e manter um histórico completo de todas as modificações (Quem, Quando, Qual Campo, Valor Antigo/Novo)?**  
   **R6:** "Toda alteração em cadastros mestre (Itens, Fornecedores, Serviços) deve ser auditada. É **obrigatório** o registro de **Motivo da Alteração** e a manutenção do **Histórico Completo** (`log`). Isso é fundamental para a auditoria de preços e conformidade."

7. **Quem tem a permissão de criar, alterar e inativar os cadastros mestre (ex: Itens, Serviços, Fornecedores)? As permissões devem ser restritas a um pequeno grupo de usuários (ex: Gestores de Suprimentos ou Financeiro)?**  
   **R7:** "As permissões devem ser **estritamente restritas**. Apenas o **Gestor de Suprimentos** ou o **Gestor Financeiro** (dependendo da entidade) deve ter acesso para Criar/Alterar. O acesso à Inativação também deve ser controlado, pois impacta diretamente nos processos de compra e agendamento."

---

## IV. Integração e Complexidade (Perspectiva Sistêmica)

8. **O módulo de cadastro precisa suportar a gestão de Hierarquias? (Ex: um item de estoque que é componente de um kit, ou um procedimento que depende de um serviço pai). Como essa estrutura é visualizada?**  
   **R8:** "Sim, é muito importante. Principalmente para Kits e Composições. Precisamos cadastrar **Kits de Materiais** (ex: Kit Cirurgia de Joelho) onde o item 'Kit' é pai e os materiais individuais são os componentes filhos. O sistema deve visualizar essa composição de forma clara e gerenciar o saldo do Kit pela soma dos seus componentes."

9. **Como o sistema deve lidar com a necessidade de indexar informações para Busca Rápida? No cadastro de Itens, é necessário usar campos alternativos de busca (ex: Código de Barras, Código do Fornecedor, Sinônimos/Palavras-Chave) para otimizar a localização do item?**  
   **R9:** "A busca rápida é essencial. Além da descrição principal e do código interno, precisamos de campos para **Código de Barras** e **Código do Fornecedor**, que são os mais usados na entrada e saída de estoque. **Palavras-Chave** (sinônimos) também ajudariam muito na hora de pesquisar um item com nome longo ou técnico."

10. **Em relação a medicamentos, é necessário o cadastro de informações complementares além daquelas do item de estoque? (Ex: Princípio Ativo, Concentração, Forma Farmacêutica). O sistema deve integrar-se com alguma base de dados de medicamentos (ex: BULÁRIO) para preencher automaticamente campos técnicos?**  
    **R10:** "Sim, para medicamentos, precisamos do **Princípio Ativo**, **Concentração** e **Forma Farmacêutica** (comprimido, injetável, etc.). A integração com uma base de dados externa (bulário ou similar) seria um **ganho enorme** para garantir a precisão e padronização desses campos técnicos, reduzindo o trabalho manual."