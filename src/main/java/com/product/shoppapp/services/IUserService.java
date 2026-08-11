package com.product.shoppapp.services;

import com.product.shoppapp.dtos.UserDTO;
import com.product.shoppapp.exceptions.DataNotFoundException;
import com.product.shoppapp.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;
    String login(String phoneNumber, String password) throws Exception;
}
