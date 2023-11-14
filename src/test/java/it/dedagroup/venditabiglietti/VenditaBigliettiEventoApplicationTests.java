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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ContextConfiguration(classes = VenditaBigliettiEventoApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class VenditaBigliettiEventoApplicationTests {

    @Autowired
    private MockMvc mvc;


    @Test @Order(1)
    void findByIdSbagliato() throws Exception{
        long id = 100;
        mvc.perform(MockMvcRequestBuilders.get("/evento/id/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable()).andReturn();
    }

    @Test @Order(1)
    void findByIdOk() throws Exception{
        long id = 1;
        mvc.perform(MockMvcRequestBuilders.get("/evento/id/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    void findByIdsOk() throws Exception {
        List<Long> ids = new ArrayList<Long>(List.of(1L, 2L, 3L));
        String json = new ObjectMapper().writeValueAsString(ids);
        mvc.perform(MockMvcRequestBuilders.get("/evento/ids", ids).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(ids.toString())).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    void findByIdsSbagliati() throws Exception {
        List<Long> ids = new ArrayList<Long>(List.of(90L, 22L, 33L));
        String json = new ObjectMapper().writeValueAsString(ids);
        mvc.perform(MockMvcRequestBuilders.get("/evento/ids", ids).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(ids.toString())).andExpect(MockMvcResultMatchers.status().isNotAcceptable()).andReturn();
    }

    @Test @Order(2)
    void salvaSenzaBody() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/evento/salva").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test @Order(3)
    void salvaConBody() throws Exception{
        EventoDTORequest eDTOReq = new EventoDTORequest();
        LocalDateTime dataIns = LocalDateTime.parse("2023-11-06T23:00:00");
        eDTOReq.setData(dataIns);
        eDTOReq.setDescrizione("Festival Bar");
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        String json = mapper.writeValueAsString(eDTOReq);

        mvc.perform(MockMvcRequestBuilders.post("/evento/salva").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
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

}
