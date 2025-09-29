package br.com.simplehealth.armazenamento.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

/**
 * Testes unitários para a classe Item.
 */
class ItemTest {

    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item();
    }

    @Test
    void testConstrutorVazio() {
        assertNotNull(item);
        assertNull(item.getIdItem());
        assertNull(item.getNome());
        assertNull(item.getTipo());
    }

    @Test
    void testConstrutorComParametros() {
        LocalDate validade = LocalDate.now().plusMonths(6);
        
        item = new Item("Aspirina", "Medicamento para dor", "MEDICAMENTO", 
                       "comprimido", 100, validade, "LOTE001");

        assertEquals("Aspirina", item.getNome());
        assertEquals("Medicamento para dor", item.getDescricao());
        assertEquals("MEDICAMENTO", item.getTipo());
        assertEquals("comprimido", item.getUnidadeMedida());
        assertEquals(100, item.getQuantidadeTotal());
        assertEquals(validade, item.getValidade());
        assertEquals("LOTE001", item.getLote());
    }

    @Test
    void testSettersEGetters() {
        Long id = 1L;
        String nome = "Paracetamol";
        String descricao = "Analgésico";
        String tipo = "MEDICAMENTO";
        String unidadeMedida = "comprimido";
        Integer quantidade = 50;
        LocalDate validade = LocalDate.now().plusYears(2);
        String lote = "LOTE002";

        item.setIdItem(id);
        item.setNome(nome);
        item.setDescricao(descricao);
        item.setTipo(tipo);
        item.setUnidadeMedida(unidadeMedida);
        item.setQuantidadeTotal(quantidade);
        item.setValidade(validade);
        item.setLote(lote);

        assertEquals(id, item.getIdItem());
        assertEquals(nome, item.getNome());
        assertEquals(descricao, item.getDescricao());
        assertEquals(tipo, item.getTipo());
        assertEquals(unidadeMedida, item.getUnidadeMedida());
        assertEquals(quantidade, item.getQuantidadeTotal());
        assertEquals(validade, item.getValidade());
        assertEquals(lote, item.getLote());
    }

    @Test
    void testToString() {
        item.setIdItem(1L);
        item.setNome("Teste Item");
        item.setTipo("TESTE");
        item.setQuantidadeTotal(10);

        String resultado = item.toString();
        
        assertTrue(resultado.contains("idItem=1"));
        assertTrue(resultado.contains("nome='Teste Item'"));
        assertTrue(resultado.contains("tipo='TESTE'"));
        assertTrue(resultado.contains("quantidadeTotal=10"));
    }
}