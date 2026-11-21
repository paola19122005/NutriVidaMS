package cl.nutrivida.catalogo_servicios.repository;

import cl.nutrivida.catalogo_servicios.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    List<Servicio> findByTipoIgnoreCase(String tipo);
    List<Servicio> findByNombreContainingIgnoreCase(String nombre);
}