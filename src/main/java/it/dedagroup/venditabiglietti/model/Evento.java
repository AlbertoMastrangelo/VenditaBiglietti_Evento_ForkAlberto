package it.dedagroup.venditabiglietti.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private LocalDate data;
    @Column(nullable = false)
    LocalTime ora;
    @Column(nullable = false)
    private String descrizione;
    @Column(nullable = false)
    private boolean isCancellato;
    @Version
    private long version;

}