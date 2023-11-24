package it.dedagroup.venditabiglietti.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class FiltraEventoDTORequest {

    LocalDate data;
    String descrizione;
}
