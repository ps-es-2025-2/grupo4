package com.grupo4.SimpleHealth;

import com.grupo4.SimpleHealth.controller.EstoqueController;
import com.grupo4.SimpleHealth.entity.Estoque;
import com.grupo4.SimpleHealth.service.EstoqueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EstoqueController.class)
public class EstoqueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstoqueService estoqueService;

    @Test
    public void testListar() throws Exception {
        Estoque estoque1 = new Estoque();
        estoque1.setIdEstoque(1L);
        estoque1.setLocal("Prateleira A");

        Estoque estoque2 = new Estoque();
        estoque2.setIdEstoque(2L);
        estoque2.setLocal("Prateleira B");

        when(estoqueService.listar()).thenReturn(Arrays.asList(estoque1, estoque2));

        mockMvc.perform(get("/estoques"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].local").value("Prateleira A"))
                .andExpect(jsonPath("$[1].local").value("Prateleira B"));
    }

    @Test
    public void testBuscarPorId() throws Exception {
        Estoque estoque = new Estoque();
        estoque.setIdEstoque(1L);
        estoque.setLocal("Prateleira A");

        when(estoqueService.buscarPorId(1L)).thenReturn(Optional.of(estoque));

        mockMvc.perform(get("/estoques/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.local").value("Prateleira A"));
    }

    @Test
    public void testBuscarPorIdNotFound() throws Exception {
        when(estoqueService.buscarPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/estoques/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSalvar() throws Exception {
        Estoque estoque = new Estoque();
        estoque.setIdEstoque(1L);
        estoque.setLocal("Prateleira A");

        when(estoqueService.salvar(any(Estoque.class))).thenReturn(estoque);

        mockMvc.perform(post("/estoques")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"local\": \"Prateleira A\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.local").value("Prateleira A"));
    }

    @Test
    public void testAtualizar() throws Exception {
        Estoque estoque = new Estoque();
        estoque.setIdEstoque(1L);
        estoque.setLocal("Prateleira C");

        when(estoqueService.atualizar(anyLong(), any(Estoque.class))).thenReturn(estoque);

        mockMvc.perform(put("/estoques/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"local\": \"Prateleira C\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.local").value("Prateleira C"));
    }

    @Test
    public void testAtualizarNotFound() throws Exception {
        when(estoqueService.atualizar(anyLong(), any(Estoque.class))).thenThrow(new RuntimeException());

        mockMvc.perform(put("/estoques/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"local\": \"Prateleira C\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeletar() throws Exception {
        mockMvc.perform(delete("/estoques/1"))
                .andExpect(status().isNoContent());
    }
}