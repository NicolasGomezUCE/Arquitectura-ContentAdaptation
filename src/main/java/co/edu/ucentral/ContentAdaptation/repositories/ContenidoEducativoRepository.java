package co.edu.ucentral.ContentAdaptation.repositories;

import co.edu.ucentral.ContentAdaptation.model.ContenidoEducativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContenidoEducativoRepository extends JpaRepository<ContenidoEducativo, Long> {
    // Custom query methods for content filtering
    List<ContenidoEducativo> findByNivelDificultad(String nivel);
    List<ContenidoEducativo> findByTipoContenido(String tipo);
    List<ContenidoEducativo> findByTagsContaining(String tag);
    List<ContenidoEducativo> findByPerfilCognitivoSugerido(String perfil);
}