package br.com.x4fare.controllers;

import br.com.x4fare.services.BusStopService;
import br.com.x4fare.services.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(BusStopController.class)
class BusStopControllerTests {

    @MockBean(name="busStopService")
    BusStopService busStopService;
    BusStopController busStopController;
    HashMap<String, Object> departures;


    @BeforeEach
    public void setUp() {
        departures = new HashMap<>();
        var map1 = new HashMap<String, String>();
        var map2 = new HashMap<String, String>();

        map1.put("Line", "7S");
        map1.put("Destination", "Shadwell");
        map1.put("Time", "20:00");

        map2.put("Line", "7A");
        map2.put("Destination", "Alwoodley");
        map2.put("Time", "20:40");

        departures.put("Departures", List.of(map1, map2));

        busStopController = new BusStopController(busStopService);
    }

    @Test
    public void dailyReport_Success() throws Exception {

        BDDMockito.given( busStopService.stopTimetables(anyString())).willReturn(departures);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/bus/stop_timetables/450024834.json");

        MockMvcBuilders.standaloneSetup(busStopController).build().perform(request)
                .andDo( print() )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.Departures").isArray())
                .andExpect( jsonPath("$.Departures").value(hasSize(2)))
                .andExpect( jsonPath("$.Departures[0].Line", equalTo("7S")))
                .andExpect( jsonPath("$.Departures[0].Destination", equalTo("Shadwell")))
                .andExpect( jsonPath("$.Departures[0].Time", equalTo("20:00")))
                .andExpect( jsonPath("$.Departures[1].Line", equalTo("7A")))
                .andExpect( jsonPath("$.Departures[1].Destination", equalTo("Alwoodley")))
                .andExpect( jsonPath("$.Departures[1].Time", equalTo("20:40")));
    }

}
