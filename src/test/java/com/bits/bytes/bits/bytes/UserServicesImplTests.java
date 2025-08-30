package com.bits.bytes.bits.bytes;


import com.bits.bytes.bits.bytes.Repo.UserRepo;
import com.bits.bytes.bits.bytes.Services.Impl.UserServicesImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServicesImplTests {

    // The Repo I want to mock
    @Mock
    private UserRepo userRepo;

    // Injecting the service layer I want to test
    @InjectMocks
    private UserServicesImpl userServicesImpl;

    @Test
    public void UserServices_Register_ReturnTrue() {

    }
}
