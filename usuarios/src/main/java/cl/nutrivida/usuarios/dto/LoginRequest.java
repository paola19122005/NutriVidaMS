package cl.nutrivida.usuarios.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginRequest {
    @NotBlank @Email private String correo;
    @NotBlank private String clave;
}
