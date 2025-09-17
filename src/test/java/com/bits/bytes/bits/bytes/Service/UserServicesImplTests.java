package com.bits.bytes.bits.bytes.Service;


import com.bits.bytes.bits.bytes.DTOs.UsersDTO;
import com.bits.bytes.bits.bytes.MapDTOs.MapDTOs;
import com.bits.bytes.bits.bytes.Models.Users;
import com.bits.bytes.bits.bytes.Repo.UserRepo;
import com.bits.bytes.bits.bytes.Services.Impl.UserServicesImpl;
import com.github.javafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServicesImplTests {

    // The Repo I want to mock
    @Mock
    private UserRepo userRepo;

    // Injecting the service layer I want to test
    @InjectMocks
    private UserServicesImpl userServicesImpl;

    private Users testUser;
    private UsersDTO testUserDto;

    @BeforeEach
    void setup() {
        Faker faker = new Faker();

        // Generate random test user
        String username = faker.name().username();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();

        testUser = new Users();
        testUser.setUsername(username);
        testUser.setEmail(email);
        testUser.setPassword_hash(password);

        testUserDto = UsersDTO.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();
    }

    @Test
    public void UserServices_Register_ReturnSuccess() {
        when(userRepo.save(Mockito.any(Users.class))).thenReturn(testUser);
        userServicesImpl.Register(testUserDto);
    }

    @Test
    public void UserServices_findUser_ReturnUser() {
            when(userRepo.findByUsername(testUserDto.getUsername())).thenReturn(testUser);
            // Act + Assert inside the static mock block
            try (MockedStatic<MapDTOs> mockedStatic = Mockito.mockStatic(MapDTOs.class)) {
                mockedStatic.when(() -> MapDTOs.mapToUsersDto(testUser))
                        .thenReturn(testUserDto);

                UsersDTO user = userServicesImpl.findUser(testUserDto.getUsername());

                Assertions.assertThat(user.getUsername()).isEqualTo(testUserDto.getUsername());
            }
        }
}
