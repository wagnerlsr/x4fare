package br.com.x4fare.controllers;

import br.com.x4fare.dtos.ResponseDto;
import br.com.x4fare.dtos.UserDto;
import br.com.x4fare.models.User;
import br.com.x4fare.services.UserService;
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
@WebMvcTest(UserController.class)
class UserControllerTests {

    @MockBean(name="userService")
    UserService userService;
    UserController userController;
    List<User> users;


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

        userController = new UserController(userService);
    }

    @Test
    public void users_Success() throws Exception {

        BDDMockito.given( userService.users()).willReturn(users);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/users");

        MockMvcBuilders.standaloneSetup(userController).build().perform(request)
                .andDo( print() )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$").isArray())
                .andExpect( jsonPath("$").value(hasSize(3)))
                .andExpect( jsonPath("$[0].id", equalTo(1)))
                .andExpect( jsonPath("$[0].name", equalTo("Usuario 1")));
    }

    @Test
    public void insertUser_Success() throws Exception {

        BDDMockito.given( userService.insertUser(any()) )
                .willReturn( ResponseDto.builder()
                        .success(true)
                        .messages(Collections.singletonList("Usuário criado com sucesso"))
                        .build() );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserInJson("Usuario 1", UserDto.UserType.REGULAR.toString()));

        MockMvcBuilders.standaloneSetup(userController).build().perform(request)
                .andDo( print() )
                .andExpect( status().isCreated() )
                .andExpect( jsonPath("$.success", equalTo(true)))
                .andExpect( jsonPath("$.messages[0]", equalTo("Usuário criado com sucesso")));
    }

    @Test
    public void insertUser_Error() throws Exception {

        BDDMockito.given( userService.insertUser(any()) )
                .willReturn( ResponseDto.builder()
                        .success(false)
                        .messages(Collections.singletonList("Usuário já cadastrado"))
                        .build() );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserInJson("Linha 1", UserDto.UserType.REGULAR.toString()));

        MockMvcBuilders.standaloneSetup(userController).build().perform(request)
                .andDo( print() )
                .andExpect( status().isInternalServerError() )
                .andExpect( jsonPath("$.success", equalTo(false)))
                .andExpect( jsonPath("$.messages[0]", equalTo("Usuário já cadastrado")));
    }

    private static String createUserInJson (String name, String type) {
        return "{ \"name\": \"" + name + "\", " + "\"type\": \"" + type + "\"}";
    }

}
