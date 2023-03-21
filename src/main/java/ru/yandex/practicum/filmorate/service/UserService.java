package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorageImpl;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class UserService {
    @Qualifier("userDbStorageImpl")
    private UserDbStorageImpl userStorage;

    @Autowired
    public UserService(UserDbStorageImpl userStorage) {
        this.userStorage = userStorage;
    }

    private User getUserOrThrowException(Long userId) {
        Optional<User> user = userStorage.getById(userId);
        return user.orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public long getLastId() {
        return userStorage.getLastAddedUser().getId();
    }

    public User get(long id) {
        return getUserOrThrowException(id);
    }

    public List<User> getAll() {
        return userStorage.getAll().orElse(new ArrayList<>());
    }

    public void create(User user) {
        validate(user);
        userStorage.add(user);
    }

    public void update(User user) {
        getUserOrThrowException(user.getId());
        validate(user);
        userStorage.update(user);
    }

    public void delete(long id) {
        getUserOrThrowException(id);
        userStorage.deleteById(id);
    }

    public void makeFriends(long userId, long friendId) {
        getUserOrThrowException(userId);
        getUserOrThrowException(friendId);

        userStorage.makeFriends(userId, friendId);
    }


    public void removeFromFriends(long userId, long friendId) {
        getUserOrThrowException(userId);
        getUserOrThrowException(friendId);

       userStorage.removeFromFriends(userId, friendId);
    }

    public List<User> getUsersFriends(Long userId) {
        getUserOrThrowException(userId);
        Optional<List<User>> optionalFriends = userStorage.getUserFriends(userId);
        return optionalFriends.orElse(new ArrayList<>());
    }


    public List<User> getCommonFriends(Long userId, Long friendId) {
        getUserOrThrowException(userId);
        getUserOrThrowException(friendId);

        return userStorage.getCommonFriends(userId, friendId).orElse(new ArrayList<>());
    }

    protected void validate(User user) {
        if (user.getName() == null || user.getName().isEmpty()) user.setName(user.getLogin());
        if (user.getLogin() == null || user.getBirthday() == null)
            throw new ValidationException("Ошибка валидации пользователя");

        if (!(user.getBirthday().isBefore(LocalDate.now()) &&
                user.getEmail().contains("@") &&
                !user.getEmail().isEmpty() &&
                !user.getLogin().isEmpty() &&
                !user.getLogin().contains(" "))) {
            log.info("Ошибка валидации пользователя - {}", user);
            throw new ValidationException("Ошибка валидации пользователя");
        }
    }
}
