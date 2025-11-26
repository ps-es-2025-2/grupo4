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
//@WebMvcTest(AlimentoController.class)
//public class AlimentoControllerTest {
//
//  @Autowired
//  private MockMvc mockMvc;
//
//  @MockBean
//  private AlimentoService alimentoService;
//
//  @Test
//  public void testListar() throws Exception {
//    Alimento alimento1 = new Alimento();
//    alimento1.setIdItem(1L);
//    alimento1.setNome("Arroz");
//
//    Alimento alimento2 = new Alimento();
//    alimento2.setIdItem(2L);
//    alimento2.setNome("Feijão");
//
//    when(alimentoService.listar()).thenReturn(Arrays.asList(alimento1, alimento2));
//
//    mockMvc.perform(get("/alimentos"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$[0].nome").value("Arroz"))
//        .andExpect(jsonPath("$[1].nome").value("Feijão"));
//  }
//
//  @Test
//  public void testBuscarPorId() throws Exception {
//    Alimento alimento = new Alimento();
//    alimento.setIdItem(1L);
//    alimento.setNome("Arroz");
//
//    when(alimentoService.buscarPorId(1L)).thenReturn(Optional.of(alimento));
//
//    mockMvc.perform(get("/alimentos/1"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.nome").value("Arroz"));
//  }
//
//  @Test
//  public void testBuscarPorIdNotFound() throws Exception {
//    when(alimentoService.buscarPorId(1L)).thenReturn(Optional.empty());
//
//    mockMvc.perform(get("/alimentos/1"))
//        .andExpect(status().isNotFound());
//  }
//
//  @Test
//  public void testSalvar() throws Exception {
//    Alimento alimento = new Alimento();
//    alimento.setIdItem(1L);
//    alimento.setNome("Arroz");
//
//    when(alimentoService.salvar(any(Alimento.class))).thenReturn(alimento);
//
//    mockMvc.perform(post("/alimentos")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content("{\"nome\": \"Arroz\"}"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.nome").value("Arroz"));
//  }
//
//  @Test
//  public void testAtualizar() throws Exception {
//    Alimento alimento = new Alimento();
//    alimento.setIdItem(1L);
//    alimento.setNome("Arroz Integral");
//
//    when(alimentoService.atualizar(anyLong(), any(Alimento.class))).thenReturn(alimento);
//
//    mockMvc.perform(put("/alimentos/1")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content("{\"nome\": \"Arroz Integral\"}"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.nome").value("Arroz Integral"));
//  }
//
//  @Test
//  public void testAtualizarNotFound() throws Exception {
//    when(alimentoService.atualizar(anyLong(), any(Alimento.class))).thenThrow(new RuntimeException());
//
//    mockMvc.perform(put("/alimentos/1")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content("{\"nome\": \"Arroz Integral\"}"))
//        .andExpect(status().isNotFound());
//  }
//
//  @Test
//  public void testDeletar() throws Exception {
//    mockMvc.perform(delete("/alimentos/1"))
//        .andExpect(status().isNoContent());
//  }
//}