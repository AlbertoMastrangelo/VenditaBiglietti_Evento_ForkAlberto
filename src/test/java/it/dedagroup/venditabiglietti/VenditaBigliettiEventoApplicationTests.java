package it.dedagroup.venditabiglietti;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.dedagroup.venditabiglietti.dto.request.EventoDTORequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = VenditaBigliettiEventoApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class VenditaBigliettiEventoApplicationTests {

    @Autowired
    private MockMvc mvc;

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();


    @Test @Order(1)
    void findByIdSbagliato() throws Exception{
        long id = 100;
        mvc.perform(MockMvcRequestBuilders.get("/evento/id/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andReturn();
    }

    @Test @Order(1)
    void findByIdOk() throws Exception{
        long id = 1;
        mvc.perform(MockMvcRequestBuilders.get("/evento/id/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.descrizione").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.descrizione").value("Michael Jackson live Torrino"))
                .andReturn();
    }

    @Test
    void findByIdsOk() throws Exception {
        List<Long> ids = new ArrayList<Long>(List.of(1L, 2L, 3L));
        mvc.perform(MockMvcRequestBuilders.get("/evento/ids", ids).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(ids.toString())).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].descrizione").value("Metallica live Circo Massimo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].data").value("2023-12-23"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ora").value("22:00:00"))
                .andReturn();
    }

    @Test
    void findByIdsSbagliati() throws Exception {
        List<Long> ids = new ArrayList<Long>(List.of(90L, 22L, 33L));
        mvc.perform(MockMvcRequestBuilders.get("/evento/ids", ids).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(ids.toString())).andExpect(MockMvcResultMatchers.status().isNotAcceptable()).andReturn();
    }

    @Test
    void findByIdsNoIds() throws Exception {
        List<Long> ids = new ArrayList<Long>(List.of());
        mvc.perform(MockMvcRequestBuilders.get("/evento/ids", ids).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(ids.toString())).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test @Order(2)
    void salvaSenzaBody() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/evento/salva").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test @Order(3)
    void salvaConBody() throws Exception{
        EventoDTORequest eDTOReq = new EventoDTORequest();
        LocalDate dataIns = LocalDate.parse("2023-11-06");
        LocalTime ora = LocalTime.of(23, 00, 00);
        long idLuogo = 1;
        long idManifestazione = 4;
        eDTOReq.setData(dataIns);
        eDTOReq.setOra(ora);
        eDTOReq.setDescrizione("Festival Bar");
        eDTOReq.setIdLuogo(idLuogo);
        eDTOReq.setIdManifestazione(idManifestazione);
        String json = mapper.writeValueAsString(eDTOReq);
        mvc.perform(MockMvcRequestBuilders.post("/evento/salva").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test @Order(4)
    void salvaLuogoFail() throws Exception{
        EventoDTORequest eDTOReq = new EventoDTORequest();
        LocalDate dataIns = LocalDate.parse("2023-11-11");
        LocalTime ora = LocalTime.of(21, 00, 00);
        long idLuogo = 5;
        long idManifestazione = 3;
        eDTOReq.setData(dataIns);
        eDTOReq.setOra(ora);
        eDTOReq.setDescrizione("Sanremo");
        eDTOReq.setIdLuogo(idLuogo);
        eDTOReq.setIdManifestazione(idManifestazione);
        String json = mapper.writeValueAsString(eDTOReq);
        mvc.perform(MockMvcRequestBuilders.post("/evento/salva").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotAcceptable()).andReturn();
    }

    @Test @Order(5)
    void salvaManifestazioneFail() throws Exception{
        EventoDTORequest eDTOReq = new EventoDTORequest();
        LocalDate dataIns = LocalDate.parse("2023-12-23");
        LocalTime ora = LocalTime.of(19, 00, 00);
        long idLuogo = 2;
        long idManifestazione = 1;
        eDTOReq.setData(dataIns);
        eDTOReq.setOra(ora);
        eDTOReq.setDescrizione("Sanremo");
        eDTOReq.setIdLuogo(idLuogo);
        eDTOReq.setIdManifestazione(idManifestazione);
        String json = mapper.writeValueAsString(eDTOReq);
        mvc.perform(MockMvcRequestBuilders.post("/evento/salva").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isConflict()).andReturn();
    }

    @Test
    void aggiornaOk() throws Exception{
        EventoDTORequest eDTOReq = new EventoDTORequest();
        long id = 9;
        long idLuogo = 3;
        long idManifestazione = 2;
        LocalDate dataIns = LocalDate.parse("2023-11-09");
        LocalTime ora = LocalTime.of(22, 00, 00);
        eDTOReq.setData(dataIns);
        eDTOReq.setOra(ora);
        eDTOReq.setDescrizione("Concerto JUnit");
        eDTOReq.setIdLuogo(idLuogo);
        eDTOReq.setIdManifestazione(idManifestazione);
        String json = mapper.writeValueAsString(eDTOReq);
        mvc.perform(MockMvcRequestBuilders.post("/evento/aggiorna/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    void aggiornaIdSbagliato() throws Exception{
        EventoDTORequest eDTOReq = new EventoDTORequest();
        long id = 90;
        long idLuogo = 3;
        long idManifestazione = 2;
        LocalDate dataIns = LocalDate.parse("2023-11-09");
        LocalTime ora = LocalTime.of(22, 00, 00);
        eDTOReq.setData(dataIns);
        eDTOReq.setOra(ora);
        eDTOReq.setDescrizione("Concerto errore update");
        eDTOReq.setIdLuogo(idLuogo);
        eDTOReq.setIdManifestazione(idManifestazione);
        String json = mapper.writeValueAsString(eDTOReq);
        mvc.perform(MockMvcRequestBuilders.post("/evento/aggiorna/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotAcceptable()).andReturn();
    }

    @Test @Order(4)
    void aggiornaDescGiàEsistente() throws Exception{
        EventoDTORequest eDTOReq = new EventoDTORequest();
        long id = 2;
        long idLuogo = 3;
        long idManifestazione = 2;
        LocalDate dataIns = LocalDate.parse("2023-11-09");
        LocalTime ora = LocalTime.of(22, 00, 00);
        eDTOReq.setData(dataIns);
        eDTOReq.setOra(ora);
        eDTOReq.setDescrizione("Madonna live Pescara");
        eDTOReq.setIdLuogo(idLuogo);
        eDTOReq.setIdManifestazione(idManifestazione);
        String json = mapper.writeValueAsString(eDTOReq);
        mvc.perform(MockMvcRequestBuilders.post("/evento/aggiorna/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn();
    }

    @Test
    void cancellaOk() throws Exception{
        long id = 3;
        mvc.perform(MockMvcRequestBuilders.post("/evento/cancella/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    void cancellaIdSbagliato() throws Exception{
        long id = 300;
        mvc.perform(MockMvcRequestBuilders.post("/evento/cancella/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable()).andReturn();
    }

    @Test
    void trovaPerDescrizione() throws Exception{
        String descrizione = "Michael Jackson live Torrino";
        mvc.perform(MockMvcRequestBuilders.post("/evento/trovaPerDescrizione")
                        .param("descrizione", descrizione)
                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    void trovaPerDescrizioneFail() throws Exception{
        String descrizione = "Sanremo 2025";
        mvc.perform(MockMvcRequestBuilders.post("/evento/trovaPerDescrizione")
                        .param("descrizione", descrizione)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable()).andReturn();
    }

    @Test
    void trovaPerDescrizioneNoBody() throws Exception{
        String descrizione = "";
        mvc.perform(MockMvcRequestBuilders.post("/evento/trovaPerDescrizione")
                        .param("descrizione", descrizione)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn();
    }

    @Test
    void trovaPerDataEventoOk() throws Exception{
        LocalDate data = LocalDate.of(2023, 12, 23);
        mvc.perform(MockMvcRequestBuilders.get("/evento/trovaPerData")
                .param("data", data.toString())
                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    void trovaPerDataEventoFail() throws Exception{
        LocalDate data = LocalDate.of(2022, 11, 30);
        mvc.perform(MockMvcRequestBuilders.get("/evento/trovaPerData")
                        .param("data", data.toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable()).andReturn();
    }

    @Test
    void trovaPerDataEventoNoParam() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/evento/trovaPerData")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    void trovaTuttiConCancellatoSuOk() throws Exception{
        boolean canc = false;
        mvc.perform(MockMvcRequestBuilders.post("/evento/trovaTuttiConCancellatoSu")
                .param("cancellato", String.valueOf(canc))
                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].descrizione").value("Festival Bar"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].data").value("2023-11-06"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].descrizione").value("Pooh live Marconia"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].data").value("2023-11-11"))
                .andReturn();
    }

    @Test
    void trovaPerCancellatoParamInvalido() throws Exception{
        String canc = "falseeee";
        mvc.perform(MockMvcRequestBuilders.post("/evento/trovaTuttiConCancellatoSu")
                        .param("cancellato", canc)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    void trovaPerDataEventoEDescrizioneOk() throws Exception{
        LocalDate data = LocalDate.of(2023, 12, 23);
        String desc = "Metallica live Circo Massimo";
        mvc.perform(MockMvcRequestBuilders.post("/evento/trovaPerDataEDescrizione")
                        .param("data", data.toString()).param("descrizione", desc)
                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.descrizione").value("Metallica live Circo Massimo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("2023-12-23"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ora").value("22:00:00"))
                .andReturn();
    }

    @Test
    void trovaPerEventoEDescrizioneSenzaBody() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/evento/trovaPerDataEDescrizione")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void trovaPerDataEventoEDescrizioneNessunRisultato() throws Exception{
        LocalDate data = LocalDate.of(2022, 11, 21);
        String desc = "Live Cugini di Campagna";
        mvc.perform(MockMvcRequestBuilders.post("/evento/trovaPerDataEDescrizione")
                        .param("data", data.toString()).param("descrizione", desc)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andReturn();
    }

    @Test
    void trovaEventiCheContengonoInDescrizione() throws Exception{
        String s = "live";
        mvc.perform(MockMvcRequestBuilders.post("/evento/trovaEventiCheContengono")
                .param("string", s).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].descrizione").value("Pooh live Marconia"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].data").value("2023-11-11"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].descrizione").value("Megadeth live Venezia"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].data").value("2023-11-12"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].descrizione").value("DPG live Sanremo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].data").value("2023-12-22"))
                .andReturn();
    }

    @Test
    void trovaEventiCheContengonoInDescrizioneFail() throws Exception{
        String s = "AC/DC";
        mvc.perform(MockMvcRequestBuilders.post("/evento/trovaEventiCheContengono")
                        .param("string", s).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andReturn();
    }

    @Test
    void trovaEventiFraDueDate() throws Exception {
        String data1 = String.valueOf(LocalDate.of(2023, 04, 01));
        String data2 = String.valueOf(LocalDate.of(2023, 12, 31));

        mvc.perform(MockMvcRequestBuilders.post("/evento/trovaEventiFraDueDate")
                .param("data1", data1)
                .param("data2", data2)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    void trovaEventiFraDueDateParamFail() throws Exception {
        String data1 = String.valueOf(LocalDate.of(2024, 04, 01));
        String data2 = String.valueOf(LocalDate.of(2023, 12, 31));

        mvc.perform(MockMvcRequestBuilders.post("/evento/trovaEventiFraDueDate")
                        .param("data1", data1)
                        .param("data2", data2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn();
    }

    @Test
    void trovaEventiFraDueDateNoRisultati() throws Exception {
        String data1 = String.valueOf(LocalDate.of(2026, 04, 01));
        String data2 = String.valueOf(LocalDate.of(2027, 12, 31));

        mvc.perform(MockMvcRequestBuilders.post("/evento/trovaEventiFraDueDate")
                        .param("data1", data1)
                        .param("data2", data2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable()).andReturn();
    }

    @Test
    void trovaEventiFuturi() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/evento/trovaEventiFuturi")
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    void trovaEventiPassati() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/evento/trovaEventiPassati")
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    void trovaEventiDopoData() throws Exception{
        LocalDate data = LocalDate.of(2022, 12, 21);
        mvc.perform(MockMvcRequestBuilders.get("/evento/trovaEventiDopoData")
                .param("data", data.toString()).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void trovaZeroEventiDopoData() throws Exception{
        LocalDate data = LocalDate.of(2025, 12, 21);
        mvc.perform(MockMvcRequestBuilders.get("/evento/trovaEventiDopoData")
                        .param("data", data.toString()).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotAcceptable()).andReturn();
    }

    @Test
    void trovaEventiPrimaDiData() throws Exception{
        LocalDate data = LocalDate.of(2023, 12, 31);
        mvc.perform(MockMvcRequestBuilders.get("/evento/trovaEventiPrimaDiData")
                        .param("data", data.toString()).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void trovaZeroEventiPrimaDiData() throws Exception{
        LocalDate data = LocalDate.of(2021, 12, 21);
        mvc.perform(MockMvcRequestBuilders.get("/evento/trovaEventiPrimaDiData")
                        .param("data", data.toString()).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotAcceptable()).andReturn();
    }

    @Test
    void trovaEventiConDataDa() throws Exception{
        LocalDate data = LocalDate.of(2023, 12, 22);
        mvc.perform(MockMvcRequestBuilders.get("/evento/trovaEventiConDataDa")
                .param("data", data.toString()).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descrizione").value("Madonna live Pescara"))
                .andExpect(jsonPath("$[0].data").value("2023-12-22"))
                .andExpect(jsonPath("$[0].ora").value("21:00:00"))
                .andReturn();
    }

    @Test
    void trovaEventiConDataDaNoParam() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/evento/trovaEventiConDataDa")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void trovaEventiConDataDaFail() throws Exception{
        LocalDate data = LocalDate.of(2025, 10, 1);
        mvc.perform(MockMvcRequestBuilders.get("/evento/trovaEventiConDataDa")
                        .param("data", data.toString()).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }

}
