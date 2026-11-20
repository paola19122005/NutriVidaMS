package cl.nutrivida.usuarios.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioUpdate {
    private String run;
    @Size(max = 50) private String nombre;
    @Size(max = 80) private String apellidos;
    @Email @Size(max = 100) private String correo;
    private String fechaNacimiento;
    private String rol;
    private String region;
    private String comuna;
    private String direccion;
    @Size(min = 4, max = 10) private String clave;
    private Boolean activo;
}
