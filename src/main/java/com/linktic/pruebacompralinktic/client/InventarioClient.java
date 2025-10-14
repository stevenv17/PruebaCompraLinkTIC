package com.linktic.pruebacompralinktic.client;

import com.linktic.pruebacompralinktic.dto.ActualizaInventarioDto;
import com.linktic.pruebacompralinktic.dto.InventarioDtoOut;
import com.linktic.pruebacompralinktic.dto.MensajeOutDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventario-client", url = "${inventario.url}")
public interface InventarioClient {

  @GetMapping("/consultar/{id}")
  InventarioDtoOut obtenerInventarioProducto(@RequestHeader("x-api-key") String apiKey, @PathVariable("id") Integer id);

  @PutMapping("/actualizar-cantidad")
  MensajeOutDto actualizarCantidad(@RequestHeader("x-api-key") String apiKey, @RequestBody ActualizaInventarioDto actualizaInventarioDto);

}

