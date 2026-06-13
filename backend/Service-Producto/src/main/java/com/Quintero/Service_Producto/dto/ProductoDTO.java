package com.Quintero.Service_Producto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoDTO {
    private long id;
    private String sku;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Long marcaId;
    private String marcaNombre;

}
