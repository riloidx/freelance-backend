package org.example.freelancebackend.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matvey.freelancebackend.roles.entity.Role;
import org.matvey.freelancebackend.roles.service.api.RoleService;
import org.matvey.freelancebackend.security.dto.request.RegistrationDto;
import org.matvey.freelancebackend.users.dto.request.UpdateUserDto;
import org.matvey.freelancebackend.users.dto.response.UserResponseDto;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.mapper.UserMapper;
import org.matvey.freelancebackend.users.repository.UserRepository;
import org.matvey.freelancebackend.users.service.UserServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;


    @Test
    public void testFindById() {
        User user = createBasicUser();
        when(userRepo.findById(1L)).
                thenReturn(Optional.of(user));

        User result = userService.findUserById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    public void testFindByUsername() {
        User user = createBasicUser();
        when(userRepo.findByUsername(user.getUsername())).
                thenReturn(Optional.of(user));

        User result = userService.findUserByUsername(user.getUsername());

        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    public void testFindByEmail() {
        User user = createBasicUser();
        when(userRepo.findByEmail(user.getEmail())).
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
        Role role = new Role();
        role.setId(1L);
        role.setName("USER");

        when(userMapper.toEntity(dto)).thenReturn(user);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("test");
        when(roleService.findRoleByName("USER")).thenReturn(role);
        when(userRepo.save(user)).thenReturn(user);
        when(userRepo.save(user)).thenReturn(user);

        User result = userService.create(dto);

        assertEquals(1L, result.getId());
    }

    @Test
    public void updateUser() {
        String email = "test@test.com";
        User user = createBasicUser();

        UpdateUserDto dto = UpdateUserDto.builder()
                .name("Updated Name")
                .description("Updated Description")
                .build();

        User updatedUser = User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(dto.getName())
                .description(dto.getDescription())
                .email(user.getEmail())
                .passwordHash(user.getPasswordHash())
                .build();

        UserResponseDto responseDto = UserResponseDto.builder()
                .id(updatedUser.getId())
                .username(updatedUser.getUsername())
                .name(updatedUser.getName())
                .email(updatedUser.getEmail())
                .build();

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        doNothing().when(userMapper).updateEntityFromDto(dto, user);
        when(userRepo.save(user)).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(responseDto);

        UserResponseDto result = userService.update(email, dto);

        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getDescription(), updatedUser.getDescription());
        assertEquals(user.getId(), result.getId());
    }

    @Test
    public void deleteTest() {
        User user = createBasicUser();
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        userService.delete(user.getId());

        verify(userRepo).delete(user);
    }


    private User createBasicUser() {
        return User.builder()
                .id(1L)
                .username("test")
                .name("Test")
                .passwordHash("test")
                .email("test@test.com")
                .roles(new HashSet<>())
                .build();
    }
}
