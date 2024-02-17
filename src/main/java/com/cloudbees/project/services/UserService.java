package com.cloudbees.project.services;

import com.cloudbees.project.exceptions.CustomException;
import com.cloudbees.project.exceptions.ErrorCode;
import com.cloudbees.project.models.User;
import com.cloudbees.project.models.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) throws CustomException {
        validateUser(user);
        if(userRepository.existsByEmail(user.getEmail())) throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);

        return userRepository.save(user);
    }

    public List<User> listUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User getUser(String idString) throws CustomException {
        long id = userIdFromString(idString);
        if(!userRepository.existsById(id)) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        return userRepository.findById(id).orElse(null);
    }

    public User editUser(User user, String idString) throws CustomException {
        long id = userIdFromString(idString);
        if(!Objects.equals(id, user.getId())) throw new CustomException(ErrorCode.USER_ID_MISMATCH);
        validateUser(user);
        User prevUser = userRepository.findById(id).orElse(null);
        if(Objects.isNull(prevUser)) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        if(!user.getEmail().equals(prevUser.getEmail())) throw new CustomException(ErrorCode.EMAIL_MISMATCH);

        return userRepository.save(user);
    }

    public User deleteUser(String idString) throws CustomException {
        long id = userIdFromString(idString);
        User user = userRepository.findById(id).orElse(null);
        if(Objects.isNull(user)) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        userRepository.delete(user);
        return user;
    }

    public long userIdFromString(String userIdString) throws CustomException{
        try {
            return Long.parseLong(userIdString);
        } catch (NumberFormatException e) {
            throw new CustomException(ErrorCode.INVALID_USER_ID);
        }
    }

    private void validateUser(User user) throws CustomException {
        if(Objects.isNull(user.getEmail())) throw new CustomException(ErrorCode.EMAIL_NOT_PROVIDED);
    }
}
