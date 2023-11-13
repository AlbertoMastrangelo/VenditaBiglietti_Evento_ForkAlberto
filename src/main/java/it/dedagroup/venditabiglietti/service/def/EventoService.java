package it.dedagroup.venditabiglietti.service.def;

import it.dedagroup.venditabiglietti.model.Evento;

import java.time.LocalDateTime;
import java.util.List;

public interface EventoService {
    Evento findByIdAndIsCancellatoFalse(long id);
    List<Evento> findAllByIds(List<Long> ids);
    Evento findByDescrizioneAndIsCancellatoFalse(String descrizione);
    List<Evento> findAllByData_EventoAndIsCancellatoFalse(LocalDateTime dataEvento);
    void salva(Evento ev);
    void aggiorna(Evento ev, long id);
    void cancella(long id);

    List<Evento> findAllByIsCancellato(boolean cancellato);
    Evento findByDataEventoAndDescrizione(LocalDateTime dataEvento, String descrizione);
    List<Evento> findAllEventiCheContengonoInDescrizione(String string);
    List<Evento> findEventiFuturi();
    List<Evento> findEventiPassati();
    //List<Evento> findEventiTraDueDate(LocalDateTime data1, LocalDateTime data2);
    List<Evento> findAllByDataEventoBetweenAndIsCancellatoFalse(LocalDateTime data1, LocalDateTime data2);
}