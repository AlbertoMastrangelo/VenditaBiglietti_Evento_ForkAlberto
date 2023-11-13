package it.dedagroup.venditabiglietti;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import it.dedagroup.venditabiglietti.dto.request.EventoDTORequest;
import lombok.RequiredArgsConstructor;
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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ContextConfiguration(classes = VenditaBigliettiEventoApplicationTests.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class VenditaBigliettiEventoApplicationTests {

    @Autowired
    private MockMvc mvc;

    /*
    @Test
    void findById() throws Exception{
        long id = 1;
        mvc.perform(MockMvcRequestBuilders.get("/evento/id/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }
     */

    @Test
    void salvaSenzaBody() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("evento/salva").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    void salvaConBody() throws Exception{
        EventoDTORequest eDTOReq = new EventoDTORequest();
        String data2 = "2023-09-06T23:00:00";
        LocalDateTime dataIns = LocalDateTime.parse(data2);
        eDTOReq.setData(dataIns);
        eDTOReq.setDescrizione("Festival Bar");
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule()) //permette inserimento data, senza dà errore
                .build();
        String json = mapper.writeValueAsString(eDTOReq);

        mvc.perform(MockMvcRequestBuilders.post("evento/salva").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(print());
    }

}
