package com.linktic.pruebacompralinktic.client;

import com.linktic.pruebacompralinktic.dto.ProductoDtoOut;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "producto-client", url = "${producto.url}")
public interface ProductoClient {

  @GetMapping("/consultar/{id}")
  ProductoDtoOut obtenerProductoPorId(@RequestHeader("x-api-key") String apiKey, @PathVariable("id") Integer id);

}

