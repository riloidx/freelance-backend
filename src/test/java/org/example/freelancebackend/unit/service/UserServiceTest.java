package org.example.freelancebackend.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matvey.freelancebackend.security.dto.request.RegistrationDto;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.mapper.UserMapper;
import org.matvey.freelancebackend.users.repository.UserRepository;
import org.matvey.freelancebackend.users.service.UserServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    public void testFindById() {
        User user = createBasicUser();
        when(userRepository.findById(1L)).
                thenReturn(Optional.of(user));

        User result = userService.findUserById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    public void testFindByUsername() {
        User user = createBasicUser();
        when(userRepository.findByUsername(user.getUsername())).
                thenReturn(Optional.of(user));

        User result = userService.findUserByUsername(user.getUsername());

        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    public void testFindByEmail() {
        User user = createBasicUser();
        when(userRepository.findByEmail(user.getEmail())).
                thenReturn(Optional.of(user));

        User result = userService.findUserByEmail(user.getEmail());

        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    public void createUser() {
        RegistrationDto dto = RegistrationDto.builder()
                .username("test")
                .name("Test")
                .password("test")
                .email("test@test.com")
                .build();

        User user = createBasicUser();

        when(userMapper.toEntity(dto)).thenReturn(user);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("test");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.create(dto);

        assertEquals(1L, result.getId());
    }

    private User createBasicUser() {
        return User.builder()
                .id(1L)
                .username("test")
                .name("Test")
                .passwordHash("test")
                .email("test@test.com")
                .build();
    }
}
