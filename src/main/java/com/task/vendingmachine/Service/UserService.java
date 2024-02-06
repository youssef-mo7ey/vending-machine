package com.task.vendingmachine.Service;

import com.task.vendingmachine.Configuration.JwtService;
import com.task.vendingmachine.Models.DTO.LoginRequest;
import com.task.vendingmachine.Models.DTO.RegisterRequest;
import com.task.vendingmachine.Models.Role;
import com.task.vendingmachine.Models.User;
import com.task.vendingmachine.Repositories.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public User register(RegisterRequest req){
        Optional<User> user= userRepo.findByUsername(req.getUsername());
        if(user.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"This account already exists");
        }
        User newUser = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .build();
        return userRepo.save(newUser);

    }

    public String login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),loginRequest.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Optional<User> user= userRepo.findByUsername(loginRequest.getUsername());
        if(user.isEmpty()){
            return "invalid login";
        }
        return jwtService.generateToken(user.orElse(new User()));
    }

    public boolean deposit(HttpServletRequest req,Integer deposit){
        String token =req.getHeader("Authorization").substring(7);
        User user = jwtService.getUserFromToken(token);
        Integer [] vaildMoney={5,10,20,50,100};
        if (user.getDeposit() != null){
            return false;
        }
        for(Integer value : vaildMoney){
            if(Objects.equals(deposit, value)){
                user.setDeposit(deposit);
                userRepo.save(user);
                return true;
            }
        }
        return false;
    }
}
