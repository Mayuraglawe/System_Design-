
package com.example.demo.service.impl;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    public User createUser(User user) { return repo.save(user); }
    public List<User> getAllUsers() { return repo.findAll(); }
    public User getUserById(Long id) { return repo.findById(id).orElseThrow(); }
    public User updateUser(Long id, User user) {
        User u = getUserById(id);
        u.setName(user.getName());
        u.setEmail(user.getEmail());
        return repo.save(u);
    }
    public void deleteUser(Long id) { repo.deleteById(id); }
}
