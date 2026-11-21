package cl.nutrivida.catalogo_servicios.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ServicioUpdate {
    @Size(max = 40) private String tipo;
    @Size(max = 120) private String nombre;
    @Size(max = 30) private String duracion;
    @Size(max = 40) private String modalidad;
    @Positive private BigDecimal precio;
    @Size(max = 500) private String descripcion;
    @Size(max = 250) private String imagen;
}