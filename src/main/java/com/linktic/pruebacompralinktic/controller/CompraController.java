package com.linktic.pruebacompralinktic.controller;

import com.linktic.pruebacompralinktic.dto.CompraProductoDto;
import com.linktic.pruebacompralinktic.dto.CompraProductoDtoOut;
import com.linktic.pruebacompralinktic.exception.ElementoNoEncontradoException;
import com.linktic.pruebacompralinktic.exception.ErrorGeneralException;
import com.linktic.pruebacompralinktic.service.ICompraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para gestionar compras
 */
@Slf4j
@RestController
@RequestMapping("/compra")
@RequiredArgsConstructor
public class CompraController {

  private final ICompraService iCompraService;

  /**
   * Compra productos
   *
   * @param compraProductoDto información de entrada para comprar producto
   * @return respuesta con resultado de la compra
   * @throws ElementoNoEncontradoException excepción si no encuentra información
   * @throws ErrorGeneralException excepción si ocurre un error
   */
  @PostMapping(value = "/comprar-producto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CompraProductoDtoOut> comprarProducto(@RequestBody CompraProductoDto compraProductoDto) throws ElementoNoEncontradoException, ErrorGeneralException {
    return ResponseEntity.ok(iCompraService.comprarProducto(compraProductoDto));
  }

}
