package com.kotkina.quotesServiceApi.services;

import com.kotkina.quotesServiceApi.entities.User;
import com.kotkina.quotesServiceApi.exceptions.EntityNotFoundException;
import com.kotkina.quotesServiceApi.exceptions.UserAlreadyExists;
import com.kotkina.quotesServiceApi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(User user) {
        if (userRepository.findUserByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExists("You can't use this email. Choose another one");
        }
        return userRepository.save(user);
    }

    public User getById(long userId) throws EntityNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("No users found"));
    }
}
