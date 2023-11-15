package it.dedagroup.venditabiglietti.repositorytest;

import it.dedagroup.venditabiglietti.VenditaBigliettiEventoApplication;
import it.dedagroup.venditabiglietti.model.Evento;
import it.dedagroup.venditabiglietti.repository.EventoRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = VenditaBigliettiEventoApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventoRepoTest {

    @Autowired
    EventoRepository eRepo;

    @Test
    public void findAllTest(){
        List<Evento> eventi = eRepo.findAll();
        assertEquals(8, eventi.size());
    }

    @Test
    public void findById(){
        assertThat(eRepo.findById(1L).get()).extracting(Evento::getDescrizione).isEqualTo("Michael Jackson live Torrino");
    }
    @Test
    public void findByDescrizione(){
        assertThat(eRepo.findByDescrizioneAndIsCancellatoFalse("Michael Jackson live Torrino").get())
                .extracting(Evento::getDescrizione).isEqualTo("Michael Jackson live Torrino");
    }

    @Test
    public void findAllByData(){
        LocalDate data = LocalDate.of(2023, 12, 22);
        List<Evento> eventi = eRepo.findAllByDataAndIsCancellatoFalse(data);
        assertEquals(2, eventi.size());
    }

}
