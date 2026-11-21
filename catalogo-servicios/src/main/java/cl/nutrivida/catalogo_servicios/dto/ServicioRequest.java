package cl.nutrivida.catalogo_servicios.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ServicioRequest {
    @NotBlank @Size(max = 40) private String tipo;
    @NotBlank @Size(max = 120) private String nombre;
    @Size(max = 30) private String duracion;
    @NotBlank @Size(max = 40) private String modalidad;
    @NotNull @Positive private BigDecimal precio;
    @NotBlank @Size(max = 500) private String descripcion;
    @NotBlank @Size(max = 250) private String imagen;
}