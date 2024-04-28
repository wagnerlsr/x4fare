package br.com.x4fare.services.impl;

import br.com.x4fare.X4FareApplication;
import br.com.x4fare.dtos.BusDto;
import br.com.x4fare.dtos.ResponseDto;
import br.com.x4fare.models.Bus;
import br.com.x4fare.repositories.BusRepository;
import br.com.x4fare.services.BusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = X4FareApplication.class)
class BusServiceImplTests {

    @MockBean(name="busRepository")
    BusRepository busRepository;
    BusService busService;

    List<Bus> buses;
    List<BusDto> busesDto;


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

        busesDto = Arrays.asList(
                BusDto.builder()
                        .name("Linha 1")
                        .tripFare(5.00)
                        .build(),
                BusDto.builder()
                        .name("Linha 2")
                        .tripFare(3.00)
                        .build()
        );

        busService = new BusServiceImpl(busRepository);
    }

    @Test
    public void buses_Success() {

        when( busRepository.findAll() ).thenReturn(buses);

        List<Bus> result = busService.buses();

        assertThat( result.size() ).isEqualTo(2);
        assertThat( result.containsAll(buses) ).isTrue();

    }

    @Test
    public void insertBus_Success() {

        when( busRepository.findByName(anyString()) ).thenReturn( List.of() );

        ResponseDto result = busService.insertBus(busesDto.get(0));

        assertThat( result ).isNotNull();
        assertThat( result ).isInstanceOf(ResponseDto.class);
        assertThat( result.getSuccess() ).isTrue();
        assertThat( result.getMessages() ).contains("Linha criada com sucesso");

    }

    @Test
    public void insertBus_LineAlreadyRegistered() {

        when( busRepository.findByName(anyString()) ).thenReturn( List.of(buses.get(0)) );

        ResponseDto result = busService.insertBus(busesDto.get(0));

        assertThat( result ).isNotNull();
        assertThat( result ).isInstanceOf(ResponseDto.class);
        assertThat( result.getSuccess() ).isFalse();
        assertThat( result.getMessages() ).contains("Linha já cadastrada");

    }

    @Test
    public void insertBus_ExceptionError() {

        when( busRepository.save(any(Bus.class)) ).thenThrow(new RuntimeException("Any type of error"));

        ResponseDto result = busService.insertBus(busesDto.get(0));

        assertThat( result ).isNotNull();
        assertThat( result ).isInstanceOf(ResponseDto.class);
        assertThat( result.getSuccess() ).isFalse();
        assertThat( result.getMessages() ).contains("Any type of error");

    }

}
