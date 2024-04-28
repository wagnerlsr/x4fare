package br.com.x4fare.services.impl;

import br.com.x4fare.X4FareApplication;
import br.com.x4fare.repositories.IReport;
import br.com.x4fare.repositories.TransactionRepository;
import br.com.x4fare.services.ReportService;
import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class Report implements IReport {
    private Long    busId;
    private Float   tripFare;
    private Long    userId;
    private String  type;
    private Integer count;
}

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = X4FareApplication.class)
class ReportServiceImplTests {

    @MockBean(name="transactionRepository")
    TransactionRepository transactionRepository;
    ReportService reportService;

    List<IReport> reports;


    @BeforeEach
    public void setUp() {
        reports = Arrays.asList(
                Report.builder()
                        .busId(1L)
                        .tripFare(5f)
                        .userId(1L)
                        .type("REGULAR")
                        .count(2)
                        .build(),
                Report.builder()
                        .busId(1L)
                        .tripFare(5f)
                        .userId(2L)
                        .type("ELDERLY")
                        .count(1)
                        .build(),
                Report.builder()
                        .busId(1L)
                        .tripFare(5f)
                        .userId(3L)
                        .type("STUDENT")
                        .count(1)
                        .build()
        );

        reportService = new ReportServiceImpl(transactionRepository);
    }

    @Test
    public void transaction_Success() {

        when( transactionRepository.findAllByTransactionDate(any()) ).thenReturn(reports);

        HashMap<String, List<HashMap<String, Object>>> result = reportService.dailyReport(LocalDate.now(), LocalDate.now());

        var busId = JsonPath.parse(result).read("$[\"2024-04-28\"][0].busId");
        var totalAmount = JsonPath.parse(result).read("$[\"2024-04-28\"][0].totalAmount");
        var numTransactions = JsonPath.parse(result).read("$[\"2024-04-28\"][0].numTransactions");
        var numUsers = JsonPath.parse(result).read("$[\"2024-04-28\"][0].numUsers");

        assertThat( result ).isNotNull();
        assertThat( result ).isInstanceOf(HashMap.class);
        assertThat( busId ).isEqualTo(1L);
        assertThat( totalAmount ).isEqualTo(12.5);
        assertThat( numTransactions ).isEqualTo(4);
        assertThat( numUsers ).isEqualTo(3);

    }

}
