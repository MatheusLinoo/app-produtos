package br.com.equipe4.app_produtos.controller;

import br.com.equipe4.app_produtos.model.UserRole;
import br.com.equipe4.app_produtos.service.UserService;
import br.com.equipe4.app_produtos.service.dto.UserResponseDTO;
import br.com.equipe4.app_produtos.service.dto.request.UserRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;
    private UserService userService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        userService = Mockito.mock(UserService.class);
        UserController controller = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldCreateUser() throws Exception {

        UserRequestDTO request = new UserRequestDTO("João", "joao@example.com", "123456");
        UserResponseDTO response = new UserResponseDTO("123", "João", UserRole.ADMIN);

        Mockito.when(userService.createUser(any())).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("123"));
    }

    @Test
    void shouldListUsers() throws Exception {

        List<UserResponseDTO> users = List.of(
                new UserResponseDTO("1", "Maria", UserRole.SELLER),
                new UserResponseDTO("2", "José", UserRole.CUSTOMER)
        );

        Mockito.when(userService.listUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void shouldUpdateUser() throws Exception {

        UserRequestDTO request = new UserRequestDTO("Pedro", "pedro@example.com", "123456");

        mockMvc.perform(put("/users/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).updateUser(eq("10"), any());
    }

    @Test
    void shouldDeleteUser() throws Exception {

        mockMvc.perform(delete("/users/10"))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).deleteUser("10");
    }
}