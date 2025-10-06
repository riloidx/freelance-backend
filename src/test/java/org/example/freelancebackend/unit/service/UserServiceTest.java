package org.example.freelancebackend.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matvey.freelancebackend.users.entity.User;
import org.matvey.freelancebackend.users.repository.UserRepository;
import org.matvey.freelancebackend.users.service.UserServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testFindByUsername() {
        User user = User.builder()
                .id(1L)
                .username("test")
                .name("Test")
                .passwordHash("test")
                .email("test@test.com")
                .build();

        when(userRepository.save(user)).thenReturn(user);

        UserService userService = new UserServiceImpl(userRepository);
        User result = userService.findUserById(1L);

        assertEquals(1L, result.getId());
    }
}
