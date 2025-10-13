package com.linktic.pruebacompralinktic.client;

import com.linktic.pruebacompralinktic.dto.ProductoDtoOut;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "producto-client", url = "${producto.url}")
public interface ProductoClient {

  @GetMapping("/consultar/{id}")
  ProductoDtoOut obtenerProductoPorId(@PathVariable("id") Integer id);

}

