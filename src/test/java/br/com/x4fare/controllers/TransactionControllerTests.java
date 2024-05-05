package br.com.x4fare.controllers;

import br.com.x4fare.dtos.ResponseDto;
import br.com.x4fare.models.Transaction;
import br.com.x4fare.services.TransactionService;
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

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(TransactionController.class)
class TransactionControllerTests {

    @MockBean(name="transactionService")
    TransactionService transactionService;
    TransactionController transactionController;
    List<Transaction> transactions;


    @BeforeEach
    public void setUp() {
        transactionController = new TransactionController(transactionService);
    }

    @Test
    public void transaction_Success() throws Exception {

        BDDMockito.given( transactionService.transaction(any()) )
                .willReturn( ResponseDto.builder()
                        .success(true)
                        .messages(Collections.singletonList("Transação criada com sucesso"))
                        .build() );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createTransactionInJson(1L, 1L, "2024-04-27T00:00:00"));

        MockMvcBuilders.standaloneSetup(transactionController).build().perform(request)
                .andDo( print() )
                .andExpect( status().isCreated() )
                .andExpect( jsonPath("$.success", equalTo(true)))
                .andExpect( jsonPath("$.messages[0]", equalTo("Transação criada com sucesso")));
    }

    @Test
    public void transaction_Error_InvalidBus() throws Exception {

        BDDMockito.given( transactionService.transaction(any()) )
                .willReturn( ResponseDto.builder()
                        .success(false)
                        .messages(Collections.singletonList("Linha invalida"))
                        .build() );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createTransactionInJson(10L, 1L, "2024-04-27T00:00:00"));

        MockMvcBuilders.standaloneSetup(transactionController).build().perform(request)
                .andDo( print() )
                .andExpect( status().isInternalServerError() )
                .andExpect( jsonPath("$.success", equalTo(false)))
                .andExpect( jsonPath("$.messages[0]", equalTo("Linha invalida")));
    }

    @Test
    public void transaction_Error_InvalidUser() throws Exception {

        BDDMockito.given( transactionService.transaction(any()) )
                .willReturn( ResponseDto.builder()
                        .success(false)
                        .messages(Collections.singletonList("Usuário invalido"))
                        .build() );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createTransactionInJson(1L, 10L, "2024-04-27T00:00:00"));

        MockMvcBuilders.standaloneSetup(transactionController).build().perform(request)
                .andDo( print() )
                .andExpect( status().isInternalServerError() )
                .andExpect( jsonPath("$.success", equalTo(false)))
                .andExpect( jsonPath("$.messages[0]", equalTo("Usuário invalido")));
    }

    private static String createTransactionInJson (Long busId, Long userId, String transactionDate) {
        return "{ \"busId\": " + busId + ", " + "\"userId\": " + userId + ", " + "\"transactionDate\": \"" + transactionDate + "\"}";
    }

}
