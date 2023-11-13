package it.dedagroup.venditabiglietti.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private LocalDateTime dataEvento;
    @Column(nullable = false)
    private String descrizione;
    @Column(nullable = false)
    private boolean isCancellato;
    @Version
    private long version;

}