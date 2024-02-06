package com.task.vendingmachine.Controller;

import com.task.vendingmachine.Models.DTO.Deposit;
import com.task.vendingmachine.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(HttpServletRequest req, @RequestBody Deposit deposit){

        if (userService.deposit(req,deposit.getDeposit())){
            return ResponseEntity.ok("User made a deposit with "+deposit.getDeposit().toString());
        }
        return ResponseEntity.badRequest().body("Something wrong!!!");
    }
}
