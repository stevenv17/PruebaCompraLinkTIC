package com.linktic.pruebacompralinktic.client;

import com.linktic.pruebacompralinktic.dto.ActualizaInventarioDto;
import com.linktic.pruebacompralinktic.dto.InventarioDtoOut;
import com.linktic.pruebacompralinktic.dto.MensajeOutDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventario-client", url = "${inventario.url}")
public interface InventarioClient {

  @GetMapping("/consultar/{id}")
  InventarioDtoOut obtenerInventarioProducto(@PathVariable("id") Integer id);

  @PutMapping("/actualizar-cantidad")
  MensajeOutDto actualizarCantidad(@RequestBody ActualizaInventarioDto actualizaInventarioDto);

}

