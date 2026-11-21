package cl.nutrivida.catalogo_servicios;

import cl.nutrivida.catalogo_servicios.client.UsuarioInfo;
import cl.nutrivida.catalogo_servicios.client.UsuariosClient;
import cl.nutrivida.catalogo_servicios.dto.ServicioRequest;
import cl.nutrivida.catalogo_servicios.dto.ServicioUpdate;
import cl.nutrivida.catalogo_servicios.repository.ServicioRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ServicioCrudTests {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private ServicioRepository repo;
    @MockitoBean
    private UsuariosClient usuariosClient;

    @BeforeEach
    void setup() {
        Mockito.when(usuariosClient.validarSesion(anyString())).thenReturn(Optional.empty());
    }

    private String json(Object o) throws Exception {
        return objectMapper.writeValueAsString(o);
    }

    private ServicioRequest newServicio(String nombre) {
        return new ServicioRequest("Consulta", nombre, "30 min", "Presencial",
                new BigDecimal("25000"), "Descripcion de prueba", "/img/test.jpg");
    }

    private Cookie adminCookie() {
        Mockito.when(usuariosClient.validarSesion("admin-session"))
                .thenReturn(Optional.of(new UsuarioInfo("admin@nutrivida.cl", "Administrador")));
        return new Cookie("JSESSIONID", "admin-session");
    }

    private Cookie nutriCookie() {
        Mockito.when(usuariosClient.validarSesion("nutri-session"))
                .thenReturn(Optional.of(new UsuarioInfo("nutri@nutrivida.cl", "Nutricionista")));
        return new Cookie("JSESSIONID", "nutri-session");
    }

    @Test
    void contextLoads() {
    }

    @Test
    void publicListReturns200() throws Exception {
        mockMvc.perform(get("/api/servicios"))
                .andExpect(status().isOk());
    }

    @Test
    void createAsAdmin() throws Exception {
        Cookie c = adminCookie();
        ServicioRequest req = newServicio("Servicio Admin " + System.nanoTime());
        MvcResult res = mockMvc.perform(post("/api/servicios").cookie(c)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value(req.getNombre()))
                .andReturn();
        JsonNode node = objectMapper.readTree(res.getResponse().getContentAsString());
        long id = node.get("id").asLong();
        assertThat(repo.findById(id)).isPresent();
    }

    @Test
    void invalidDataReturns400() throws Exception {
        Cookie c = adminCookie();
        ServicioRequest req = new ServicioRequest("", "", "", "", null, "", "");
        mockMvc.perform(post("/api/servicios").cookie(c)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void notFoundReturns404() throws Exception {
        mockMvc.perform(get("/api/servicios/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAndDelete() throws Exception {
        Cookie c = adminCookie();
        ServicioRequest req = newServicio("ToUpdate " + System.nanoTime());
        MvcResult created = mockMvc.perform(post("/api/servicios").cookie(c)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isOk()).andReturn();
        long id = objectMapper.readTree(created.getResponse().getContentAsString()).get("id").asLong();

        ServicioUpdate upd = new ServicioUpdate();
        upd.setNombre("Actualizado");
        mockMvc.perform(put("/api/servicios/" + id).cookie(c)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(upd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Actualizado"));

        mockMvc.perform(delete("/api/servicios/" + id).cookie(c))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/servicios/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void nutricionistaCannotCreate() throws Exception {
        Cookie c = nutriCookie();
        ServicioRequest req = newServicio("NoNutri " + System.nanoTime());
        mockMvc.perform(post("/api/servicios").cookie(c)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthenticatedCannotCreate() throws Exception {
        ServicioRequest req = newServicio("NoAuth " + System.nanoTime());
        mockMvc.perform(post("/api/servicios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isForbidden());
    }
    @Test
    void initialDataLoaded() {
        assertThat(repo.count()).isGreaterThanOrEqualTo(18);
        assertThat(repo.findAll().stream().anyMatch(s -> "Primera consulta nutricional".equals(s.getNombre()))).isTrue();
    }
}