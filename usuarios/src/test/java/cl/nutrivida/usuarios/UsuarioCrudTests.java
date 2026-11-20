package cl.nutrivida.usuarios;

import cl.nutrivida.usuarios.dto.LoginRequest;
import cl.nutrivida.usuarios.dto.UsuarioRequest;
import cl.nutrivida.usuarios.dto.UsuarioUpdate;
import cl.nutrivida.usuarios.model.Usuario;
import cl.nutrivida.usuarios.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UsuarioCrudTests {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private UsuarioRepository repo;
    @Autowired
    private PasswordEncoder encoder;

    private String json(Object o) throws Exception {
        return objectMapper.writeValueAsString(o);
    }

    private MockHttpSession login(String correo, String clave) throws Exception {
        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/api/auth/login").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new LoginRequest(correo, clave))))
                .andExpect(status().isOk());
        return session;
    }

    private UsuarioRequest newUser(String run, String email) {
        return new UsuarioRequest(run, "Test", "User", email, "2000-01-01",
                "Paciente", "Region", "Comuna", "Dir", "Pass1234", true);
    }

    @Test
    void registerStoresBcrypt() throws Exception {
        String run = "RUN" + System.nanoTime();
        String email = "reg" + System.nanoTime() + "@nutrivida.cl";
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(newUser(run, email))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuario.correo").value(email));

        Usuario u = repo.findByCorreo(email).orElseThrow();
        assertThat(u.getClave()).isNotEqualTo("Pass1234");
        assertThat(encoder.matches("Pass1234", u.getClave())).isTrue();
    }

    @Test
    void loginAndLogout() throws Exception {
        String run = "LOG" + System.nanoTime();
        String email = "log" + System.nanoTime() + "@nutrivida.cl";
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(newUser(run, email))))
                .andExpect(status().isOk());

        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/api/auth/login").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new LoginRequest(email, "Pass1234"))))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/auth/me").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value(email));

        mockMvc.perform(post("/api/auth/logout").session(session))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/auth/me").session(session))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void invalidDataReturns400() throws Exception {
        UsuarioRequest req = new UsuarioRequest("", "", "", "bad-email", null, "", null, null, null, "12", null);
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void duplicateRunReturns400() throws Exception {
        String run = "DUP" + System.nanoTime();
        String email1 = "dup1" + System.nanoTime() + "@nutrivida.cl";
        String email2 = "dup2" + System.nanoTime() + "@nutrivida.cl";
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(newUser(run, email1))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(newUser(run, email2))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crudOperations() throws Exception {
        MockHttpSession admin = login("admin@nutrivida.cl", "Admin1234");
        String run = "CRUD" + System.nanoTime();
        String email = "crud" + System.nanoTime() + "@nutrivida.cl";
        UsuarioRequest req = newUser(run, email);

        MvcResult created = mockMvc.perform(post("/api/usuarios").session(admin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(req)))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode node = objectMapper.readTree(created.getResponse().getContentAsString());
        long id = node.get("id").asLong();

        mockMvc.perform(get("/api/usuarios/" + id).session(admin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value(email));

        UsuarioUpdate upd = new UsuarioUpdate();
        upd.setNombre("NuevoNombre");
        mockMvc.perform(put("/api/usuarios/" + id).session(admin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(upd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("NuevoNombre"));

        mockMvc.perform(delete("/api/usuarios/" + id).session(admin))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/usuarios/" + id).session(admin))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateDuplicateRunReturns400() throws Exception {
        MockHttpSession admin = login("admin@nutrivida.cl", "Admin1234");
        String run1 = "U1" + System.nanoTime();
        String run2 = "U2" + System.nanoTime();
        String email1 = "u1" + System.nanoTime() + "@nutrivida.cl";
        String email2 = "u2" + System.nanoTime() + "@nutrivida.cl";

        MvcResult r1 = mockMvc.perform(post("/api/usuarios").session(admin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(newUser(run1, email1))))
                .andExpect(status().isOk()).andReturn();
        long id1 = objectMapper.readTree(r1.getResponse().getContentAsString()).get("id").asLong();

        MvcResult r2 = mockMvc.perform(post("/api/usuarios").session(admin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(newUser(run2, email2))))
                .andExpect(status().isOk()).andReturn();
        long id2 = objectMapper.readTree(r2.getResponse().getContentAsString()).get("id").asLong();

        assertNotEquals(id1, id2);

        UsuarioUpdate upd = new UsuarioUpdate();
        upd.setRun(run1);
        mockMvc.perform(put("/api/usuarios/" + id2).session(admin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(upd)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void nutricionistaCanListButNotCreate() throws Exception {
        MockHttpSession nutri = login("nutricionista@nutrivida.cl", "Nutri1234");
        mockMvc.perform(get("/api/usuarios").session(nutri))
                .andExpect(status().isOk());

        String run = "N" + System.nanoTime();
        String email = "n" + System.nanoTime() + "@nutrivida.cl";
        mockMvc.perform(post("/api/usuarios").session(nutri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(newUser(run, email))))
                .andExpect(status().isForbidden());
    }

    @Test
    void pacienteCannotList() throws Exception {
        MockHttpSession pac = login("maria@nutrivida.cl", "1234");
        mockMvc.perform(get("/api/usuarios").session(pac))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthenticatedDenied() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isForbidden());
    }
}
