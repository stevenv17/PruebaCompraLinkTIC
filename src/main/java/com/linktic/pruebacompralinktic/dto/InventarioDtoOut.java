package com.linktic.pruebacompralinktic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventarioDtoOut {
  private ProductoDtoOut producto;
  private Integer cantidad;
}
