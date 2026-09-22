package com.product.shoppapp.controllers;

import com.product.shoppapp.dtos.UserDTO;
import com.product.shoppapp.dtos.UserLoginDTO;
import com.product.shoppapp.models.User;
import com.product.shoppapp.responses.LoginResponse;
import com.product.shoppapp.responses.RegisterResponse;
import com.product.shoppapp.responses.UserResponse;
import com.product.shoppapp.services.UserService;
import com.product.shoppapp.components.LocalizationUtils;
import com.product.shoppapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result)  {
       try {
           if (result.hasErrors()) {
               List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
               return ResponseEntity.badRequest().body(RegisterResponse.builder()
                       .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_FAILED))
                       .build()
               );
           }
           if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
               return ResponseEntity.badRequest().body(
                       RegisterResponse.builder()
                               .message(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORDS_DO_NOT_MATCH))
                               .build()
               );
           }
           User user = userService.createUser(userDTO);
           return ResponseEntity.ok(
                   RegisterResponse.builder().message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY))
                           .user(user)
                           .build()
           );
       } catch (Exception e) {
           return ResponseEntity.badRequest().body(RegisterResponse.builder()
                   .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED, e.getMessage()))
                   .build());
       }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userDTO) {
        try {
            String token = userService.login(userDTO.getPhoneNumber(), userDTO.getPassword());
            return ResponseEntity.ok(LoginResponse.builder()
                    .token(token)
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                    .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED, e.getMessage()))
                    .build());
        }
    }

    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String token) {
        try {
            String extractedToken = token.substring(7);
            User user = userService.getUserDetailsFromToken(extractedToken);
            return ResponseEntity.ok(UserResponse.fromUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
