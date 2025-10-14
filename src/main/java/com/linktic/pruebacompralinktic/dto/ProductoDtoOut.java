package com.linktic.pruebacompralinktic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO con información de salida de producto */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDtoOut {
  private Integer id;
  private String nombre;
  private Long precio;
  private String descripcion;
}
