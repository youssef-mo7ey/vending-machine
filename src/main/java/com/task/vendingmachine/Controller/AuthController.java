package com.task.vendingmachine.Controller;

//import com.task.vendingmachine.Configuration.AuthenticationResponse;
import com.task.vendingmachine.Models.DTO.LoginRequest;
import com.task.vendingmachine.Models.DTO.RegisterRequest;
import com.task.vendingmachine.Models.User;
import com.task.vendingmachine.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest req){
        return userService.register(req);
    }

    @GetMapping("/hello")
    public String hello()
    {
        return "hello";
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest req){
        String token = userService.login(req);
        return ResponseEntity.ok(token);
    }
}
