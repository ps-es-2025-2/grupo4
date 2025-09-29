package br.com.simplehealth.armazenamento.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

/**
 * Testes unitários para a classe Medicamento.
 */
class MedicamentoTest {

    private Medicamento medicamento;

    @BeforeEach
    void setUp() {
        medicamento = new Medicamento();
    }

    @Test
    void testConstrutorVazio() {
        assertNotNull(medicamento);
        assertEquals("MEDICAMENTO", medicamento.getTipo());
        assertNull(medicamento.getPrescricao());
        assertNull(medicamento.getComposicao());
        assertNull(medicamento.getBula());
        assertNull(medicamento.getTarja());
        assertNull(medicamento.getModoConsumo());
    }

    @Test
    void testConstrutorComParametros() {
        LocalDate validade = LocalDate.now().plusMonths(12);
        
        medicamento = new Medicamento("Aspirina", "Ácido acetilsalicílico", "comprimido",
                                     100, validade, "LOTE001", "Sem prescrição", 
                                     "C9H8O4", "Bula da aspirina", "LIVRE", "Via oral");

        assertEquals("Aspirina", medicamento.getNome());
        assertEquals("MEDICAMENTO", medicamento.getTipo());
        assertEquals("Sem prescrição", medicamento.getPrescricao());
        assertEquals("C9H8O4", medicamento.getComposicao());
        assertEquals("Bula da aspirina", medicamento.getBula());
        assertEquals("LIVRE", medicamento.getTarja());
        assertEquals("Via oral", medicamento.getModoConsumo());
    }

    @Test
    void testHerancaDeItem() {
        medicamento.setNome("Paracetamol");
        medicamento.setQuantidadeTotal(50);
        
        assertTrue(medicamento instanceof Item);
        assertEquals("Paracetamol", medicamento.getNome());
        assertEquals(50, medicamento.getQuantidadeTotal());
        assertEquals("MEDICAMENTO", medicamento.getTipo());
    }

    @Test
    void testSettersEGetters() {
        String prescricao = "Receita médica";
        String composicao = "Dipirona 500mg";
        String bula = "Tomar 1 comprimido...";
        String tarja = "VERMELHA";
        String modoConsumo = "Via oral com água";

        medicamento.setPrescricao(prescricao);
        medicamento.setComposicao(composicao);
        medicamento.setBula(bula);
        medicamento.setTarja(tarja);
        medicamento.setModoConsumo(modoConsumo);

        assertEquals(prescricao, medicamento.getPrescricao());
        assertEquals(composicao, medicamento.getComposicao());
        assertEquals(bula, medicamento.getBula());
        assertEquals(tarja, medicamento.getTarja());
        assertEquals(modoConsumo, medicamento.getModoConsumo());
    }

    @Test
    void testToString() {
        medicamento.setIdItem(1L);
        medicamento.setNome("Dipirona");
        medicamento.setTarja("VERMELHA");
        medicamento.setModoConsumo("Via oral");
        medicamento.setQuantidadeTotal(20);

        String resultado = medicamento.toString();
        
        assertTrue(resultado.contains("idItem=1"));
        assertTrue(resultado.contains("nome='Dipirona'"));
        assertTrue(resultado.contains("tarja='VERMELHA'"));
        assertTrue(resultado.contains("modoConsumo='Via oral'"));
        assertTrue(resultado.contains("quantidadeTotal=20"));
    }
}