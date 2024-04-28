package br.com.x4fare.controllers;

import br.com.x4fare.dtos.ResponseDto;
import br.com.x4fare.models.Bus;
import br.com.x4fare.services.BusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(BusController.class)
class BusControllerTests {

    @MockBean(name="busService")
    BusService busService;
    BusController busController;
    List<Bus> buses;


    @BeforeEach
    public void setUp() {
        buses = Arrays.asList(
                Bus.builder()
                        .id(1L)
                        .name("Linha 1")
                        .tripFare(5.00)
                        .build(),
                Bus.builder()
                        .id(2L)
                        .name("Linha 2")
                        .tripFare(3.00)
                        .build()
        );

        busController = new BusController(busService);
    }

    @Test
    public void buses_Success() throws Exception {

        BDDMockito.given( busService.buses()).willReturn(buses);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/buses");

        MockMvcBuilders.standaloneSetup(busController).build().perform(request)
                .andDo( print() )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$").isArray())
                .andExpect( jsonPath("$").value(hasSize(2)))
                .andExpect( jsonPath("$[0].id", equalTo(1)))
                .andExpect( jsonPath("$[0].name", equalTo("Linha 1")));
    }

    @Test
    public void insertBus_Success() throws Exception {

        BDDMockito.given( busService.insertBus(any()) )
                .willReturn( ResponseDto.builder()
                        .success(true)
                        .messages(Collections.singletonList("Linha criada com sucesso"))
                        .build() );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/buses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createBusInJson("Linha 1", 5.00));

        MockMvcBuilders.standaloneSetup(busController).build().perform(request)
                .andDo( print() )
                .andExpect( status().isCreated() )
                .andExpect( jsonPath("$.success", equalTo(true)))
                .andExpect( jsonPath("$.messages[0]", equalTo("Linha criada com sucesso")));
    }

    @Test
    public void insertBus_Error() throws Exception {

        BDDMockito.given( busService.insertBus(any()) )
                .willReturn( ResponseDto.builder()
                        .success(false)
                        .messages(Collections.singletonList("Linha já cadastrada"))
                        .build() );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/buses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createBusInJson("Linha 1", 5.00));

        MockMvcBuilders.standaloneSetup(busController).build().perform(request)
                .andDo( print() )
                .andExpect( status().isInternalServerError() )
                .andExpect( jsonPath("$.success", equalTo(false)))
                .andExpect( jsonPath("$.messages[0]", equalTo("Linha já cadastrada")));
    }

    private static String createBusInJson (String name, Double tripFare) {
        return "{ \"name\": \"" + name + "\", " + "\"tripFare\": " + tripFare + "}";
    }

}
