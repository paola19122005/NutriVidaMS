package cl.nutrivida.catalogo_servicios.service;

import cl.nutrivida.catalogo_servicios.dto.*;
import cl.nutrivida.catalogo_servicios.model.Servicio;
import cl.nutrivida.catalogo_servicios.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioService {

    private final ServicioRepository repo;

    public ServicioService(ServicioRepository repo) {
        this.repo = repo;
    }

    public List<ServicioResponse> findAll(String tipo, String q) {
        List<Servicio> list;
        if (tipo != null && !tipo.isBlank()) {
            list = repo.findByTipoIgnoreCase(tipo);
        } else {
            list = repo.findAll();
        }
        if (q != null && !q.isBlank()) {
            String lower = q.toLowerCase();
            list = list.stream()
                    .filter(s -> (s.getNombre() != null && s.getNombre().toLowerCase().contains(lower))
                            || (s.getDescripcion() != null && s.getDescripcion().toLowerCase().contains(lower)))
                    .collect(Collectors.toList());
        }
        return list.stream().map(this::toResponse).toList();
    }

    public ServicioResponse findById(Long id) {
        Servicio s = repo.findById(id).orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        return toResponse(s);
    }

    public ServicioResponse create(ServicioRequest req) {
        Servicio s = Servicio.builder()
                .tipo(req.getTipo())
                .nombre(req.getNombre())
                .duracion(req.getDuracion())
                .modalidad(req.getModalidad())
                .precio(req.getPrecio())
                .descripcion(req.getDescripcion())
                .imagen(req.getImagen())
                .build();
        return toResponse(repo.save(s));
    }

    public ServicioResponse update(Long id, ServicioUpdate req) {
        Servicio s = repo.findById(id).orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        if (req.getTipo() != null) s.setTipo(req.getTipo());
        if (req.getNombre() != null) s.setNombre(req.getNombre());
        if (req.getDuracion() != null) s.setDuracion(req.getDuracion());
        if (req.getModalidad() != null) s.setModalidad(req.getModalidad());
        if (req.getPrecio() != null) s.setPrecio(req.getPrecio());
        if (req.getDescripcion() != null) s.setDescripcion(req.getDescripcion());
        if (req.getImagen() != null) s.setImagen(req.getImagen());
        return toResponse(repo.save(s));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new RuntimeException("Servicio no encontrado");
        repo.deleteById(id);
    }

    private ServicioResponse toResponse(Servicio s) {
        return ServicioResponse.builder()
                .id(s.getId())
                .tipo(s.getTipo())
                .nombre(s.getNombre())
                .duracion(s.getDuracion())
                .modalidad(s.getModalidad())
                .precio(s.getPrecio())
                .descripcion(s.getDescripcion())
                .imagen(s.getImagen())
                .build();
    }
}