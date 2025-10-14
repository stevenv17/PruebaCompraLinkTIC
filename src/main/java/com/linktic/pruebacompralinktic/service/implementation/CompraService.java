package com.linktic.pruebacompralinktic.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linktic.pruebacompralinktic.client.InventarioClient;
import com.linktic.pruebacompralinktic.client.ProductoClient;
import com.linktic.pruebacompralinktic.dto.*;
import com.linktic.pruebacompralinktic.exception.ElementoNoEncontradoException;
import com.linktic.pruebacompralinktic.exception.ErrorGeneralException;
import com.linktic.pruebacompralinktic.service.ICompraService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompraService implements ICompraService {

  @Value("${producto.apikey}")
  private String apiKeyProducto;

  @Value("${inventario.apikey}")
  private String apiKeyInventario;

  private final InventarioClient inventarioClient;
  private final ProductoClient productoClient;

  /**
   * Funcionalidad para comprar producto
   *
   * @param compraProductoDto datos de producto a comprar
   * @return Resultado de la compra
   * @throws ElementoNoEncontradoException excepción si no encuentra información
   * @throws ErrorGeneralException         excepción si ocurre error
   */
  @Override
  public CompraProductoDtoOut comprarProducto(CompraProductoDto compraProductoDto) throws ElementoNoEncontradoException, ErrorGeneralException {
    validarDatosEntrada(compraProductoDto);

    ProductoDtoOut productoDtoOut = consultaProducto(compraProductoDto);

    int cantidadDisponible = validarDisponibilidadProducto(compraProductoDto);
    int nuevaCantidad = cantidadDisponible - compraProductoDto.getCantidad();
    ActualizaInventarioDto actualizaInventarioDto = new ActualizaInventarioDto();
    actualizaInventarioDto.setIdProducto(compraProductoDto.getIdProducto());
    actualizaInventarioDto.setCantidad(nuevaCantidad);
    actualizarInventario(actualizaInventarioDto);

    CompraProductoDtoOut compraProductoDtoOut = new CompraProductoDtoOut();
    compraProductoDtoOut.setProducto(productoDtoOut);
    compraProductoDtoOut.setCantidadComprada(compraProductoDto.getCantidad());
    long precioTotal = productoDtoOut.getPrecio() * compraProductoDto.getCantidad();
    compraProductoDtoOut.setPrecioTotal(precioTotal);

    return compraProductoDtoOut;
  }

  /**
   * Valida campos de entrada
   * @param compraProductoDto objeto con datos de entrada
   * @throws ErrorGeneralException excepción si ocurre un error
   */
  private void validarDatosEntrada(CompraProductoDto compraProductoDto) throws ErrorGeneralException {
    if(compraProductoDto.getIdProducto() == null) {
      throw new ErrorGeneralException("Debe ingresar un identificador de producto");
    }
    if(compraProductoDto.getCantidad() == null || compraProductoDto.getCantidad() <= 0) {
      throw new ErrorGeneralException("La cantidad del producto debe ser mayor que 0");
    }
  }

  /**
   * Valida disponibilidad de producto
   * @param compraProductoDto objeto con datos de entrada
   * @return cantidad disponible
   * @throws ErrorGeneralException excepción si ocurre un error
   */
  private Integer validarDisponibilidadProducto(CompraProductoDto compraProductoDto) throws ErrorGeneralException, ElementoNoEncontradoException {
    InventarioDtoOut inventario;
    String errorGeneral = "Error consultando inventario";
    try {
      inventario = inventarioClient.obtenerInventarioProducto(apiKeyInventario,compraProductoDto.getIdProducto());
    } catch (FeignException e) {
      String json = e.contentUTF8();
      ObjectMapper mapper = new ObjectMapper();
      ErrorResponse error;
      try {
        error = mapper.readValue(json, ErrorResponse.class);
      } catch (Exception ex) {
        throw new ErrorGeneralException(errorGeneral);
      }
      if(error.getCodigo() == 404) {
        throw new ElementoNoEncontradoException(error.getMensaje());
      }
      if(error.getCodigo() == 500) {
        throw new ErrorGeneralException(error.getMensaje());
      }
      throw new ErrorGeneralException(errorGeneral);
    } catch (Exception e) {
      throw new ErrorGeneralException(errorGeneral);
    }

    if(inventario.getCantidad() < compraProductoDto.getCantidad()) {
      throw new ErrorGeneralException(MessageFormat.format(
          "No hay inventario suficiente, actualmente disponemos de {0} unidades",inventario.getCantidad()
      ));
    }
    return inventario.getCantidad();
  }

  /**
   * Actualiza inventario del producto
   * @param actualizaInventarioDto datos para actualizar producto
   * @throws ErrorGeneralException excepción si ocurre error
   */
  private void actualizarInventario(ActualizaInventarioDto actualizaInventarioDto) throws ErrorGeneralException, ElementoNoEncontradoException {
    MensajeOutDto mensajeOutDto;
    String errorGeneral = "Error al actualizar el inventario";
    try {
      mensajeOutDto = inventarioClient.actualizarCantidad(apiKeyInventario, actualizaInventarioDto);
    } catch (FeignException e) {
      String json = e.contentUTF8();
      ObjectMapper mapper = new ObjectMapper();
      ErrorResponse error;
      try {
        error = mapper.readValue(json, ErrorResponse.class);
      } catch (Exception ex) {
        throw new ErrorGeneralException(errorGeneral);
      }
      if(error.getCodigo() == 404) {
        throw new ElementoNoEncontradoException(error.getMensaje());
      }
      if(error.getCodigo() == 500) {
        throw new ErrorGeneralException(error.getMensaje());
      }
      throw new ErrorGeneralException(errorGeneral);
    } catch (Exception e) {
      throw new ErrorGeneralException(errorGeneral);
    }

    if(!mensajeOutDto.getResultado().equals("EXITOSO")) {
      throw new ErrorGeneralException("Error al actualizar el inventario");
    }
  }

  /**
   * Consulta datos del producto
   * @param compraProductoDto datos de entrada para consulta
   * @return datos del producto
   * @throws ErrorGeneralException excepción si ocurre error
   */
  private ProductoDtoOut consultaProducto(CompraProductoDto compraProductoDto) throws ErrorGeneralException, ElementoNoEncontradoException {
    String errorGeneral = "Error consultando producto";
    try {
      return productoClient.obtenerProductoPorId(apiKeyProducto, compraProductoDto.getIdProducto());
    } catch (FeignException e) {
      String json = e.contentUTF8();
      ObjectMapper mapper = new ObjectMapper();
      ErrorResponse error;
      try {
        error = mapper.readValue(json, ErrorResponse.class);
      } catch (Exception ex) {
        throw new ErrorGeneralException(errorGeneral);
      }
      if(error.getCodigo() == 404) {
        throw new ElementoNoEncontradoException(error.getMensaje());
      }
      if(error.getCodigo() == 500) {
        throw new ErrorGeneralException(error.getMensaje());
      }
      throw new ErrorGeneralException(errorGeneral);
    } catch (Exception e) {
      throw new ErrorGeneralException(errorGeneral);
    }
  }
}
