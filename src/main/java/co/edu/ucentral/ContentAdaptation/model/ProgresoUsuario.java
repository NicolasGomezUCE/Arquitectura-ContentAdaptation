package co.edu.ucentral.ContentAdaptation.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "progreso_usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgresoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId; // No @ManyToOne for Usuarios table

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "progreso", length = 100)
    private String progreso; // e.g., "COMPLETED", "IN_PROGRESS"

    @Column(name = "detalle", columnDefinition = "TEXT")
    private String detalle; // e.g., ID of the content completed
}