package cl.nutrivida.catalogo_servicios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "servicios")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    @Column(nullable = false, length = 40)
    private String tipo;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String nombre;

    @Size(max = 30)
    @Column(length = 30)
    private String duracion;

    @NotBlank
    @Size(max = 40)
    @Column(nullable = false, length = 40)
    private String modalidad;

    @NotNull
    @Positive
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @NotBlank
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String descripcion;

    @NotBlank
    @Size(max = 250)
    @Column(nullable = false, length = 250)
    private String imagen;
}