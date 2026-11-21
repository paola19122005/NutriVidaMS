package cl.nutrivida.catalogo_servicios.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ServicioResponse {
    private Long id;
    private String tipo;
    private String nombre;
    private String duracion;
    private String modalidad;
    private BigDecimal precio;
    private String descripcion;
    private String imagen;
}