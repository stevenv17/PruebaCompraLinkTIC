package com.linktic.pruebacompralinktic.service;

import com.linktic.pruebacompralinktic.dto.CompraProductoDto;
import com.linktic.pruebacompralinktic.dto.CompraProductoDtoOut;
import com.linktic.pruebacompralinktic.exception.ElementoNoEncontradoException;
import com.linktic.pruebacompralinktic.exception.ErrorGeneralException;

/** Interfaz con funcionalidades para gestionar compra de productos */
public interface ICompraService {

  /**
   * Funcionalidad para comprar producto
   *
   * @param compraProductoDto datos de producto a comprar
   * @return Resultado de la compra
   * @throws ElementoNoEncontradoException excepción si no encuentra información
   * @throws ErrorGeneralException excepción si ocurre error
   */
  CompraProductoDtoOut comprarProducto(CompraProductoDto compraProductoDto) throws ElementoNoEncontradoException, ErrorGeneralException;

}
