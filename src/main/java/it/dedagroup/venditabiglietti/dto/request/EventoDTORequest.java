package it.dedagroup.venditabiglietti.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class EventoDTORequest {
    @NotNull(message = "Il campo data non può essere vuoto.")
    LocalDate data;
    @NotNull(message = "Il campo ora non può essere vuoto.")
    LocalTime ora;
    @NotBlank(message = "Il campo descrizione non può essere vuoto.")
    String descrizione;
}
