package com.linktic.pruebacompralinktic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linktic.pruebacompralinktic.dto.CompraProductoDto;
import com.linktic.pruebacompralinktic.dto.CompraProductoDtoOut;
import com.linktic.pruebacompralinktic.dto.ProductoDtoOut;
import com.linktic.pruebacompralinktic.service.implementation.CompraService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // cargar contexto de app
@AutoConfigureMockMvc
class CompraControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CompraService compraService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void comprarProductoExitoso() throws Exception {
    ProductoDtoOut productoDtoOut = new ProductoDtoOut();
    productoDtoOut.setId(1);
    productoDtoOut.setNombre("Cuaderno 100 hojas");

    CompraProductoDtoOut compraProductoDtoOut = new CompraProductoDtoOut();
    compraProductoDtoOut.setProducto(productoDtoOut);
    compraProductoDtoOut.setCantidadComprada(10);
    compraProductoDtoOut.setPrecioTotal(50000L);

    CompraProductoDto compraProductoDto = new CompraProductoDto();
    compraProductoDto.setIdProducto(1);
    compraProductoDto.setCantidad(10);
    String json = objectMapper.writeValueAsString(compraProductoDto);

    when(compraService.comprarProducto(any(CompraProductoDto.class))).thenReturn(compraProductoDtoOut);

    mockMvc.perform(post("/compra/comprar-producto")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.precioTotal").value(50000L));
  }
}
