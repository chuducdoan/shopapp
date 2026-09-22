package com.product.shoppapp.services;

import com.product.shoppapp.components.JwtTokenUtils;
import com.product.shoppapp.dtos.UserDTO;
import com.product.shoppapp.exceptions.DataNotFoundException;
import com.product.shoppapp.models.Role;
import com.product.shoppapp.models.User;
import com.product.shoppapp.repositories.RoleRepository;
import com.product.shoppapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        String phoneNumber = userDTO.getPhoneNumber();
        // kiểm tra tồn tại sđt
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        if (role.getRoleName().toUpperCase().equals(Role.ADMIN)) {
            throw new Exception("You cannot register an admin account");
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
        User user = existingUser.get();
        // check password
        if (user.getFacebookAccountId() == 0 && user.getGoogleAccountId() == 0) {
            if(!passwordEncoder.matches(password, user.getPassword())) {
                throw new DataNotFoundException("wrong phone number or password");
            }
        }

        if (!user.isActive()) {
            throw new Exception("User is not active");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phoneNumber, password, user.getAuthorities());
        // authentication with Java Sprong security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtils.generateToken(user);
    }

    @Override
    public User getUserDetailsFromToken(String token) throws Exception {
        if(jwtTokenUtils.isTokenExpired(token)) {
            throw new Exception("Token is expired");
        }
        String phoneNumber = jwtTokenUtils.extractPhoneNumber(token);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if(user.isPresent()) {
            return user.get();
        } else {
            throw new Exception("User not found");
        }
    }
}
