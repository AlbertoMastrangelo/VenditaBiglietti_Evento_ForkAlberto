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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class EventoServiceImpl implements EventoService {

    @Autowired
    EventoRepository evRepo;

    @Override
    public Evento findByIdAndIsCancellatoFalse(long id) {
        return evRepo.findByIdAndIsCancellatoFalse(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nessun evento con questo id o evento cancellato."));
    }

    @Override
    public List<Evento> findAllByIds(List<Long> ids) {
        if(ids.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nessun id specificato.");
        }
        List<Evento> eventi = evRepo.findAllById(ids).stream().filter(e -> !e.isCancellato()).toList();
        if(eventi.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nessun evento con questi id.");
        }
        return eventi.stream().sorted(Comparator.comparing(Evento::getData).thenComparing(Evento::getOra)).toList();
    }




    @Override
    public Evento findByDescrizioneAndIsCancellatoFalse(String descrizione) {
        if(descrizione.isBlank()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Inserire almeno un carattere, non lasciare stringa vuota");
        }
        return evRepo.findByDescrizioneAndIsCancellatoFalse(descrizione).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nessun evento con questa descrizione o evento cancellato."));
    }




    @Override
    @Transactional(rollbackOn = DataAccessException.class)
    public void salva(Evento ev) {
        for(Evento evento : evRepo.findAll()){
            if(evento.getDescrizione().equals(ev.getDescrizione())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Evento con questa descrizione già esistente.");
            }
        }
        evRepo.save(ev);
    }

    @Override
    @Transactional(rollbackOn = DataAccessException.class)
    public void aggiorna(Evento ev, long id) {
        Evento eventoEsistente = evRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nessun evento con questo id"));
        eventoEsistente.setData(ev.getData());
        eventoEsistente.setOra(ev.getOra());
        for(Evento evento : evRepo.findAll()){
            if (evento.getDescrizione().equalsIgnoreCase(ev.getDescrizione())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Evento con questa descrizione già esistente.");
            }
        }
        eventoEsistente.setDescrizione(ev.getDescrizione());
        evRepo.save(ev);
    }

    @Override
    @Transactional(rollbackOn = DataAccessException.class)
    public void cancella(long id){
        Evento eventoEsistente = evRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nessun evento con questo id"));
        eventoEsistente.setCancellato(true);
        evRepo.save(eventoEsistente);
    }

    @Override
    public List<Evento> findAllByIsCancellato(boolean cancellato) {
        List<Evento> eventiDiBoolean = evRepo.findAllByIsCancellato(cancellato);
        if (eventiDiBoolean.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nessun evento con cancellato settato su " + cancellato);
        }
        return eventiDiBoolean.stream().sorted(Comparator.comparing(Evento::getData).thenComparing(Evento::getOra)).toList();
    }

    /*
    @Override
    public List<Evento> findAllByDataEventoAndIsCancellatoFalse(LocalDate data) {
        List<Evento> eventi = evRepo.findAllByIsCancellato(false);
        List<Evento> eventiInData = new ArrayList<>();
        for(Evento e: eventi){
            if(LocalDate.from(e.getDataEvento()).equals(data)){
                eventiInData.add(e);
            }
        }
        if(eventiInData.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nessun evento in questa data");
        }
        return eventiInData;
    }

     */

    @Override
    public List<Evento> findAllByDataAndIsCancellatoFalse(LocalDate data){
        List<Evento> eventiInData = evRepo.findAllByDataAndIsCancellatoFalse(data);
        if(eventiInData.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nessun evento in questa data");
        }
        return eventiInData.stream().sorted(Comparator.comparing(Evento::getData).thenComparing(Evento::getOra)).toList();
    }

    /*
    @Override
    public Evento findByDataAndDescrizione(LocalDate data, String descrizione) {
        List<Evento> eventi = evRepo.findAllByIsCancellato(false);
        for (Evento e : eventi) {
            if (LocalDate.from(e.getDataEvento()).equals(data) && e.getDescrizione().equalsIgnoreCase(descrizione)) {
                return e;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nessun evento trovato.");

    }
     */

    @Override
    public Evento findByDataAndDescrizioneAndIsCancellatoFalse(LocalDate data, String descrizione) {
        return evRepo.findByDataAndDescrizioneAndIsCancellatoFalse(data, descrizione)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nessun evento con questa data e questa esatta descrizione"));
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
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nessun evento trovato con descrizione contenente " + string);
        }
        else{
            return eventiCheContengonoString.stream().sorted(Comparator.comparing(Evento::getData).thenComparing(Evento::getOra)).toList();
        }
    }

    @Override
    public List<Evento> findEventiFuturi() {
        List<Evento> eventiFuturi = evRepo.findAllByIsCancellato(false).stream()
                .filter(e -> {
                    LocalDateTime dataOraEvento = LocalDateTime.of(e.getData(), e.getOra());
                    LocalDateTime now = LocalDateTime.now();
                    return dataOraEvento.isAfter(now);
                })
                .toList();
        if(eventiFuturi.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nessun evento in programma.");
        }
        return eventiFuturi.stream().sorted(Comparator.comparing(Evento::getData).thenComparing(Evento::getOra)).toList();
    }

    @Override
    public List<Evento> findEventiPassati() {
        List<Evento> eventiPassati = evRepo.findAllByIsCancellato(false).stream()
                .filter(e -> {
                    LocalDateTime dataOraEvento = LocalDateTime.of(e.getData(), e.getOra());
                    LocalDateTime now = LocalDateTime.now();
                    return dataOraEvento.isBefore(now);
                })
                .toList();
        if(eventiPassati.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nessun evento passato.");
        }
        return eventiPassati.stream().sorted(Comparator.comparing(Evento::getData).thenComparing(Evento::getOra)).toList();
    }

    @Override
    public List<Evento> findAllByDataBetweenAndIsCancellatoFalse(LocalDate data1, LocalDate data2) {
        if (data1.isAfter(data2)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La prima data deve essere precedente alla seconda.");
        }
        List<Evento> eventiFraDate = evRepo.findAllByDataBetweenAndIsCancellatoFalse(data1, data2);
        if (eventiFraDate.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nessun evento compreso fra queste due date.");
        } else {
            return eventiFraDate.stream().sorted(Comparator.comparing(Evento::getData).thenComparing(Evento::getOra)).toList();
        }
    }

    @Override
    public List<Evento> findAllByDataAfterAndIsCancellatoFalse(LocalDate data) {
        List<Evento> eventiDopoData = evRepo.findAllByDataAfterAndIsCancellatoFalse(data);
        if(eventiDopoData.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nessun evento dopo data selezionata.");
        }
        return eventiDopoData.stream().sorted(Comparator.comparing(Evento::getData).thenComparing(Evento::getOra)).toList();
    }

    @Override
    public List<Evento> findAllByDataBeforeAndIsCancellatoFalse(LocalDate data) {
        List<Evento> eventiPrimaDiData = evRepo.findAllByDataBeforeAndIsCancellatoFalse(data);
        if(eventiPrimaDiData.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nessun evento prima della data selezionata.");
        }
        return eventiPrimaDiData.stream().sorted(Comparator.comparing(Evento::getData).thenComparing(Evento::getOra)).toList();
    }

}
