package com.linktic.pruebacompralinktic.service.implementation;

import com.linktic.pruebacompralinktic.dto.CompraProductoDto;
import com.linktic.pruebacompralinktic.dto.CompraProductoDtoOut;
import com.linktic.pruebacompralinktic.exception.ElementoNoEncontradoException;
import com.linktic.pruebacompralinktic.exception.ErrorGeneralException;
import com.linktic.pruebacompralinktic.service.ICompraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompraService implements ICompraService {
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
    return null;
  }
}
