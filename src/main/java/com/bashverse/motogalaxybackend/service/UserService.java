package com.bashverse.motogalaxybackend.service;

import com.bashverse.motogalaxybackend.exception.UserException;
import com.bashverse.motogalaxybackend.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public User findUserProfileByJwt(String jwt) throws UserException;

    public User findUserByEmail(String email) throws UserException;
}
