package cl.nutrivida.usuarios.config;

import cl.nutrivida.usuarios.model.Usuario;
import cl.nutrivida.usuarios.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.count() > 0) return;
            repo.save(Usuario.builder().run("111111111").nombre("Admin").apellidos("NutriVida")
                    .correo("admin@nutrivida.cl").fechaNacimiento("").rol("Administrador")
                    .region("La Araucanía").comuna("Temuco").direccion("Av. Vitacura 2900")
                    .clave(encoder.encode("Admin1234")).activo(true).build());
            repo.save(Usuario.builder().run("222222222").nombre("Carolina").apellidos("Fuentes M.")
                    .correo("nutricionista@nutrivida.cl").fechaNacimiento("").rol("Nutricionista")
                    .region("La Araucanía").comuna("Temuco").direccion("Av. Vitacura 2900")
                    .clave(encoder.encode("Nutri1234")).activo(true).build());
            repo.save(Usuario.builder().run("333333333").nombre("Maria").apellidos("Gonzales")
                    .correo("maria@nutrivida.cl").fechaNacimiento("2000-01-01").rol("Paciente")
                    .region("La Araucanía").comuna("Temuco").direccion("Av. Vitacura 2900")
                    .clave(encoder.encode("1234")).activo(true).build());
        };
    }
}
