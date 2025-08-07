package com.sesac.userservice.controller;

import com.sesac.userservice.entity.User;
import com.sesac.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "사용자 조회", description = "ID로 사용자를 조회합니다.")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User byId = userService.findById(id);
            return ResponseEntity.ok(byId);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
