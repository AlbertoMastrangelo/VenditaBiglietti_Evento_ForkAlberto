package it.dedagroup.venditabiglietti.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.dedagroup.venditabiglietti.dto.request.EventoDTORequest;
import it.dedagroup.venditabiglietti.dto.request.FiltraEventoDTORequest;
import it.dedagroup.venditabiglietti.dto.response.BadRequestDTOResponse;
import it.dedagroup.venditabiglietti.model.Evento;
import it.dedagroup.venditabiglietti.service.def.EventoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/evento")
public class EventoController {

    @Autowired
    EventoService eventoService;
    @GetMapping("/trovaEventiInLuogo/{idLuogo}")
    public ResponseEntity<List<Evento>> trovaEventiInLuogo(@PathVariable @Min(value = 1, message = "l'id deve essere almeno 1") long idLuogo){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByIdLuogo(idLuogo));
    }

    @GetMapping("/trovaEventiDiManifestazione/{idManifestazione}")
    public ResponseEntity<List<Evento>> trovaEventiDiManifestazione(@PathVariable @Min(value = 1, message = "l'id deve essere almeno 1") long idManifestazione){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByIdManifestazioneAndIsCancellatoFalse(idManifestazione));
    }

    @Operation(summary = "Trova evento per id", description = "Questo endpoint recupera un singolo evento, che ha l'attributo cancellato settato su false, tramite l'id inserito nell'url.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of interventions of the requested type retrieved successfully.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "404", description = "Nessun evento trovato con questo id.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "Non è stato inserito un numero come id ma un carattere non consentito.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<Evento> findById(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findByIdAndIsCancellatoFalse(id));
    }

    @Operation(summary = "Trova eventi tramite id specificati", description = "Questo endpoint recupera uno o più eventi, che hanno l'attributo cancellato settato su false, a cui corrispondono gli id inviati tramite lista in request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of interventions of the requested type retrieved successfully.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "406", description = "Nessun evento trovato con questo id.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "Non è stato inserito alcun numero oppure è presente un id con formato non valido.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @GetMapping("/ids")
    public ResponseEntity<List<Evento>> findAllByIds(@RequestBody List<Long> ids){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByIds(ids));
    }

    @Operation(summary = "Aggiungi evento", description = "Questo endpoint permette l'aggiunta di un nuovo evento, a meno che la descrizione inserita non sia già associata")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of interventions of the requested type retrieved successfully.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "403", description = "Evento con questa descrizione già esistente.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "Uno o più dei campi richiesti è stato lasciato vuoto. ",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @PostMapping("/salva")
    public ResponseEntity<Void> salva(@Valid @RequestBody EventoDTORequest req){
        Evento evento = new Evento();
        evento.setData(req.getData());
        evento.setOra(req.getOra());
        evento.setDescrizione(req.getDescrizione());
        evento.setIdLuogo(req.getIdLuogo());
        evento.setIdManifestazione(req.getIdManifestazione());
        eventoService.salva(evento);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/aggiorna/{idEvento}")
    public ResponseEntity<Void> aggiorna(@Valid @RequestBody EventoDTORequest req, @PathVariable long idEvento){
        Evento evento = new Evento();
        evento.setData(req.getData());
        evento.setOra(req.getOra());
        evento.setDescrizione(req.getDescrizione());
        evento.setIdLuogo(req.getIdLuogo());
        evento.setIdManifestazione(req.getIdManifestazione());
        eventoService.aggiorna(evento, idEvento);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/cancella/{idEvento}")
    public ResponseEntity<Void> cancella(@PathVariable long idEvento){
        eventoService.cancella(idEvento);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/trovaPerData")
    public ResponseEntity<List<Evento>> trovaPerData(@RequestParam LocalDate data){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByDataAndIsCancellatoFalse(data));
    }

    @PostMapping("/trovaPerDescrizione")
    public ResponseEntity<Evento> trovaPerDescrizione(@RequestParam String descrizione){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findByDescrizioneAndIsCancellatoFalse(descrizione));
    }

    @PostMapping("/trovaTuttiConCancellatoSu")
    public ResponseEntity<List<Evento>> trovaTuttiConCancellatoSu(@RequestParam boolean cancellato){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByIsCancellato(cancellato));
    }

    @PostMapping("/trovaPerDataEDescrizione")
    public ResponseEntity<Evento> trovaPerDataEDescrizione(@RequestParam LocalDate data, @RequestParam String descrizione){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findByDataAndDescrizioneAndIsCancellatoFalse(data, descrizione));
    }

    @PostMapping("/trovaEventiCheContengono")
    public ResponseEntity<List<Evento>> trovaEventiCheContengonoInDescrizione(@RequestParam String string){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllEventiCheContengonoInDescrizione(string));
    }

    @PostMapping("/trovaEventiFraDueDate")
    public ResponseEntity<List<Evento>> trovaEventiFraDueDate(@RequestParam LocalDate data1, @RequestParam LocalDate data2){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByDataBetweenAndIsCancellatoFalse(data1, data2));
    }

    @GetMapping("/trovaEventiFuturi")
    public ResponseEntity<List<Evento>> eventiFuturi(){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findEventiFuturi());
    }

    @GetMapping("/trovaEventiPassati")
    public ResponseEntity<List<Evento>> eventiPassati(){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findEventiPassati());
    }

    @GetMapping("/trovaEventiDopoData")
    public ResponseEntity<List<Evento>> eventiDopoData(@RequestParam LocalDate data){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByDataAfterAndIsCancellatoFalse(data));
    }

    @GetMapping("/trovaEventiPrimaDiData")
    public ResponseEntity<List<Evento>> eventiPrimaDiData(@RequestParam LocalDate data){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByDataBeforeAndIsCancellatoFalse(data));
    }

    @GetMapping("/trovaEventiConDataDa")
    public ResponseEntity<List<Evento>> eventiConDataDa(@RequestParam LocalDate data){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByDataOnwards(data));
    }

    @GetMapping("/filtraEventi")
    public ResponseEntity<List<Evento>> filtraEventi(@RequestBody FiltraEventoDTORequest request){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.filtraEventi(request));
    }
}
