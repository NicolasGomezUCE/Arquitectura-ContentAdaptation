package co.edu.ucentral.ContentAdaptation.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "contenido_educativo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContenidoEducativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", length = 255)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "url_recurso", length = 255)
    private String urlRecurso;

    @Column(name = "tipo_contenido", length = 50)
    private String tipoContenido; // e.g., 'VIDEO', 'ARTICLE', 'QUIZ'

    @Column(name = "nivel_dificultad", length = 50)
    private String nivelDificultad; // e.g., 'BEGINNER', 'INTERMEDIATE'

    @Column(name = "tags", length = 255)
    private String tags; // Comma-separated topics

    @Column(name = "perfil_cognitivo_sugerido", length = 100)
    private String perfilCognitivoSugerido; // e.g., 'VISUAL', 'AUDITORY'
}