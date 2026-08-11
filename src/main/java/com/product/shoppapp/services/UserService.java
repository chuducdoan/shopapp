package com.product.shoppapp.services;

import com.product.shoppapp.dtos.UserDTO;
import com.product.shoppapp.exceptions.DataNotFoundException;
import com.product.shoppapp.models.Role;
import com.product.shoppapp.models.User;
import com.product.shoppapp.repositories.RoleRepository;
import com.product.shoppapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
        String phoneNumber = userDTO.getPhoneNumber();
        // kiểm tra tồn tại sđt
        boolean isExist = userRepository.existsByPhoneNumber(phoneNumber);
        if (isExist) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        // convert userDTO to user
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        newUser.setRole(role);
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);

        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) throws Exception {
        Optional<User> existingUser = userRepository.findByPhoneNumber(phoneNumber);
        if (existingUser.isEmpty()) {
            throw new DataNotFoundException("Invalid phone number / password");
        }

        return "Login successful";
    }
}
