package com.bits.bytes.bits.bytes.Controllers;
import com.bits.bytes.bits.bytes.Models.Users;
import com.bits.bytes.bits.bytes.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    UserServices userServices;

    @PutMapping("/register")
    public String registerUser(@RequestBody Users user) {
        userServices.Register(user);
        return "ok";
    }


//    @GetMapping("/csrf-token")
//    public CsrfToken csrfToken(HttpServletRequest request) {
//        return (CsrfToken) request.getAttribute("_csrf");
//    }

}
