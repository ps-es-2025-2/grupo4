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
//@WebMvcTest(HospitalarController.class)
//public class HospitalarControllerTest {
//
//  @Autowired
//  private MockMvc mockMvc;
//
//  @MockBean
//  private HospitalarService hospitalarService;
//
//  @Test
//  public void testListar() throws Exception {
//    Hospitalar hospitalar1 = new Hospitalar();
//    hospitalar1.setIdItem(1L);
//    hospitalar1.setNome("Seringa");
//
//    Hospitalar hospitalar2 = new Hospitalar();
//    hospitalar2.setIdItem(2L);
//    hospitalar2.setNome("Gaze");
//
//    when(hospitalarService.listar()).thenReturn(Arrays.asList(hospitalar1, hospitalar2));
//
//    mockMvc.perform(get("/hospitalares"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$[0].nome").value("Seringa"))
//        .andExpect(jsonPath("$[1].nome").value("Gaze"));
//  }
//
//  @Test
//  public void testBuscarPorId() throws Exception {
//    Hospitalar hospitalar = new Hospitalar();
//    hospitalar.setIdItem(1L);
//    hospitalar.setNome("Seringa");
//
//    when(hospitalarService.buscarPorId(1L)).thenReturn(Optional.of(hospitalar));
//
//    mockMvc.perform(get("/hospitalares/1"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.nome").value("Seringa"));
//  }
//
//  @Test
//  public void testBuscarPorIdNotFound() throws Exception {
//    when(hospitalarService.buscarPorId(1L)).thenReturn(Optional.empty());
//
//    mockMvc.perform(get("/hospitalares/1"))
//        .andExpect(status().isNotFound());
//  }
//
//  @Test
//  public void testSalvar() throws Exception {
//    Hospitalar hospitalar = new Hospitalar();
//    hospitalar.setIdItem(1L);
//    hospitalar.setNome("Seringa");
//
//    when(hospitalarService.salvar(any(Hospitalar.class))).thenReturn(hospitalar);
//
//    mockMvc.perform(post("/hospitalares")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content("{\"nome\": \"Seringa\"}"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.nome").value("Seringa"));
//  }
//
//  @Test
//  public void testAtualizar() throws Exception {
//    Hospitalar hospitalar = new Hospitalar();
//    hospitalar.setIdItem(1L);
//    hospitalar.setNome("Agulha");
//
//    when(hospitalarService.atualizar(anyLong(), any(Hospitalar.class))).thenReturn(hospitalar);
//
//    mockMvc.perform(put("/hospitalares/1")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content("{\"nome\": \"Agulha\"}"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.nome").value("Agulha"));
//  }
//
//  @Test
//  public void testAtualizarNotFound() throws Exception {
//    when(hospitalarService.atualizar(anyLong(), any(Hospitalar.class))).thenThrow(new RuntimeException());
//
//    mockMvc.perform(put("/hospitalares/1")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content("{\"nome\": \"Agulha\"}"))
//        .andExpect(status().isNotFound());
//  }
//
//  @Test
//  public void testDeletar() throws Exception {
//    mockMvc.perform(delete("/hospitalares/1"))
//        .andExpect(status().isNoContent());
//  }
//}