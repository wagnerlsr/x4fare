package br.com.x4fare.services.impl;

import br.com.x4fare.X4FareApplication;
import br.com.x4fare.dtos.ResponseDto;
import br.com.x4fare.dtos.UserDto;
import br.com.x4fare.models.User;
import br.com.x4fare.repositories.UserRepository;
import br.com.x4fare.services.UserService;
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
class UserServiceImplTests {

    @MockBean(name="userRepository")
    UserRepository userRepository;
    UserService userService;

    List<User> users;
    List<UserDto> usersDto;


    @BeforeEach
    public void setUp() {
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
                        .build(),
                User.builder()
                        .id(3L)
                        .name("Usuario 3")
                        .type(UserDto.UserType.STUDENT.toString())
                        .build()
        );

        usersDto = Arrays.asList(
                UserDto.builder()
                        .name("Usuario 1")
                        .type(UserDto.UserType.REGULAR)
                        .build(),
                UserDto.builder()
                        .name("Usuario 2")
                        .type(UserDto.UserType.ELDERLY)
                        .build(),
                UserDto.builder()
                        .name("Usuario 3")
                        .type(UserDto.UserType.STUDENT)
                        .build()
        );

        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void buses_Success() {

        when( userRepository.findAll() ).thenReturn(users);

        List<User> result = userService.users();

        assertThat( result.size() ).isEqualTo(3);
        assertThat( result.containsAll(users) ).isTrue();

    }

    @Test
    public void insertUser_Success() {

        when( userRepository.findByName(anyString()) ).thenReturn( List.of() );

        ResponseDto result = userService.insertUser(usersDto.get(0));

        assertThat( result ).isNotNull();
        assertThat( result ).isInstanceOf(ResponseDto.class);
        assertThat( result.getSuccess() ).isTrue();
        assertThat( result.getMessages() ).contains("Usuário criado com sucesso");

    }

    @Test
    public void insertUser_UserAlreadyRegistered() {

        when( userRepository.findByName(anyString()) ).thenReturn( List.of(users.get(0)) );

        ResponseDto result = userService.insertUser(usersDto.get(0));

        assertThat( result ).isNotNull();
        assertThat( result ).isInstanceOf(ResponseDto.class);
        assertThat( result.getSuccess() ).isFalse();
        assertThat( result.getMessages() ).contains("Usuário já cadastrado");

    }

    @Test
    public void insertUser_ExceptionError() {

        when( userRepository.save(any(User.class)) ).thenThrow(new RuntimeException("Any type of error"));

        ResponseDto result = userService.insertUser(usersDto.get(0));

        assertThat( result ).isNotNull();
        assertThat( result ).isInstanceOf(ResponseDto.class);
        assertThat( result.getSuccess() ).isFalse();
        assertThat( result.getMessages() ).contains("Any type of error");

    }

}
