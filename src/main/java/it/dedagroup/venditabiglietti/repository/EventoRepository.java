package it.dedagroup.venditabiglietti.repository;

import it.dedagroup.venditabiglietti.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    Optional<Evento> findByDescrizioneAndIsCancellatoFalse(String descrizione);
    //List<Evento> findAllByDataEventoAndIsCancellatoFalse(LocalDateTime dataEvento);
    List<Evento> findAllByIsCancellato(boolean cancellato);
    Optional<Evento> findByIdAndIsCancellatoFalse(long idEvento);

    List<Evento> findAllByDataAndIsCancellatoFalse(LocalDate data);
    Optional<Evento> findByDataAndDescrizioneAndIsCancellatoFalse(LocalDate data, String descrizione);
    List<Evento> findAllByDataBetweenAndIsCancellatoFalse(LocalDate data1, LocalDate data2);
    List<Evento> findAllByDataAfterAndIsCancellatoFalse(LocalDate data);
    List<Evento> findAllByDataBeforeAndIsCancellatoFalse(LocalDate data);

}
