package com.grupo4.SimpleHealth;

import com.grupo4.SimpleHealth.controller.PedidoController;
import com.grupo4.SimpleHealth.entity.Pedido;
import com.grupo4.SimpleHealth.service.PedidoService;
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

@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Test
    public void testListar() throws Exception {
        Pedido pedido1 = new Pedido();
        pedido1.setIdPedido(1L);
        pedido1.setStatus("Pendente");

        Pedido pedido2 = new Pedido();
        pedido2.setIdPedido(2L);
        pedido2.setStatus("Entregue");

        when(pedidoService.listar()).thenReturn(Arrays.asList(pedido1, pedido2));

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("Pendente"))
                .andExpect(jsonPath("$[1].status").value("Entregue"));
    }

    @Test
    public void testBuscarPorId() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(1L);
        pedido.setStatus("Pendente");

        when(pedidoService.buscarPorId(1L)).thenReturn(Optional.of(pedido));

        mockMvc.perform(get("/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Pendente"));
    }

    @Test
    public void testBuscarPorIdNotFound() throws Exception {
        when(pedidoService.buscarPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/pedidos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSalvar() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(1L);
        pedido.setStatus("Pendente");

        when(pedidoService.salvar(any(Pedido.class))).thenReturn(pedido);

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"Pendente\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Pendente"));
    }

    @Test
    public void testAtualizar() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(1L);
        pedido.setStatus("Concluído");

        when(pedidoService.atualizar(anyLong(), any(Pedido.class))).thenReturn(pedido);

        mockMvc.perform(put("/pedidos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"Concluído\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Concluído"));
    }

    @Test
    public void testAtualizarNotFound() throws Exception {
        when(pedidoService.atualizar(anyLong(), any(Pedido.class))).thenThrow(new RuntimeException());

        mockMvc.perform(put("/pedidos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"Concluído\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeletar() throws Exception {
        mockMvc.perform(delete("/pedidos/1"))
                .andExpect(status().isNoContent());
    }
}