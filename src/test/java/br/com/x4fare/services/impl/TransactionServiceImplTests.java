package br.com.x4fare.services.impl;

import br.com.x4fare.X4FareApplication;
import br.com.x4fare.dtos.ResponseDto;
import br.com.x4fare.dtos.TransactionDto;
import br.com.x4fare.dtos.UserDto;
import br.com.x4fare.models.Bus;
import br.com.x4fare.models.Transaction;
import br.com.x4fare.models.User;
import br.com.x4fare.repositories.BusRepository;
import br.com.x4fare.repositories.TransactionRepository;
import br.com.x4fare.repositories.UserRepository;
import br.com.x4fare.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = X4FareApplication.class)
class TransactionServiceImplTests {

    @MockBean(name="busRepository")
    BusRepository busRepository;
    @MockBean(name="userRepository")
    UserRepository userRepository;
    @MockBean(name="transactionRepository")
    TransactionRepository transactionRepository;
    TransactionService transactionService;

    List<Transaction> transactions;
    List<TransactionDto> transactionsDto;
    List<Bus> buses;
    List<User> users;


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

        users = Arrays.asList(
                User.builder()
                        .id(1L)
                        .name("Usuario 1")
                        .type(UserDto.UserType.REGULAR.toString())
                        .build(),
                User.builder()
                        .id(2L)
                        .name("Usuario 2")
                        .type(UserDto.UserType.ELDERLY.toString())
                        .build()
        );

        transactions = Arrays.asList(
                Transaction.builder()
                        .id(1L)
                        .bus(buses.get(0))
                        .user(users.get(0))
                        .transactionDate(LocalDateTime.now())
                        .build(),
                Transaction.builder()
                        .id(2L)
                        .bus(buses.get(0))
                        .user(users.get(1))
                        .transactionDate(LocalDateTime.now())
                        .build(),
                Transaction.builder()
                        .id(3L)
                        .bus(buses.get(1))
                        .user(users.get(1))
                        .transactionDate(LocalDateTime.now())
                        .build()
        );

        transactionsDto = Arrays.asList(
                TransactionDto.builder()
                        .busId(1L)
                        .userId(1L)
                        .build(),
                TransactionDto.builder()
                        .busId(1L)
                        .userId(2L)
                        .build(),
                TransactionDto.builder()
                        .busId(2L)
                        .userId(1L)
                        .build()
        );

        transactionService = new TransactionServiceImpl(transactionRepository, busRepository, userRepository);
    }

    @Test
    public void transaction_Success() {

        when( busRepository.findById(anyLong()) ).thenReturn(Optional.of(buses.get(0)));
        when( userRepository.findById(anyLong()) ).thenReturn(Optional.of(users.get(0)));

        ResponseDto result = transactionService.transaction(transactionsDto.get(0));

        assertThat( result ).isNotNull();
        assertThat( result ).isInstanceOf(ResponseDto.class);
        assertThat( result.getSuccess() ).isTrue();
        assertThat( result.getMessages() ).contains("Transação criada com sucesso");

    }

    @Test
    public void transaction_InvalidBus() {

        when( busRepository.findById(anyLong()) ).thenReturn(Optional.empty());
        when( userRepository.findById(anyLong()) ).thenReturn(Optional.of(users.get(0)));

        ResponseDto result = transactionService.transaction(transactionsDto.get(0));

        assertThat( result ).isNotNull();
        assertThat( result ).isInstanceOf(ResponseDto.class);
        assertThat( result.getSuccess() ).isFalse();
        assertThat( result.getMessages() ).contains("Linha invalida");

    }

    @Test
    public void transaction_InvalidUser() {

        when( busRepository.findById(anyLong()) ).thenReturn(Optional.of(buses.get(0)));
        when( userRepository.findById(anyLong()) ).thenReturn(Optional.empty());

        ResponseDto result = transactionService.transaction(transactionsDto.get(0));

        assertThat( result ).isNotNull();
        assertThat( result ).isInstanceOf(ResponseDto.class);
        assertThat( result.getSuccess() ).isFalse();
        assertThat( result.getMessages() ).contains("Usuário invalido");

    }

}
