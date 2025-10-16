package com.linktic.pruebacompralinktic.service.implementation;

import com.linktic.pruebacompralinktic.client.InventarioClient;
import com.linktic.pruebacompralinktic.client.ProductoClient;
import com.linktic.pruebacompralinktic.dto.*;
import com.linktic.pruebacompralinktic.exception.ElementoNoEncontradoException;
import com.linktic.pruebacompralinktic.exception.ErrorGeneralException;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompraServiceTest {

  @InjectMocks private CompraService compraService;
  @Mock private InventarioClient inventarioClient;
  @Mock private ProductoClient productoClient;

  @Test
  void comprarProductoExitoso() throws ElementoNoEncontradoException, ErrorGeneralException {
    CompraProductoDto compraProductoDto = new CompraProductoDto();
    compraProductoDto.setIdProducto(1);
    compraProductoDto.setCantidad(10);

    ProductoDtoOut productoDtoOut = new ProductoDtoOut();
    productoDtoOut.setId(compraProductoDto.getIdProducto());
    productoDtoOut.setNombre("Cuaderno 100 hojas");
    productoDtoOut.setPrecio(5000L);

    InventarioDtoOut inventarioDtoOut = new InventarioDtoOut();
    inventarioDtoOut.setCantidad(100);

    MensajeOutDto mensajeOutDto = new MensajeOutDto();
    mensajeOutDto.setResultado("EXITOSO");

    // consultaProducto
    when(productoClient.obtenerProductoPorId(any(), eq(compraProductoDto.getIdProducto())))
        .thenReturn(productoDtoOut);

    // validarDisponibilidadProducto
    when(inventarioClient.obtenerInventarioProducto(any(), eq(compraProductoDto.getIdProducto())))
        .thenReturn(inventarioDtoOut);

    // actualizarInventario
    when(inventarioClient.actualizarCantidad(any(), any(ActualizaInventarioDto.class))).thenReturn(mensajeOutDto);

    CompraProductoDtoOut resultado = compraService.comprarProducto(compraProductoDto);
    assert resultado != null;
    Long precioTotal = productoDtoOut.getPrecio() * compraProductoDto.getCantidad();
    assertEquals(resultado.getPrecioTotal(), precioTotal);
  }

  @Test
  void comprarProductoFallidoNoProducto() {

    String mensajeEx = "Producto no encontrado";
    CompraProductoDto compraProductoDto = new CompraProductoDto();
    compraProductoDto.setIdProducto(1);
    compraProductoDto.setCantidad(10);

    // Cuerpo JSON de error
    String jsonError = "{\"codigo\":404,\"resultado\":\"ERROR\",\"mensaje\":\"Producto no encontrado\"}";

    // Crea un FeignException simulado
    FeignException feignException = mock(FeignException.class);
    when(feignException.contentUTF8()).thenReturn(jsonError);

    // consultaProducto
    when(productoClient.obtenerProductoPorId(any(), eq(compraProductoDto.getIdProducto())))
        .thenThrow(feignException);

    ElementoNoEncontradoException ex = assertThrows(ElementoNoEncontradoException.class, () -> {
      compraService.comprarProducto(compraProductoDto);
    });

    assertEquals(mensajeEx, ex.getMessage());

  }

  @Test
  void comprarProductoFallidoInesperadoProducto() {

    String mensajeEx = "Error inesperado";
    CompraProductoDto compraProductoDto = new CompraProductoDto();
    compraProductoDto.setIdProducto(1);
    compraProductoDto.setCantidad(10);

    // Cuerpo JSON de error
    String jsonError = "{\"codigo\":500,\"resultado\":\"ERROR\",\"mensaje\":\"Error inesperado\"}";

    // Crea un FeignException simulado
    FeignException feignException = mock(FeignException.class);
    when(feignException.contentUTF8()).thenReturn(jsonError);

    // consultaProducto
    when(productoClient.obtenerProductoPorId(any(), eq(compraProductoDto.getIdProducto())))
        .thenThrow(feignException);

    ErrorGeneralException ex = assertThrows(ErrorGeneralException.class, () -> {
      compraService.comprarProducto(compraProductoDto);
    });

    assertEquals(mensajeEx, ex.getMessage());

  }

  @Test
  void comprarProductoFallidoNoInventario() {

    String mensajeEx = "Inventario no encontrado";
    CompraProductoDto compraProductoDto = new CompraProductoDto();
    compraProductoDto.setIdProducto(1);
    compraProductoDto.setCantidad(10);

    ProductoDtoOut productoDtoOut = new ProductoDtoOut();
    productoDtoOut.setId(compraProductoDto.getIdProducto());
    productoDtoOut.setNombre("Cuaderno 100 hojas");
    productoDtoOut.setPrecio(5000L);

    // Cuerpo JSON de error
    String jsonError = "{\"codigo\":404,\"resultado\":\"ERROR\",\"mensaje\":\"Inventario no encontrado\"}";

    // Crea un FeignException simulado
    FeignException feignException = mock(FeignException.class);
    when(feignException.contentUTF8()).thenReturn(jsonError);

    // consultaProducto
    when(productoClient.obtenerProductoPorId(any(), eq(compraProductoDto.getIdProducto())))
        .thenReturn(productoDtoOut);

    // validarDisponibilidadProducto
    when(inventarioClient.obtenerInventarioProducto(any(), eq(compraProductoDto.getIdProducto())))
        .thenThrow(feignException);

    ElementoNoEncontradoException ex = assertThrows(ElementoNoEncontradoException.class, () -> {
      compraService.comprarProducto(compraProductoDto);
    });

    assertEquals(mensajeEx, ex.getMessage());

  }

  @Test
  void comprarProductoFallidoInesperadoInventario() {

    String mensajeEx = "Error consultando inventario";
    CompraProductoDto compraProductoDto = new CompraProductoDto();
    compraProductoDto.setIdProducto(1);
    compraProductoDto.setCantidad(10);

    ProductoDtoOut productoDtoOut = new ProductoDtoOut();
    productoDtoOut.setId(compraProductoDto.getIdProducto());
    productoDtoOut.setNombre("Cuaderno 100 hojas");
    productoDtoOut.setPrecio(5000L);

    // Cuerpo JSON de error
    String jsonError = "{\"codigo\":500,\"resultado\":\"ERROR\",\"mensaje\":\"\"Error consultando inventario\"}";

    // Crea un FeignException simulado
    FeignException feignException = mock(FeignException.class);
    when(feignException.contentUTF8()).thenReturn(jsonError);

    // consultaProducto
    when(productoClient.obtenerProductoPorId(any(), eq(compraProductoDto.getIdProducto())))
        .thenReturn(productoDtoOut);

    // validarDisponibilidadProducto
    when(inventarioClient.obtenerInventarioProducto(any(), eq(compraProductoDto.getIdProducto())))
        .thenThrow(feignException);

    ErrorGeneralException ex = assertThrows(ErrorGeneralException.class, () -> {
      compraService.comprarProducto(compraProductoDto);
    });

    assertEquals(mensajeEx, ex.getMessage());

  }

  @Test
  void comprarProductoFallidoActualizaInventario() {
    String mensajeEx = "Inventario no encontrado";
    CompraProductoDto compraProductoDto = new CompraProductoDto();
    compraProductoDto.setIdProducto(1);
    compraProductoDto.setCantidad(10);

    ProductoDtoOut productoDtoOut = new ProductoDtoOut();
    productoDtoOut.setId(compraProductoDto.getIdProducto());
    productoDtoOut.setNombre("Cuaderno 100 hojas");
    productoDtoOut.setPrecio(5000L);

    InventarioDtoOut inventarioDtoOut = new InventarioDtoOut();
    inventarioDtoOut.setCantidad(100);

    // Cuerpo JSON de error
    String jsonError = "{\"codigo\":404,\"resultado\":\"ERROR\",\"mensaje\":\"Inventario no encontrado\"}";

    // Crea un FeignException simulado
    FeignException feignException = mock(FeignException.class);
    when(feignException.contentUTF8()).thenReturn(jsonError);

    // consultaProducto
    when(productoClient.obtenerProductoPorId(any(), eq(compraProductoDto.getIdProducto())))
        .thenReturn(productoDtoOut);

    // validarDisponibilidadProducto
    when(inventarioClient.obtenerInventarioProducto(any(), eq(compraProductoDto.getIdProducto())))
        .thenReturn(inventarioDtoOut);

    // actualizarInventario
    when(inventarioClient.actualizarCantidad(any(), any(ActualizaInventarioDto.class))).thenThrow(feignException);

    ElementoNoEncontradoException ex = assertThrows(ElementoNoEncontradoException.class, () -> {
      compraService.comprarProducto(compraProductoDto);
    });

    assertEquals(mensajeEx, ex.getMessage());
  }

  @Test
  void comprarProductoFallidoInesperadoActualizaInventario() {
    String mensajeEx = "Error inesperado";
    CompraProductoDto compraProductoDto = new CompraProductoDto();
    compraProductoDto.setIdProducto(1);
    compraProductoDto.setCantidad(10);

    ProductoDtoOut productoDtoOut = new ProductoDtoOut();
    productoDtoOut.setId(compraProductoDto.getIdProducto());
    productoDtoOut.setNombre("Cuaderno 100 hojas");
    productoDtoOut.setPrecio(5000L);

    InventarioDtoOut inventarioDtoOut = new InventarioDtoOut();
    inventarioDtoOut.setCantidad(100);

    // Cuerpo JSON de error
    String jsonError = "{\"codigo\":500,\"resultado\":\"ERROR\",\"mensaje\":\"Error inesperado\"}";

    // Crea un FeignException simulado
    FeignException feignException = mock(FeignException.class);
    when(feignException.contentUTF8()).thenReturn(jsonError);

    // consultaProducto
    when(productoClient.obtenerProductoPorId(any(), eq(compraProductoDto.getIdProducto())))
        .thenReturn(productoDtoOut);

    // validarDisponibilidadProducto
    when(inventarioClient.obtenerInventarioProducto(any(), eq(compraProductoDto.getIdProducto())))
        .thenReturn(inventarioDtoOut);

    // actualizarInventario
    when(inventarioClient.actualizarCantidad(any(), any(ActualizaInventarioDto.class))).thenThrow(feignException);

    ErrorGeneralException ex = assertThrows(ErrorGeneralException.class, () -> {
      compraService.comprarProducto(compraProductoDto);
    });

    assertEquals(mensajeEx, ex.getMessage());
  }
}
