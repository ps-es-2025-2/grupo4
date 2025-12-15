# SimpleHealth – Perguntas ao Stakeholder (Módulo de Estoque)

---

## I. Detalhamento da Estrutura e Organização do Estoque (Perspectiva Gerencial)

1. **Como o estoque está fisicamente organizado? O sistema deve suportar múltiplas Localidades/Almoxarifados (ex: Estoque Central, Farmácia, Sala de Procedimentos) e, se sim, a visualização do saldo deve ser consolidada ou separada por local?**  
   **R1:** "Sim, precisamos de múltiplas localidades. Temos um **Estoque Central** para itens de maior volume e as **Salas de Procedimentos/Unidades** que mantêm estoques menores (estoque de passagem). A visualização precisa ser **separada por local** para sabermos onde o item está exatamente, mas a busca inicial pode mostrar o saldo consolidado de todos os locais."

2. **Qual é o critério principal para categorizar os itens (medicamentos, materiais de consumo, equipamentos, etc.)? Além disso, o sistema deve suportar a gestão de Itens Controlados (ex: medicamentos sujeitos a controle especial da ANVISA) com necessidade de rastreabilidade e relatórios específicos?**  
   **R2:** "A categorização principal é por **Tipo de Material** (Medicamento, Material Médico-Hospitalar, Material de Escritório). E sim, a gestão de **Itens Controlados** é obrigatória. Eles precisam ter um campo para o número de lote, data de validade e, idealmente, um alerta que force a conferência por dupla checagem na saída."

---

## II. Processos de Movimentação e Rastreabilidade (Perspectiva Estoquista/Enfermagem)

3. **Quais são os principais tipos de movimentação de estoque que o sistema deve registrar? (Ex: Entrada por Compra/Nota Fiscal, Saída por Consumo/Uso, Transferência entre Almoxarifados, Perda/Vencimento). O registro de saída por consumo deve ser associado a um Paciente/Prontuário ou a uma Área/Centro de Custo?**  
   **R3:** "Precisamos registrar todas essas: Entrada (NF), Saída por Consumo, Transferência e Ajuste (Perda/Vencimento). A saída por consumo deve ser rastreada: no caso de **medicamentos/materiais de uso direto**, precisa ser ligada ao **Paciente/Prontuário** para cobrança e auditoria. Para materiais gerais (limpeza, escritório), pode ser ligada ao **Centro de Custo** da área que requisitou."

4. **Como é feito o controle de Itens Perecíveis? O sistema deve exigir o registro da Data de Validade e do Lote na entrada, e o processo de saída (Saída por Consumo ou Transferência) deve priorizar o método 'Primeiro que Vence, Primeiro que Sai' (**FEFO - First Expire, First Out**)?**  
   **R4:** "É crucial! O sistema deve obrigar o registro de **Lote e Validade** na entrada. O processo de saída **DEVE** utilizar o **FEFO**, emitindo alertas visuais (ex: cor amarela/vermelha) para itens próximos da validade (ex: 90 ou 30 dias) e, se possível, impedir a saída de um lote com validade mais longa se houver outro com validade mais curta disponível."

5. **Qual é o processo para realizar um Inventário (Contagem Física) do estoque? O sistema deve suportar contagens parciais (cíclicas) de itens específicos, além do inventário geral, e permitir que o usuário gere um documento de contagem para conferência antes de ajustar os saldos?**  
   **R5:** "O ideal é que suporte a **Contagem Cíclica**, permitindo selecionar um grupo de itens (ex: todos os itens controlados) ou uma localização para contagem. O sistema deve gerar uma **folha de contagem cega** (sem saldo atual) para o estoquista preencher e, após a entrada dos dados, mostrar a diferença (divergência) para que o gestor aprove o ajuste final."

---

## III. Aquisição e Gestão de Fornecedores (Perspectiva Compras)

6. **Como é definida a necessidade de compra de um item (Gatilho de Compra)? O sistema deve permitir configurar um Nível Mínimo de Estoque (**Ponto de Pedido**) por item e por localização para emitir um alerta automático?**  
   **R6:** "Sim, o gatilho de compra é o **Ponto de Pedido**. O sistema **precisa** ter um campo para configurar o **Estoque Mínimo** por item e localização. Ao atingir esse mínimo, ele deve gerar um alerta e, idealmente, sugerir uma quantidade a ser comprada baseada no consumo médio ou no Lote de Compra padrão."

7. **O módulo de estoque precisa gerenciar informações de Fornecedores (ex: CNPJ, Contato, Itens Fornecidos)? O sistema deve permitir vincular Múltiplos Fornecedores a um único item, facilitando a cotação de preços?**  
   **R7:** "Sim, a gestão de fornecedores é essencial. Precisamos vincular múltiplos fornecedores a cada item e registrar o **Preço de Compra mais recente** de cada um. O sistema deve permitir a comparação rápida desses preços para auxiliar a área de compras na cotação."

---

## IV. Relatórios e Segurança (Perspectiva Geral/Auditoria)

8. **Quais são os relatórios de estoque considerados cruciais para a gestão? Especifique exemplos (ex: Curva ABC de Consumo, Posição Atual de Estoque, Relatório de Vencimentos, Histórico de Movimentações por Item).**  
   **R8:** "Os mais importantes são:  
   - **Relatório de Vencimentos:** Com filtro de 30/60/90 dias.  
   - **Posição Atual do Estoque:** Saldo por item e localização (com valor total).  
   - **Histórico de Movimentações por Item:** Para auditoria, mostrando quem, quando, e qual tipo de movimento.  
   - **Relatório de Consumo por Paciente/Centro de Custo:** Para rateio e conferência de despesas."

9. **Quais são as regras de permissão essenciais? Quem pode realizar Entrada de Nota Fiscal, quem pode realizar Saída por Consumo, e quem tem permissão para realizar Ajuste de Inventário (que altera o saldo sem um documento de entrada/saída)?**  
   **R9:** "A permissão deve ser bem granular:  
   - **Entrada NF/Compra:** Apenas Compras/Estoquista Chefe.  
   - **Saída por Consumo/Transferência:** Estoquistas, Enfermeiros (mediante requisição).  
   - **Ajuste de Inventário:** Apenas o **Gestor/Estoquista Chefe** e deve exigir um **motivo obrigatório** para qualquer alteração de saldo, pois é um ponto de risco de fraude."

10. **Existe a necessidade de gerenciar o Custo dos itens? O sistema deve calcular o Custo Médio Ponderado (**Custo Médio**) para cada item após cada nova entrada, e esse valor deve ser usado para valorizar o estoque e o consumo?**  
    **R10:** "Sim, o gerenciamento de custos é fundamental para a área financeira. O sistema **deve** calcular o **Custo Médio Ponderado** após cada entrada e esse custo médio deve ser o valor utilizado para dar baixa no estoque no momento do consumo/saída."

---