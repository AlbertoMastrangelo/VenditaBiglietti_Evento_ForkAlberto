package it.dedagroup.venditabiglietti.service.def;

import it.dedagroup.venditabiglietti.dto.request.FiltraEventoDTORequest;
import it.dedagroup.venditabiglietti.model.Evento;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EventoService {
    List<Evento> findAllByIdLuogo(long idLuogo);
    List<Evento> findAllByIdManifestazioneAndIsCancellatoFalse(long idManifestazione);
    Evento findByIdAndIsCancellatoFalse(long id);
    List<Evento> findAllByIds(List<Long> ids);
    Evento findByDescrizioneAndIsCancellatoFalse(String descrizione);
    //List<Evento> findAllByData_EventoAndIsCancellatoFalse(LocalDateTime dataEvento);
    void salva(Evento ev);
    void aggiorna(Evento ev, long id);
    void cancella(long id);

    List<Evento> findAllByIsCancellato(boolean cancellato);

    List<Evento> findAllByDataAndIsCancellatoFalse(LocalDate data);

    Evento findByDataAndDescrizioneAndIsCancellatoFalse(LocalDate data, String descrizione);
    List<Evento> findAllEventiCheContengonoInDescrizione(String string);
    List<Evento> findEventiFuturi();
    List<Evento> findEventiPassati();
    List<Evento> findAllByDataBetweenAndIsCancellatoFalse(LocalDate data1, LocalDate data2);
    List<Evento> findAllByDataAfterAndIsCancellatoFalse(LocalDate data);
    List<Evento> findAllByDataBeforeAndIsCancellatoFalse(LocalDate data);

    List<Evento> findAllByDataOnwards(LocalDate data);

    List<Evento> filtraEventi(FiltraEventoDTORequest request);
    List<Evento> findAllByIsCancellatoFalse();

}