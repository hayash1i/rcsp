package ru.university.equiprent.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.university.equiprent.model.User;
import ru.university.equiprent.service.IdGeneratorService;

@RequiredArgsConstructor
@Repository
public class UserRepository implements UserDao {
    private final IdGeneratorService idGeneratorService;
    private final Map<Long, User> storage = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGeneratorService.nextUserId());
        }
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> findAll() { return List.copyOf(storage.values()); }

    @Override
    public Optional<User> findById(Long id) { return Optional.ofNullable(storage.get(id)); }

    @Override
    public void delete(Long id) { storage.remove(id); }
}