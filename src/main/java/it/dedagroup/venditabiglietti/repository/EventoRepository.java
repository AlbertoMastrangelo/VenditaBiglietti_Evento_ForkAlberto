package it.dedagroup.venditabiglietti.repository;

import it.dedagroup.venditabiglietti.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    Optional<Evento> findByDescrizioneAndIsCancellatoFalse(String descrizione);
    List<Evento> findAllByDataEventoAndIsCancellatoFalse(LocalDateTime dataEvento);
    List<Evento> findAllByIsCancellato(boolean cancellato);
    Optional<Evento> findByIdAndIsCancellatoFalse(long idEvento);
    Optional<Evento> findByDataEventoAndDescrizioneAndIsCancellatoFalse(LocalDateTime dataEvento, String descrizione);
    List<Evento> findAllByDataEventoBetweenAndIsCancellatoFalse(LocalDateTime data1, LocalDateTime data2);

}
