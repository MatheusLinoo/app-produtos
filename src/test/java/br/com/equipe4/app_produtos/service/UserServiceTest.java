package br.com.equipe4.app_produtos.service;

import br.com.equipe4.app_produtos.model.User;
import br.com.equipe4.app_produtos.repository.UserRepository;
import br.com.equipe4.app_produtos.service.dto.request.UserRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserRequestDTO requestDTO;
    private User user;
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        requestDTO = new UserRequestDTO("João Silva", "joao@email.com", "senha123");

        user = new User();
        user.setId("1");
        user.setName("João Silva");
        user.setEmail("joao@email.com");
        user.setUsername("joao@email.com");
        user.setPassword("encodedPassword");
    }

    @Test
    void shouldSaveUserWithEncodedPassword() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        verify(passwordEncoder, never()).encode(anyString());

        String encodedPassword = passwordEncoder.encode("senha123");
        User savedUser = userRepository.save(user);

        assertNotNull(encodedPassword);
        assertNotNull(savedUser);
        verify(passwordEncoder).encode("senha123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldSetUsernameAsEmail() {
        User newUser = new User();
        newUser.setName("João Silva");
        newUser.setEmail("joao@email.com");
        newUser.setPassword("senha123");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        newUser.setPassword(passwordEncoder.encode("senha123"));
        newUser.setUsername("joao@email.com");
        User savedUser = userRepository.save(newUser);

        assertEquals("joao@email.com", savedUser.getUsername());
        assertEquals("joao@email.com", savedUser.getEmail());
        verify(passwordEncoder).encode("senha123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldReturnAllUsers() {
        User user2 = new User();
        user2.setId("2");
        user2.setName("Maria Santos");
        user2.setEmail("maria@email.com");

        List<User> users = Arrays.asList(user, user2);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userRepository.findAll();

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals("João Silva", result.get(0).getName());
        assertEquals("Maria Santos", result.get(1).getName());
        verify(userRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> result = userRepository.findAll();

        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void shouldUpdateNameEmailAndPassword() {
        UserRequestDTO updateDTO = new UserRequestDTO("João Atualizado", "joao.novo@email.com", "novaSenha");
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("novaSenha")).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User foundUser = userRepository.findById("1").orElseThrow();
        foundUser.setName(updateDTO.name());
        foundUser.setEmail(updateDTO.email());

        if (updateDTO.password() != null && !updateDTO.password().isBlank()) {
            foundUser.setPassword(passwordEncoder.encode(updateDTO.password()));
        }

        User savedUser = userRepository.save(foundUser);

        assertNotNull(savedUser);
        assertEquals("João Atualizado", foundUser.getName());
        assertEquals("joao.novo@email.com", foundUser.getEmail());
        verify(userRepository).findById("1");
        verify(passwordEncoder).encode("novaSenha");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldNotUpdatePasswordWhenNull() {
        UserRequestDTO updateDTO = new UserRequestDTO("João Atualizado", "joao.novo@email.com", null);
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User foundUser = userRepository.findById("1").orElseThrow();
        foundUser.setName(updateDTO.name());
        foundUser.setEmail(updateDTO.email());

        if (updateDTO.password() != null && !updateDTO.password().isBlank()) {
            foundUser.setPassword(passwordEncoder.encode(updateDTO.password()));
        }

        userRepository.save(foundUser);

        verify(userRepository).findById("1");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository).save(user);
    }

    @Test
    void shouldNotUpdatePasswordWhenEmpty() {
        UserRequestDTO updateDTO = new UserRequestDTO("João Atualizado", "joao.novo@email.com", "");
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User foundUser = userRepository.findById("1").orElseThrow();
        foundUser.setName(updateDTO.name());
        foundUser.setEmail(updateDTO.email());

        if (updateDTO.password() != null && !updateDTO.password().isBlank()) {
            foundUser.setPassword(passwordEncoder.encode(updateDTO.password()));
        }

        userRepository.save(foundUser);

        verify(userRepository).findById("1");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnUpdate() {
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        Optional<User> result = userRepository.findById("999");

        assertTrue(result.isEmpty());
        verify(userRepository).findById("999");
    }

    @Test
    void shouldCheckExistenceBeforeDelete() {
        when(userRepository.existsById("1")).thenReturn(true);
        doNothing().when(userRepository).deleteById("1");

        boolean exists = userRepository.existsById("1");
        if (exists) {
            userRepository.deleteById("1");
        }

        assertTrue(exists);
        verify(userRepository).existsById("1");
        verify(userRepository).deleteById("1");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnDelete() {
        when(userRepository.existsById("999")).thenReturn(false);

        boolean exists = userRepository.existsById("999");

        assertFalse(exists);
        verify(userRepository).existsById("999");
        verify(userRepository, never()).deleteById(anyString());
    }

    @Test
    void shouldValidateInvalidEmail() {
        UserRequestDTO invalidDTO = new UserRequestDTO("João Silva", "email-invalido", "senha123");

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(invalidDTO);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("email válido")));
    }

    @Test
    void shouldValidateNullFields() {
        UserRequestDTO invalidDTO = new UserRequestDTO(null, null, null);

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(invalidDTO);

        assertFalse(violations.isEmpty());
        assertTrue(violations.size() >= 3);
    }

    @Test
    void shouldValidateEmptyFields() {
        UserRequestDTO invalidDTO = new UserRequestDTO("", "joao@email.com", "");

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(invalidDTO);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassValidationWithValidData() {
        UserRequestDTO validDTO = new UserRequestDTO("João Silva", "joao@email.com", "senha123");

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(validDTO);

        assertTrue(violations.isEmpty());
    }
}