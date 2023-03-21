package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    public final UserService userService;

    @GetMapping("/users")
    public List<User> getAll() {
        List<User> users = userService.getAll();
        log.info("Запрошен список всех пользователей");
        return users;
    }

    @GetMapping("/users/{id}")
    public User get(@PathVariable long id) {
        return userService.get(id);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getUserFriends(@PathVariable long id) {
        List<User> friends = userService.getUsersFriends(id);
        log.info("Запрошен список всех друзей пользователя - {}", id);
        return friends;
    }

    @GetMapping("/users/{userId}/friends/common/{friendId}")
    public List<User> getCommonFriends(@PathVariable long userId, @PathVariable long friendId) {
        List<User> commonFriends = userService.getCommonFriends(userId, friendId);
        log.info("Запрошен список общих друзей пользователей - {}, {}", userId, friendId);
        return commonFriends;
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        userService.create(user);
        log.info("Добавлен пользователь - {}", user);
        user.setId(userService.getLastId());
        return user;
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        userService.update(user);
        log.info("Добавлен обновлен - {}", user);
        return user;
    }

    @PutMapping("/users/{userId}/friends/{friendId}")
    public void makeFriends(@PathVariable long userId, @PathVariable long friendId) {
        userService.makeFriends(userId, friendId);
        log.info("Пользователи - {} и {} стали друзьями", userId, friendId);
    }

    @DeleteMapping("/users/{userId}/friends/{friendId}")
    public void removeFromFriends(@PathVariable long userId, @PathVariable long friendId) {
        userService.removeFromFriends(userId, friendId);
        log.info("Пользователи - {} и {} больше не друзья", userId, friendId);
    }
}
