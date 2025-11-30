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
//@WebMvcTest(MedicamentoController.class)
//public class MedicamentoControllerTest {
//
//  @Autowired
//  private MockMvc mockMvc;
//
//  @MockBean
//  private MedicamentoService medicamentoService;
//
//  @Test
//  public void testListar() throws Exception {
//    Medicamento medicamento1 = new Medicamento();
//    medicamento1.setIdItem(1L);
//    medicamento1.setNome("Dipirona");
//
//    Medicamento medicamento2 = new Medicamento();
//    medicamento2.setIdItem(2L);
//    medicamento2.setNome("Paracetamol");
//
//    when(medicamentoService.listar()).thenReturn(Arrays.asList(medicamento1, medicamento2));
//
//    mockMvc.perform(get("/medicamentos"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$[0].nome").value("Dipirona"))
//        .andExpect(jsonPath("$[1].nome").value("Paracetamol"));
//  }
//
//  @Test
//  public void testBuscarPorId() throws Exception {
//    Medicamento medicamento = new Medicamento();
//    medicamento.setIdItem(1L);
//    medicamento.setNome("Dipirona");
//
//    when(medicamentoService.buscarPorId(1L)).thenReturn(Optional.of(medicamento));
//
//    mockMvc.perform(get("/medicamentos/1"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.nome").value("Dipirona"));
//  }
//
//  @Test
//  public void testBuscarPorIdNotFound() throws Exception {
//    when(medicamentoService.buscarPorId(1L)).thenReturn(Optional.empty());
//
//    mockMvc.perform(get("/medicamentos/1"))
//        .andExpect(status().isNotFound());
//  }
//
//  @Test
//  public void testSalvar() throws Exception {
//    Medicamento medicamento = new Medicamento();
//    medicamento.setIdItem(1L);
//    medicamento.setNome("Dipirona");
//
//    when(medicamentoService.salvar(any(Medicamento.class))).thenReturn(medicamento);
//
//    mockMvc.perform(post("/medicamentos")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content("{\"nome\": \"Dipirona\"}"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.nome").value("Dipirona"));
//  }
//
//  @Test
//  public void testAtualizar() throws Exception {
//    Medicamento medicamento = new Medicamento();
//    medicamento.setIdItem(1L);
//    medicamento.setNome("Ibuprofeno");
//
//    when(medicamentoService.atualizar(anyLong(), any(Medicamento.class))).thenReturn(medicamento);
//
//    mockMvc.perform(put("/medicamentos/1")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content("{\"nome\": \"Ibuprofeno\"}"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.nome").value("Ibuprofeno"));
//  }
//
//  @Test
//  public void testAtualizarNotFound() throws Exception {
//    when(medicamentoService.atualizar(anyLong(), any(Medicamento.class))).thenThrow(new RuntimeException());
//
//    mockMvc.perform(put("/medicamentos/1")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content("{\"nome\": \"Ibuprofeno\"}"))
//        .andExpect(status().isNotFound());
//  }
//
//  @Test
//  public void testDeletar() throws Exception {
//    mockMvc.perform(delete("/medicamentos/1"))
//        .andExpect(status().isNoContent());
//  }
//}