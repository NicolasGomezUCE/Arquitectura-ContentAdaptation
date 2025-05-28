package co.edu.ucentral.ContentAdaptation.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "preferencias_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreferenciasUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId; // No @ManyToOne here yet as Usuarios is in another service

    @Column(name = "estilo_aprendizaje_preferido", length = 100)
    private String estiloAprendizajePreferido;

    @Column(name = "ritmo_preferido", length = 50)
    private String ritmoPreferido;

    @Column(name = "formato_preferido", length = 50)
    private String formatoPreferido;

    @Column(name = "temas_interes", columnDefinition = "TEXT")
    private String temasInteres; // Comma-separated list of topics
}