package it.dedagroup.venditabiglietti.service.impl;

import it.dedagroup.venditabiglietti.repository.EventoRepository;
import it.dedagroup.venditabiglietti.model.Evento;
import it.dedagroup.venditabiglietti.service.def.EventoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class EventoServiceImpl implements EventoService {

    @Autowired
    EventoRepository evRepo;

    @Override
    public Evento findByIdAndIsCancellatoFalse(long id) {
        return evRepo.findByIdAndIsCancellatoFalse(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun evento con questo id o evento cancellato."));
    }

    @Override
    public List<Evento> findAllByIds(List<Long> ids) {
        List<Evento> eventi = evRepo.findAllById(ids).stream().filter(e -> !e.isCancellato()).toList();
        if(eventi.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun evento con questi id.");
        }
        return eventi;
    }

    @Override
    public Evento findByDescrizioneAndIsCancellatoFalse(String descrizione) {
        return evRepo.findByDescrizioneAndIsCancellatoFalse(descrizione).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun evento con questa descrizione o evento cancellato."));
    }

    @Override
    public List<Evento> findAllByData_EventoAndIsCancellatoFalse(LocalDateTime dataEvento) {
        if(evRepo.findAllByDataEventoAndIsCancellatoFalse(dataEvento).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun evento in questa data e ora o evento cancellato.");
        }
        return evRepo.findAllByDataEventoAndIsCancellatoFalse(dataEvento);
    }

    @Override
    @Transactional(rollbackOn = DataAccessException.class)
    public void salva(Evento ev) {
        for(Evento evento : evRepo.findAll()){
            if(evento.getDescrizione().equals(ev.getDescrizione())){
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Evento con questa descrizione già esistente");
            }
        }
        evRepo.save(ev);
    }

    @Override
    @Transactional(rollbackOn = DataAccessException.class)
    public void aggiorna(Evento ev, long id) {
        Evento eventoEsistente = evRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun evento con questo id"));
        eventoEsistente.setDataEvento(ev.getDataEvento());
        eventoEsistente.setDescrizione(ev.getDescrizione());
        evRepo.save(ev);
    }

    @Override
    @Transactional(rollbackOn = DataAccessException.class)
    public void cancella(long id){
        Evento eventoEsistente = evRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun evento con questo id"));
        eventoEsistente.setCancellato(true);
        evRepo.save(eventoEsistente);
    }

    @Override
    public List<Evento> findAllByIsCancellato(boolean cancellato) {
        List<Evento> eventiDiBoolean = evRepo.findAllByIsCancellato(cancellato);
        if (eventiDiBoolean.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun evento con cancellato settato su " + cancellato);
        }
        return eventiDiBoolean;
    }

    @Override
    public Evento findByDataEventoAndDescrizione(LocalDateTime dataEvento, String descrizione) {
        return evRepo.findByDataEventoAndDescrizioneAndIsCancellatoFalse(dataEvento, descrizione).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun evento in questa data e con questa descrizione"));
    }

    @Override
    public List<Evento> findAllEventiCheContengonoInDescrizione(String string) {
        List<Evento> eventiCheContengonoString = new ArrayList<>();
        for(Evento ev :evRepo.findAll().stream().filter(e->!e.isCancellato()).toList()){
            if(ev.getDescrizione().toLowerCase().contains(string.toLowerCase())){
                eventiCheContengonoString.add(ev);
            }
        }
        if(eventiCheContengonoString.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun evento trovato con descrizione contenente " + string);
        }
        else{
            return eventiCheContengonoString;
        }
    }

    @Override
    public List<Evento> findEventiFuturi() {
        List<Evento> eventiFuturi = evRepo.findAllByIsCancellato(false).stream().filter(e->e.getDataEvento().isAfter(LocalDateTime.now())).toList();
        if(eventiFuturi.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun evento in programma.");
        }
        return eventiFuturi.stream().sorted(Comparator.comparing(Evento::getDataEvento)).toList();
    }

    @Override
    public List<Evento> findEventiPassati() {
        List<Evento> eventiPassati = evRepo.findAllByIsCancellato(false).stream().filter(e->e.getDataEvento().isBefore(LocalDateTime.now())).toList();
        if(eventiPassati.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun evento passato.");
        }
        return eventiPassati.stream().sorted(Comparator.comparing(Evento::getDataEvento)).toList();
    }

    @Override
    public List<Evento> findAllByDataEventoBetweenAndIsCancellatoFalse(LocalDateTime data1, LocalDateTime data2) {
        if (data1.isAfter(data2)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "La prima data deve essere precedente alla seconda.");
        }
        List<Evento> eventiFraDate = evRepo.findAllByDataEventoBetweenAndIsCancellatoFalse(data1, data2);
        if (eventiFraDate.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun evento compreso fra queste due date.");
        } else {
            return eventiFraDate.stream().sorted(Comparator.comparing(Evento::getDataEvento)).toList();
        }
    }

    /*
    @Override
    public List<Evento> findEventiTraDueDate(LocalDateTime data1, LocalDateTime data2) {
        List<Evento> eventi = evRepo.findAllByIsCancellato(false);
        List<Evento> eventiCompresiFraDate = new ArrayList<>();
        if(data1.isAfter(data2)){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "La prima data deve essere precedente alla seconda");
        }
        for(Evento e : eventi){
            if(e.getDataEvento().isAfter(data1) && e.getDataEvento().isBefore(data2)){
                eventiCompresiFraDate.add(e);
            }
        }
        if(eventiCompresiFraDate.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun evento compreso fra queste due date.");
        }
        else{
            return eventiCompresiFraDate;
        }


    }
    */
}
