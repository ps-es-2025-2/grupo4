//package com.simplehealth.cadastro;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.Arrays;
//import java.util.Optional;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//@WebMvcTest(ItemController.class)
//public class ItemControllerTest {
//
//  @Autowired
//  private MockMvc mockMvc;
//
//  @MockBean
//  private ItemService itemService;
//
//  @Test
//  public void testListar() throws Exception {
//    Item item1 = new Alimento();
//    item1.setIdItem(1L);
//    item1.setNome("Item A");
//
//    Item item2 = new Alimento();
//    item2.setIdItem(2L);
//    item2.setNome("Item B");
//
//    when(itemService.listar()).thenReturn(Arrays.asList(item1, item2));
//
//    mockMvc.perform(get("/itens"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$[0].nome").value("Item A"))
//        .andExpect(jsonPath("$[1].nome").value("Item B"));
//  }
//
//  @Test
//  public void testBuscarPorId() throws Exception {
//    Item item = new Alimento();
//    item.setIdItem(1L);
//    item.setNome("Item A");
//
//    when(itemService.buscarPorId(1L)).thenReturn(Optional.of(item));
//
//    mockMvc.perform(get("/itens/1"))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.nome").value("Item A"));
//  }
//
//  @Test
//  public void testBuscarPorIdNotFound() throws Exception {
//    when(itemService.buscarPorId(1L)).thenReturn(Optional.empty());
//
//    mockMvc.perform(get("/itens/1"))
//        .andExpect(status().isNotFound());
//  }
//
//  @Test
//  public void testDeletar() throws Exception {
//    mockMvc.perform(delete("/itens/1"))
//        .andExpect(status().isNoContent());
//  }
//}