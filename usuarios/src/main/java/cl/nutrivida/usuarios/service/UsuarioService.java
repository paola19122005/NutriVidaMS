package cl.nutrivida.usuarios.service;

import cl.nutrivida.usuarios.dto.*;
import cl.nutrivida.usuarios.model.Usuario;
import cl.nutrivida.usuarios.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public List<UsuarioResponse> findAll() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    public UsuarioResponse findById(Long id) {
        Usuario u = repo.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return toResponse(u);
    }

    public UsuarioResponse findByCorreo(String correo) {
        Usuario u = repo.findByCorreo(correo).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return toResponse(u);
    }

    public UsuarioResponse create(UsuarioRequest req) {
        if (repo.existsByCorreo(req.getCorreo())) throw new RuntimeException("Correo ya registrado");
        if (repo.existsByRun(req.getRun())) throw new RuntimeException("RUN ya registrado");
        Usuario u = Usuario.builder()
                .run(req.getRun())
                .nombre(req.getNombre())
                .apellidos(req.getApellidos())
                .correo(req.getCorreo())
                .fechaNacimiento(req.getFechaNacimiento())
                .rol(req.getRol())
                .region(req.getRegion())
                .comuna(req.getComuna())
                .direccion(req.getDireccion())
                .clave(encoder.encode(req.getClave()))
                .activo(req.getActivo() != null ? req.getActivo() : true)
                .build();
        return toResponse(repo.save(u));
    }

    public UsuarioResponse update(Long id, UsuarioUpdate req) {
        Usuario u = repo.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (req.getRun() != null) {
            if (!req.getRun().equals(u.getRun()) && repo.existsByRun(req.getRun()))
                throw new RuntimeException("RUN ya registrado");
            u.setRun(req.getRun());
        }
        if (req.getNombre() != null) u.setNombre(req.getNombre());
        if (req.getApellidos() != null) u.setApellidos(req.getApellidos());
        if (req.getCorreo() != null) {
            if (!req.getCorreo().equalsIgnoreCase(u.getCorreo()) && repo.existsByCorreo(req.getCorreo()))
                throw new RuntimeException("Correo ya registrado");
            u.setCorreo(req.getCorreo());
        }
        if (req.getFechaNacimiento() != null) u.setFechaNacimiento(req.getFechaNacimiento());
        if (req.getRol() != null) u.setRol(req.getRol());
        if (req.getRegion() != null) u.setRegion(req.getRegion());
        if (req.getComuna() != null) u.setComuna(req.getComuna());
        if (req.getDireccion() != null) u.setDireccion(req.getDireccion());
        if (req.getClave() != null && !req.getClave().isBlank()) u.setClave(encoder.encode(req.getClave()));
        if (req.getActivo() != null) u.setActivo(req.getActivo());
        return toResponse(repo.save(u));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new RuntimeException("Usuario no encontrado");
        repo.deleteById(id);
    }

    public Usuario authenticate(String correo, String clave) {
        Usuario u = repo.findByCorreo(correo).orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
        if (u.getActivo() == null || !u.getActivo()) throw new RuntimeException("Usuario inactivo");
        if (!encoder.matches(clave, u.getClave())) throw new RuntimeException("Credenciales inválidas");
        return u;
    }

    private UsuarioResponse toResponse(Usuario u) {
        return UsuarioResponse.builder()
                .id(u.getId())
                .run(u.getRun())
                .nombre(u.getNombre())
                .apellidos(u.getApellidos())
                .correo(u.getCorreo())
                .fechaNacimiento(u.getFechaNacimiento())
                .rol(u.getRol())
                .region(u.getRegion())
                .comuna(u.getComuna())
                .direccion(u.getDireccion())
                .activo(u.getActivo())
                .build();
    }
}