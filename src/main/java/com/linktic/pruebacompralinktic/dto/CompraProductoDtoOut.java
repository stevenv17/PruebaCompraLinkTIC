package com.linktic.pruebacompralinktic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraProductoDtoOut {
  private Integer idProducto;
  private Integer cantidad;
}
