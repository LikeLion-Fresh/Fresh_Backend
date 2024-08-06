package com.example.demo.controller;

import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user/subscribe")
    public ResponseEntity<?> subscribe(Principal principal) {
        try {
            userService.updateUserRole(principal.getName(), "ROLE_SUB");
            return ResponseEntity.ok("구독 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("구독 중 오류 발생: " + e.getMessage());
        }
    }

    @PostMapping("/user/unsubscribe")
    public ResponseEntity<?> unsubscribe(Principal principal) {
        try {
            userService.updateUserRole(principal.getName(), "ROLE_USER");
            return ResponseEntity.ok("구독 취소 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("구독 취소 중 오류 발생: " + e.getMessage());
        }
    }

    @GetMapping("/login/check")
    public ResponseEntity<Boolean> checkLogin(Principal principal) {
        if(principal == null) {
            return ResponseEntity.ok(Boolean.FALSE);
        } else {
            return ResponseEntity.ok(Boolean.TRUE);
        }
    }
}
