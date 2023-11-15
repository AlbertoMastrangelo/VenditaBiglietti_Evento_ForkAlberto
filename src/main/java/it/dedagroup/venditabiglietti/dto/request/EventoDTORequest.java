package it.dedagroup.venditabiglietti.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class EventoDTORequest {
    @NotNull(message = "Il campo data non può essere vuoto.")
    LocalDate data;
    @NotNull(message = "Il campo ora non può essere vuoto.")
    LocalTime ora;
    @NotBlank(message = "Il campo descrizione non può essere vuoto.")
    String descrizione;
    @Min(value = 1, message = "L'id del luogo associato deve essere almeno 1")
    private long idLuogo;
    @Min(value = 1, message = "L'id del luogo associato deve essere almeno 1")
    private long idManifestazione;
}
