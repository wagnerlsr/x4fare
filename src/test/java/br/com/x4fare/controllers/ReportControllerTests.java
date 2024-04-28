package br.com.x4fare.controllers;

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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(ReportController.class)
class ReportControllerTests {

    @MockBean(name="reportService")
    ReportService reportService;
    ReportController reportController;
    HashMap<String, List<HashMap<String, Object>>> reports;


    @BeforeEach
    public void setUp() {
        reports = new HashMap<>();
        var map1 = new HashMap<String, Object>();
        var map2 = new HashMap<String, Object>();

        map1.put("busId", 1);
        map1.put("totalAmount", 12.5);
        map1.put("numTransactions", 4);
        map1.put("numUsers", 3);

        map2.put("busId", 2);
        map2.put("totalAmount", 9.0);
        map2.put("numTransactions", 5);
        map2.put("numUsers", 3);

        reports.put("2024-04-27", List.of(map1, map2));

        reportController = new ReportController(reportService);
    }

    @Test
    public void dailyReport_Success() throws Exception {

        BDDMockito.given( reportService.dailyReport(any(), any())).willReturn(reports);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/reports/daily-report?startDate=2024-04-27&endDate=2024-04-27");

        MockMvcBuilders.standaloneSetup(reportController).build().perform(request)
                .andDo( print() )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$[\"2024-04-27\"]").isArray())
                .andExpect( jsonPath("$[\"2024-04-27\"]").value(hasSize(2)))
                .andExpect( jsonPath("$[\"2024-04-27\"][0].busId", equalTo(1)))
                .andExpect( jsonPath("$[\"2024-04-27\"][0].totalAmount", equalTo(12.5)))
                .andExpect( jsonPath("$[\"2024-04-27\"][0].numTransactions", equalTo(4)))
                .andExpect( jsonPath("$[\"2024-04-27\"][0].numUsers", equalTo(3)))
                .andExpect( jsonPath("$[\"2024-04-27\"][1].busId", equalTo(2)))
                .andExpect( jsonPath("$[\"2024-04-27\"][1].totalAmount", equalTo(9.0)))
                .andExpect( jsonPath("$[\"2024-04-27\"][1].numTransactions", equalTo(5)))
                .andExpect( jsonPath("$[\"2024-04-27\"][1].numUsers", equalTo(3)));
    }

}
