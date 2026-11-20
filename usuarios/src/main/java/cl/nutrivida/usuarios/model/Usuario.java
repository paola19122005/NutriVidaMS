package cl.nutrivida.usuarios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true, length = 20)
    private String run;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank
    @Size(max = 80)
    @Column(nullable = false, length = 80)
    private String apellidos;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @Column(length = 20)
    private String fechaNacimiento;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String rol;

    @Column(length = 60)
    private String region;

    @Column(length = 60)
    private String comuna;

    @Column(length = 120)
    private String direccion;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String clave;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
}
