package co.edu.ucentral.ContentAdaptation.controller;

import co.edu.ucentral.ContentAdaptation.model.ContenidoEducativo;
import co.edu.ucentral.ContentAdaptation.model.PreferenciasUsuario;
import co.edu.ucentral.ContentAdaptation.model.ProgresoUsuario;
import co.edu.ucentral.ContentAdaptation.service.ContenidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ContenidoController {

    private final ContenidoService contenidoService;

    public ContenidoController(ContenidoService contenidoService) {
        this.contenidoService = contenidoService;
    }

    // GET /contenido/personalizado/{idUsuario}: Consultar contenido personalizado.
    @GetMapping("/contenido/personalizado/{idUsuario}")
    public ResponseEntity<List<ContenidoEducativo>> getContenidoPersonalizado(@PathVariable Long idUsuario) {
        List<ContenidoEducativo> contenido = contenidoService.getContenidoPersonalizado(idUsuario);
        return new ResponseEntity<>(contenido, HttpStatus.OK);
    }

    // PUT /usuario/{id}/preferencias: Actualizar preferencias de aprendizaje.
    @PutMapping("/usuario/{id}/preferencias")
    public ResponseEntity<PreferenciasUsuario> updatePreferencias(@PathVariable("id") Long idUsuario, @RequestBody PreferenciasUsuario preferencias) {
        PreferenciasUsuario updatedPrefs = contenidoService.updatePreferencias(idUsuario, preferencias);
        return new ResponseEntity<>(updatedPrefs, HttpStatus.OK);
    }

    // POST /contenido/completado: Marcar contenido como completado.
    @PostMapping("/contenido/completado")
    public ResponseEntity<?> marcarContenidoCompletado(@RequestBody Map<String, Long> request) {
        Long idUsuario = request.get("idUsuario");
        Long idContenido = request.get("idContenido");

        if (idUsuario == null || idContenido == null) {
            return new ResponseEntity<>("idUsuario y idContenido son requeridos.", HttpStatus.BAD_REQUEST);
        }

        try {
            ProgresoUsuario progreso = contenidoService.marcarContenidoCompletado(idUsuario, idContenido);
            return new ResponseEntity<>(progreso, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT); // Or BAD_REQUEST
        }
    }

    // GET /contenido/recomendado/{idUsuario}: Obtener recomendaciones adaptadas.
    @GetMapping("/contenido/recomendado/{idUsuario}")
    public ResponseEntity<List<ContenidoEducativo>> getContenidoRecomendado(@PathVariable Long idUsuario) {
        List<ContenidoEducativo> recomendaciones = contenidoService.getContenidoRecomendado(idUsuario);
        return new ResponseEntity<>(recomendaciones, HttpStatus.OK);
    }
}