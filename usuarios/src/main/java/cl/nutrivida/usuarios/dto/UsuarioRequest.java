package cl.nutrivida.usuarios.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioRequest {
    @NotBlank private String run;
    @NotBlank @Size(max = 50) private String nombre;
    @NotBlank @Size(max = 80) private String apellidos;
    @NotBlank @Email @Size(max = 100) private String correo;
    private String fechaNacimiento;
    @NotBlank private String rol;
    private String region;
    private String comuna;
    private String direccion;
    @NotBlank @Size(min = 4, max = 10) private String clave;
    @Builder.Default
    private Boolean activo = true;
}
