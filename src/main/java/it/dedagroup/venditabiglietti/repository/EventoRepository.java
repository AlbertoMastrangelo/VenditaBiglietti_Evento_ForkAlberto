package it.dedagroup.venditabiglietti.repository;

import it.dedagroup.venditabiglietti.model.Evento;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findAllByIdLuogoAndIsCancellatoFalseOrderByDataAscOraAsc(long idLuogo);
    List<Evento> findAllByIdManifestazioneAndIsCancellatoFalseOrderByDataAscOraAsc(long idManifestazione);
    Optional<Evento> findByDescrizioneAndIsCancellatoFalse(String descrizione);
    List<Evento> findAllByIsCancellatoOrderByDataAscOraAsc(boolean cancellato);
    Optional<Evento> findByIdAndIsCancellatoFalse(long idEvento);

    List<Evento> findAllByDataAndIsCancellatoFalseOrderByOraAsc(LocalDate data);
    Optional<Evento> findByDataAndDescrizioneAndIsCancellatoFalse(LocalDate data, String descrizione);
    List<Evento> findAllByDataBetweenAndIsCancellatoFalseOrderByDataAscOraAsc(LocalDate data1, LocalDate data2);
    List<Evento> findAllByDataAfterAndIsCancellatoFalseOrderByDataAscOraAsc(LocalDate data);
    List<Evento> findAllByDataBeforeAndIsCancellatoFalseOrderByDataAscOraAsc(LocalDate data);

    @Query("SELECT e FROM Evento e WHERE e.data >= :data AND e.isCancellato = false ORDER BY e.data, e.ora")
    List<Evento> findAllByDataOnwardsOrderByDataAscOraAsc(@Param("data") LocalDate data);
    List<Evento> findAllByIsCancellatoFalseOrderByDataAscOraAsc();
}
