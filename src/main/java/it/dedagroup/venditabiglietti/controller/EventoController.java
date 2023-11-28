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
            @ApiResponse(responseCode = "200", description = "Lista eventi a cui corrispondono uno o più degli id recuperata correttamente.",
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

    @Operation(summary = "Aggiungi evento", description = "Questo endpoint permette l'aggiunta di un nuovo evento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento aggiunto con successo.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "403", description = "Evento con questa descrizione già esistente.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "Uno o più dei campi richiesti è stato lasciato vuoto. ",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "406", description = "Impossibile creare evento, luogo già occupato in tale data.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "409", description = "Impossibile creare evento. Esiste già un evento per questa manifestazione in questa data.",
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

    @Operation(summary = "Aggiorna evento", description = "Questo endpoint permette di aggiornare un evento, recuperato tramite id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento aggiunto con successo.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "403", description = "Evento con questa descrizione già esistente.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "Uno o più dei campi richiesti è stato lasciato vuoto.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "406", description = "Impossibile creare evento, luogo già occupato in tale data.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "406", description = "Nessun evento trovato tramite l'id immesso.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "409", description = "Impossibile creare evento. Esiste già un evento per questa manifestazione in questa data.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
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

    @Operation(summary = "Elimina evento", description = "Questo endpoint permette di cancellare un evento, rendendolo non visibile e recuperabile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento cancellato con successo.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "406", description = "Nessun evento trovato tramite l'id immesso.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @PostMapping("/cancella/{idEvento}")
    public ResponseEntity<Void> cancella(@PathVariable long idEvento){
        eventoService.cancella(idEvento);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Trova eventi per data", description = "Questo endpoint permette di trovare tutti gli eventi che avvengono in una specifica data, se esistenti.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista eventi in data recuperata con successo.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "406", description = "Nessun evento trovato nella data specificata.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @GetMapping("/trovaPerData")
    public ResponseEntity<List<Evento>> trovaPerData(@RequestParam LocalDate data){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByDataAndIsCancellatoFalse(data));
    }

    @Operation(summary = "Trova eventi per descrizione esatta", description = "Questo endpoint permette di trovare l'evento che ha, come descrizione, esattamente la stringa immessa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento trovato con successo",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "403", description = "Nessun carattere immesso come valore, inserirne almeno uno.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "406", description = "Nessun evento trovato nella con questa esatta descrizione.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @PostMapping("/trovaPerDescrizione")
    public ResponseEntity<Evento> trovaPerDescrizione(@RequestParam String descrizione){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findByDescrizioneAndIsCancellatoFalse(descrizione));
    }

    @Operation(summary = "Trova eventi con visibilità impostata su true o false", description = "Questo endpoint permette di trovare gli eventi in base alla loro visibilità (isCancellato su false o true).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento trovato con successo",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "406", description = "Nessun evento trovato con visibilità impostata sul valore immesso.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @PostMapping("/trovaTuttiConCancellatoSu")
    public ResponseEntity<List<Evento>> trovaTuttiConCancellatoSu(@RequestParam boolean cancellato){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByIsCancellato(cancellato));
    }

    @Operation(summary = "Trova eventi con questa esatta data e descrizione.", description = "Questo endpoint permette di trovare gli eventi che hanno, come data e descrizione, esattamente i valori immessi per i rispettivi campi.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento trovato con successo",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "406", description = "Nessun evento con questa data e questa esatta descrizione.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @PostMapping("/trovaPerDataEDescrizione")
    public ResponseEntity<Evento> trovaPerDataEDescrizione(@RequestParam LocalDate data, @RequestParam String descrizione){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findByDataAndDescrizioneAndIsCancellatoFalse(data, descrizione));
    }

    @Operation(summary = "Trova eventi con questa esatta data e descrizione.", description = "Questo endpoint permette di trovare gli eventi che, come descrizione, contengono la stringa immessa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eventi contenenti la stringa recuperati correttamente.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "406", description = "Nessun evento trovato contenente la stringa immessa.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @PostMapping("/trovaEventiCheContengono")
    public ResponseEntity<List<Evento>> trovaEventiCheContengonoInDescrizione(@RequestParam String string){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllEventiCheContengonoInDescrizione(string));
    }

    @Operation(summary = "Trova eventi compresi fra due date.", description = "Questo endpoint permette di trovare gli eventi compresi fra due date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eventi compresi fra le due date recuperati correttamente.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "406", description = "Nessun evento compreso fra queste due date.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "403", description = "Errore in input: la prima data deve essere precedente alla seconda.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @PostMapping("/trovaEventiFraDueDate")
    public ResponseEntity<List<Evento>> trovaEventiFraDueDate(@RequestParam LocalDate data1, @RequestParam LocalDate data2){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByDataBetweenAndIsCancellatoFalse(data1, data2));
    }

    @Operation(summary = "Trova eventi con data successiva a quella di invio della richiesta.", description = "Questo endpoint permette di trovare gli eventi che hanno data successiva a quella attuale.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eventi futuri recuperati correttamente.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "406", description = "Nessun evento trovato con data futura.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @GetMapping("/trovaEventiFuturi")
    public ResponseEntity<List<Evento>> eventiFuturi(){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findEventiFuturi());
    }

    @Operation(summary = "Trova eventi con data precedente a quella di invio della richiesta.", description = "Questo endpoint permette di trovare gli eventi che hanno data precedente a quella attuale.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eventi passati recuperati correttamente.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "406", description = "Nessun evento trovato con data passata.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @GetMapping("/trovaEventiPassati")
    public ResponseEntity<List<Evento>> eventiPassati(){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findEventiPassati());
    }

    @Operation(summary = "Trova eventi con data successiva a quella immessa.", description = "Questo endpoint permette di inserire una data e di trovare solo gli eventi che hanno data successiva a quella immessa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eventi con data successiva a quella immessa recuperati correttamente.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "406", description = "Nessun evento trovato con data successiva a quella immessa.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @GetMapping("/trovaEventiDopoData")
    public ResponseEntity<List<Evento>> eventiDopoData(@RequestParam LocalDate data){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByDataAfterAndIsCancellatoFalse(data));
    }

    @Operation(summary = "Trova eventi con data precedente a quella immessa.", description = "Questo endpoint permette di inserire una data e di trovare solo gli eventi che hanno data precedente a quella immessa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eventi con data precedente a quella immessa recuperati correttamente.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "406", description = "Nessun evento trovato con data precedente a quella immessa.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @GetMapping("/trovaEventiPrimaDiData")
    public ResponseEntity<List<Evento>> eventiPrimaDiData(@RequestParam LocalDate data){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByDataBeforeAndIsCancellatoFalse(data));
    }

    @Operation(summary = "Trova eventi con data uguale o successiva a quella immessa.", description = "Questo endpoint permette di inserire una data e di trovare solo gli eventi che hanno data uguale o successiva a quella immessa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eventi recuperati correttamente.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "406", description = "Nessun evento trovato con data uguale o successiva a quella immessa.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @GetMapping("/trovaEventiConDataDa")
    public ResponseEntity<List<Evento>> eventiConDataDa(@RequestParam LocalDate data){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByDataOnwards(data));
    }

    @Operation(summary = "Endpoint per la criteria query degli eventi", description = "Questo endpoint permette di filtrare gli eventi in base a una data e a una descrizione, entrambe opzionali. Se la data viene immessa, vengono restituiti solo gli eventi che hanno data uguale o successiva a essa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eventi recuperati correttamente.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "404", description = "Nessun evento trovato con questi criteri.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @PostMapping("/filtraEventi")
    public ResponseEntity<List<Evento>> filtraEventi(@RequestBody FiltraEventoDTORequest request){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.filtraEventi(request));
    }

    @Operation(summary = "Trova tutti gli eventi", description = "Questo endpoint permette di trovare tutti gli eventi a noi visibili.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eventi recuperati correttamente.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Evento.class))),
            @ApiResponse(responseCode = "404", description = "Nessun evento trovato.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @GetMapping("/tuttiGliEventi")
    public ResponseEntity<List<Evento>> trovaTuttiGliEventi(){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAllByIsCancellatoFalse());
    }

}
