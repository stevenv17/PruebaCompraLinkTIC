package com.linktic.pruebacompralinktic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
  private int codigo;
  private String resultado;
  private String mensaje;
}

