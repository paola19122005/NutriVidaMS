package cl.nutrivida.catalogo_servicios.client;

import java.util.Optional;

public interface UsuariosClient {
    Optional<UsuarioInfo> validarSesion(String sessionId);
}