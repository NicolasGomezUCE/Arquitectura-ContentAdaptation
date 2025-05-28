package co.edu.ucentral.ContentAdaptation.repositories;

import co.edu.ucentral.ContentAdaptation.model.ProgresoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgresoUsuarioRepository extends JpaRepository<ProgresoUsuario, Long> {
    List<ProgresoUsuario> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioIdAndDetalle(Long usuarioId, String detalle); // To check if content is already marked completed
}
