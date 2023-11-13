package it.dedagroup.venditabiglietti.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.dedagroup.venditabiglietti.dto.request.EventoDTORequest;
import it.dedagroup.venditabiglietti.dto.response.BadRequestDTOResponse;
import it.dedagroup.venditabiglietti.model.Evento;
import it.dedagroup.venditabiglietti.service.def.EventoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("evento/")
public class EventoController {

    @Autowired
    EventoService eventoService;

    @Operation(summary = "Trova evento per id", description = "Questo endpoint recupera un singolo evento, che ha l'attributo cancellato settato su false, tramite l'id inserito nell'url.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of interventions of the requested type retrieved successfully.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "404", description = "Nessun evento trovato con questo id.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "Non è stato inserito un numero come id ma un carattere non consentito.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @GetMapping("id/{id}")
    public ResponseEntity<Evento> findById(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findByIdAndIsCancellatoFalse(id));
    }

    @GetMapping("ids")
    public ResponseEntity<List<Evento>> findAllByIds(@RequestBody List<Long> ids){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByIds(ids));
    }

    @PostMapping("salva")
    public ResponseEntity<Void> salva(@Valid @RequestBody EventoDTORequest req){
        Evento evento = new Evento();
        evento.setDataEvento(req.getData());
        evento.setDescrizione(req.getDescrizione());
        eventoService.salva(evento);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("aggiorna/{idEvento}")
    public ResponseEntity<Void> aggiorna(@Valid @RequestBody Evento ev, @PathVariable long idEvento){
        eventoService.aggiorna(ev, idEvento);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("cancella/{idEvento}")
    public ResponseEntity<Void> cancella(@PathVariable long idEvento){
        eventoService.cancella(idEvento);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("trovaPerData")
    public ResponseEntity<List<Evento>> trovaPerData(@RequestParam LocalDateTime data){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByData_EventoAndIsCancellatoFalse(data));
    }

    @PostMapping("trovaPerDescrizione")
    public ResponseEntity<Evento> trovaPerDescrizione(@RequestParam String descrizione){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findByDescrizioneAndIsCancellatoFalse(descrizione));
    }

    @PostMapping("trovaPerCancellato")
    public ResponseEntity<List<Evento>> trovaPerCancellato(@RequestParam boolean cancellato){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByIsCancellato(cancellato));
    }

    @PostMapping("trovaPerDataEDescrizione")
    public ResponseEntity<Evento> trovaPerDataEDescrizione(@Valid @RequestBody EventoDTORequest request){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findByDataEventoAndDescrizione(request.getData(), request.getDescrizione()));
    }

    @PostMapping("trovaEventiCheContengono")
    public ResponseEntity<List<Evento>> trovaEventiCheContengonoInDescrizione(@RequestParam String string){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllEventiCheContengonoInDescrizione(string));
    }

    @PostMapping("trovaEventiFraDueDate")
    public ResponseEntity<List<Evento>> trovaEventiFraDueDate(@RequestParam LocalDateTime data1, @RequestParam LocalDateTime data2){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByDataEventoBetweenAndIsCancellatoFalse(data1, data2));
    }

    @GetMapping("trovaEventiFuturi")
    public ResponseEntity<List<Evento>> eventiFuturi(){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findEventiFuturi());
    }

    @GetMapping("trovaEventiPassati")
    public ResponseEntity<List<Evento>> eventiPassati(){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findEventiPassati());
    }

    //trova eventi prima di data

    //trova eventi dopo di data
}
