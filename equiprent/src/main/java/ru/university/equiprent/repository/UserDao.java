package ru.university.equiprent.repository;

import java.util.List;
import java.util.Optional;

import ru.university.equiprent.model.User;

public interface UserDao {
    User save(User user);
    List<User> findAll();
    Optional<User> findById(Long id);
    void delete(Long id);
}