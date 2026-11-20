package cl.nutrivida.usuarios.repository;

import cl.nutrivida.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByRun(String run);
    boolean existsByCorreo(String correo);
    boolean existsByRun(String run);
}
