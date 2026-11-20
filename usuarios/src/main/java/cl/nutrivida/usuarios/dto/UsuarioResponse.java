package cl.nutrivida.usuarios.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioResponse {
    private Long id;
    private String run;
    private String nombre;
    private String apellidos;
    private String correo;
    private String fechaNacimiento;
    private String rol;
    private String region;
    private String comuna;
    private String direccion;
    private Boolean activo;
}
