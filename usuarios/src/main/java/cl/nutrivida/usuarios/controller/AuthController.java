package cl.nutrivida.usuarios.controller;

import cl.nutrivida.usuarios.dto.*;
import cl.nutrivida.usuarios.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService service;
    private final AuthenticationManager authManager;

    public AuthController(UsuarioService service, AuthenticationManager authManager) {
        this.service = service;
        this.authManager = authManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UsuarioRequest req) {
        try {
            UsuarioResponse u = service.create(req);
            return ResponseEntity.ok(new AuthResponse("Registro exitoso", u));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req, HttpServletRequest request) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getCorreo(), req.getClave()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            return ResponseEntity.ok(new AuthResponse("Login exitoso", service.findByCorreo(req.getCorreo())));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout exitoso");
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(401).body("No autenticado");
        }
        try {
            return ResponseEntity.ok(service.findByCorreo(auth.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}