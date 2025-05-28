package co.edu.ucentral.ContentAdaptation.repositories;

import co.edu.ucentral.ContentAdaptation.model.PreferenciasUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PreferenciasUsuarioRepository extends JpaRepository<PreferenciasUsuario, Long> {
    Optional<PreferenciasUsuario> findByUsuarioId(Long usuarioId);
}