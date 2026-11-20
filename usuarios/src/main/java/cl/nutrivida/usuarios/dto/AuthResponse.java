package cl.nutrivida.usuarios.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthResponse {
    private String mensaje;
    private UsuarioResponse usuario;
}
