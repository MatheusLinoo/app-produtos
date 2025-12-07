package br.com.equipe4.app_produtos.service;

import br.com.equipe4.app_produtos.infra.exceptions.EntityNotFoundException;
import br.com.equipe4.app_produtos.mapper.UserMapper;
import br.com.equipe4.app_produtos.model.User;
import br.com.equipe4.app_produtos.repository.UserRepository;
import br.com.equipe4.app_produtos.service.dto.UserResponseDTO;
import br.com.equipe4.app_produtos.service.dto.request.UserRequestDTO;
import br.com.equipe4.app_produtos.service.dto.response.UsuarioResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);
        
        user.setPassword(passwordEncoder.encode(dto.password()));
        
       
        user.setUsername(dto.email());

        user.setRole(br.com.equipe4.app_produtos.model.UserRole.CUSTOMER);

        return userMapper.toResponseDto(userRepository.save(user));
    }

    public List<UserResponseDTO> listUsers() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            log.warn("No users found in database!");
            return Collections.emptyList();
        }
        return users.stream().map(userMapper::toResponseDto).toList();
    }

    public void updateUser(String id, UserRequestDTO dto) { 
        userRepository.findById(id).map(user -> {
            user.setName(dto.name());
            user.setEmail(dto.email());
    
            if (dto.password() != null && !dto.password().isBlank()) {
                user.setPassword(passwordEncoder.encode(dto.password()));
            }
            return userRepository.save(user);
        }).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado!");
        }
        userRepository.deleteById(id);
    }

}
