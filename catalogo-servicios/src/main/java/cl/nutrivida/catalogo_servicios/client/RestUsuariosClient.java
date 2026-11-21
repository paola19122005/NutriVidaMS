package cl.nutrivida.catalogo_servicios.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
public class RestUsuariosClient implements UsuariosClient {

    private final String baseUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RestUsuariosClient(@Value("${usuarios.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public Optional<UsuarioInfo> validarSesion(String sessionId) {
        try {
            String body = RestClient.create()
                    .get()
                    .uri(baseUrl + "/api/auth/me")
                    .header("Cookie", "JSESSIONID=" + sessionId)
                    .retrieve()
                    .body(String.class);
            if (body == null) return Optional.empty();
            JsonNode node = objectMapper.readTree(body);
            String correo = node.path("correo").asText("");
            String rol = node.path("rol").asText("");
            if (correo.isBlank() || rol.isBlank()) return Optional.empty();
            return Optional.of(new UsuarioInfo(correo, rol));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}