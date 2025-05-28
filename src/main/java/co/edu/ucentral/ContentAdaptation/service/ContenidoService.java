package co.edu.ucentral.ContentAdaptation.service;


import co.edu.ucentral.ContentAdaptation.model.ContenidoEducativo;
import co.edu.ucentral.ContentAdaptation.model.PreferenciasUsuario;
import co.edu.ucentral.ContentAdaptation.model.ProgresoUsuario;
import co.edu.ucentral.ContentAdaptation.repositories.ContenidoEducativoRepository;
import co.edu.ucentral.ContentAdaptation.repositories.PreferenciasUsuarioRepository;
import co.edu.ucentral.ContentAdaptation.repositories.ProgresoUsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContenidoService {

    private final ContenidoEducativoRepository contenidoRepository;
    private final PreferenciasUsuarioRepository preferenciasRepository;
    private final ProgresoUsuarioRepository progresoRepository;

    public ContenidoService(ContenidoEducativoRepository contenidoRepository,
                            PreferenciasUsuarioRepository preferenciasRepository,
                            ProgresoUsuarioRepository progresoRepository) {
        this.contenidoRepository = contenidoRepository;
        this.preferenciasRepository = preferenciasRepository;
        this.progresoRepository = progresoRepository;
    }

    /**
     * Consultar contenido personalizado.
     * Fetches content adapted to the user's learning profile.
     * This is a simplified example; real adaptation would be more complex.
     */
    public List<ContenidoEducativo> getContenidoPersonalizado(Long idUsuario) {
        Optional<PreferenciasUsuario> userPrefs = preferenciasRepository.findByUsuarioId(idUsuario);

        if (userPrefs.isPresent()) {
            PreferenciasUsuario preferencias = userPrefs.get();
            // Example personalization logic:
            // 1. Get content based on preferred learning style (cognitive profile)
            List<ContenidoEducativo> adaptedContent = contenidoRepository.findByPerfilCognitivoSugerido(preferencias.getEstiloAprendizajePreferido());

            // 2. Further filter by preferred format
            if (preferencias.getFormatoPreferido() != null && !preferencias.getFormatoPreferido().isEmpty()) {
                adaptedContent = adaptedContent.stream()
                        .filter(c -> preferencias.getFormatoPreferido().equalsIgnoreCase(c.getTipoContenido()))
                        .collect(Collectors.toList());
            }

            // 3. Exclude content already completed by the user
            List<ProgresoUsuario> completedContent = progresoRepository.findByUsuarioId(idUsuario);
            List<String> completedDetails = completedContent.stream()
                    .map(ProgresoUsuario::getDetalle) // Assuming detalle stores content ID or URL
                    .collect(Collectors.toList());

            adaptedContent = adaptedContent.stream()
                    .filter(c -> !completedDetails.contains(String.valueOf(c.getId()))) // Filter by content ID
                    .collect(Collectors.toList());

            // You could also consider 'ritmo_preferido', 'temas_interes' etc.
            return adaptedContent;

        } else {
            // If no preferences, return some default content or highly rated content
            return contenidoRepository.findAll(); // Or a subset
        }
    }

    /**
     * Actualizar preferencias de aprendizaje.
     */
    public PreferenciasUsuario updatePreferencias(Long idUsuario, PreferenciasUsuario newPreferences) {
        return preferenciasRepository.findByUsuarioId(idUsuario)
                .map(existingPrefs -> {
                    existingPrefs.setEstiloAprendizajePreferido(newPreferences.getEstiloAprendizajePreferido());
                    existingPrefs.setRitmoPreferido(newPreferences.getRitmoPreferido());
                    existingPrefs.setFormatoPreferido(newPreferences.getFormatoPreferido());
                    existingPrefs.setTemasInteres(newPreferences.getTemasInteres());
                    return preferenciasRepository.save(existingPrefs);
                })
                .orElseGet(() -> {
                    // Create new preferences if none exist for the user
                    newPreferences.setUsuarioId(idUsuario);
                    return preferenciasRepository.save(newPreferences);
                });
    }

    /**
     * Marcar contenido como completado.
     */
    public ProgresoUsuario marcarContenidoCompletado(Long idUsuario, Long idContenido) {
        if (progresoRepository.existsByUsuarioIdAndDetalle(idUsuario, String.valueOf(idContenido))) {
            throw new RuntimeException("El contenido ya ha sido marcado como completado para este usuario.");
        }

        // Optional: Verify content exists before marking
        contenidoRepository.findById(idContenido)
                .orElseThrow(() -> new RuntimeException("Contenido educativo no encontrado con ID: " + idContenido));

        ProgresoUsuario progreso = new ProgresoUsuario();
        progreso.setUsuarioId(idUsuario);
        progreso.setFecha(LocalDate.now());
        progreso.setProgreso("COMPLETADO");
        progreso.setDetalle(String.valueOf(idContenido)); // Store the content ID as detail

        return progresoRepository.save(progreso);
    }

    /**
     * Recomendar contenido basado en desempeño.
     * This is a very simplified recommendation logic. A real system would use
     * collaborative filtering, content-based filtering, or hybrid approaches.
     */
    public List<ContenidoEducativo> getContenidoRecomendado(Long idUsuario) {
        // For simplicity, let's recommend content based on completed content's tags/difficulty
        List<ProgresoUsuario> completed = progresoRepository.findByUsuarioId(idUsuario);
        if (completed.isEmpty()) {
            return contenidoRepository.findAll(); // Or some default recommendations
        }

        // Get the last completed content to find similar items
        Optional<ContenidoEducativo> lastCompletedContent = Optional.empty();
        if(!completed.isEmpty()){
            // Assuming 'detalle' contains the content ID
            lastCompletedContent = contenidoRepository.findById(Long.parseLong(completed.get(completed.size() - 1).getDetalle()));
        }


        if (lastCompletedContent.isPresent()) {
            ContenidoEducativo referenceContent = lastCompletedContent.get();
            // Recommend content of similar difficulty or tags
            List<ContenidoEducativo> recommendations = contenidoRepository.findByNivelDificultad(referenceContent.getNivelDificultad());

            // Filter out already completed content
            List<String> completedDetails = completed.stream()
                    .map(ProgresoUsuario::getDetalle)
                    .collect(Collectors.toList());

            recommendations = recommendations.stream()
                    .filter(c -> !completedDetails.contains(String.valueOf(c.getId())))
                    .collect(Collectors.toList());

            return recommendations.stream().limit(5).collect(Collectors.toList()); // Limit to 5 recommendations
        } else {
            return contenidoRepository.findAll(); // Fallback if no specific reference
        }
    }
}