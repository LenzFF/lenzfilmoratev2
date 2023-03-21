package ru.yandex.practicum.filmorate.dao;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDbStorage {
    User getLastAddedUser();

    Optional<List<User>> getAll();

    Optional<User> getById(long id);

    void add(User user);

    void update(User user);

    void deleteAll();

    void deleteById(long id);

    Optional<List<User>> getUserFriends(long userId);

    Optional<List<User>> getCommonFriends(long userId, long friendId);

    void makeFriends(long userId, long friendId);

    void removeFromFriends(long userId, long friendId);

    void removeUserFromFriendshipTable(long userId);
}
