//package com.simplehealth.cadastro;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.Arrays;
//import java.util.Optional;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//@WebMvcTest(FornecedorController.class)
//public class FornecedorControllerTest {
//
//  @Autowired
//  private MockMvc mockMvc;
//
//  @MockBean
//  private FornecedorService fornecedorService;
//
//  @Test
//  public void testListar() throws Exception {
//    Fornecedor fornecedor1 = new Fornecedor();
//    fornecedor1.setIdFornecedor(1L);
//    fornecedor1.setNome("Fornecedor A");
//
//    Fornecedor fornecedor2 = new Fornecedor();
//    fornecedor2.setIdFornecedor(2L);
//    fornecedor2.setNome("Fornecedor B");
//
//    when(fornecedorService.listar()).thenReturn(Arrays.asList(fornecedor1, fornecedor2));
//
//    mockMvc.perform(get("/fornecedores"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$[0].nome").value("Fornecedor A"))
//        .andExpect(jsonPath("$[1].nome").value("Fornecedor B"));
//  }
//
//  @Test
//  public void testBuscarPorId() throws Exception {
//    Fornecedor fornecedor = new Fornecedor();
//    fornecedor.setIdFornecedor(1L);
//    fornecedor.setNome("Fornecedor A");
//
//    when(fornecedorService.buscarPorId(1L)).thenReturn(Optional.of(fornecedor));
//
//    mockMvc.perform(get("/fornecedores/1"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.nome").value("Fornecedor A"));
//  }
//
//  @Test
//  public void testBuscarPorIdNotFound() throws Exception {
//    when(fornecedorService.buscarPorId(1L)).thenReturn(Optional.empty());
//
//    mockMvc.perform(get("/fornecedores/1"))
//        .andExpect(status().isNotFound());
//  }
//
//  @Test
//  public void testSalvar() throws Exception {
//    Fornecedor fornecedor = new Fornecedor();
//    fornecedor.setIdFornecedor(1L);
//    fornecedor.setNome("Fornecedor A");
//
//    when(fornecedorService.salvar(any(Fornecedor.class))).thenReturn(fornecedor);
//
//    mockMvc.perform(post("/fornecedores")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content("{\"nome\": \"Fornecedor A\"}"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.nome").value("Fornecedor A"));
//  }
//
//  @Test
//  public void testAtualizar() throws Exception {
//    Fornecedor fornecedor = new Fornecedor();
//    fornecedor.setIdFornecedor(1L);
//    fornecedor.setNome("Fornecedor C");
//
//    when(fornecedorService.atualizar(anyLong(), any(Fornecedor.class))).thenReturn(fornecedor);
//
//    mockMvc.perform(put("/fornecedores/1")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content("{\"nome\": \"Fornecedor C\"}"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.nome").value("Fornecedor C"));
//  }
//
//  @Test
//  public void testAtualizarNotFound() throws Exception {
//    when(fornecedorService.atualizar(anyLong(), any(Fornecedor.class))).thenThrow(new RuntimeException());
//
//    mockMvc.perform(put("/fornecedores/1")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content("{\"nome\": \"Fornecedor C\"}"))
//        .andExpect(status().isNotFound());
//  }
//
//  @Test
//  public void testDeletar() throws Exception {
//    mockMvc.perform(delete("/fornecedores/1"))
//        .andExpect(status().isNoContent());
//  }
//}