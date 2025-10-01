package com.bits.bytes.bits.bytes.Controllers;
import com.bits.bytes.bits.bytes.DTOs.UsersDTO;
import com.bits.bytes.bits.bytes.Services.Impl.UserServicesImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    UserServicesImpl userServicesImpl;
    LoginController(UserServicesImpl userServicesImpl) {
        this.userServicesImpl = userServicesImpl;
    }

    @PutMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UsersDTO user) {
        userServicesImpl.Register(user);
        logger.info("Successfully Registered New User");
        return new ResponseEntity<>("Successfully Registered New User", HttpStatus.OK);
    }


//    @GetMapping("/csrf-token")
//    public CsrfToken csrfToken(HttpServletRequest request) {
//        return (CsrfToken) request.getAttribute("_csrf");
//    }

}
