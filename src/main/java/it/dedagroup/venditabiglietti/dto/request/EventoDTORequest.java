package it.dedagroup.venditabiglietti.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventoDTORequest {
    @NotNull(message = "Il campo data non può essere vuoto.")
    LocalDateTime data;
    @NotBlank(message = "Il campo descrizione non può essere vuoto.")
    String descrizione;
}
