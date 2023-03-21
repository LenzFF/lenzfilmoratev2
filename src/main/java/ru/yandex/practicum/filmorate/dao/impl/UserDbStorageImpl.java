package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Component
@Qualifier("userDbStorageImpl")
public class UserDbStorageImpl implements UserDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<User> getUserRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            user.setEmail(rs.getString("email"));
            return user;
        };
    }

    @Override
    public User getLastAddedUser() {
        return jdbcTemplate.query("select * from users order by id desc limit 1", getUserRowMapper()).
                get(0);
    }

    @Override
    public Optional<List<User>> getAll() {
        String sql = "select * from users";
        List<User> list = jdbcTemplate.query(sql, getUserRowMapper());

        return Optional.of(list);
    }

    @Override
    public Optional<User> getById(long id) {
        String sql = "select * from users where id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);

        // обрабатываем результат выполнения запроса
        if (userRows.next()) {
            User user = new User();
            user.setId(userRows.getLong("id"));
            user.setLogin(userRows.getString("login"));
            user.setName(userRows.getString("name"));
            user.setBirthday(userRows.getDate("BIRTHDAY").toLocalDate());
            user.setEmail(userRows.getString("email"));
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void add(User user) {
        String sql = "insert into USERS (LOGIN, NAME, BIRTHDAY, EMAIL) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getEmail());
    }

    @Override
    public void update(User user) {
        String sql = "update USERS set LOGIN = ?, NAME = ?, BIRTHDAY = ?, EMAIL = ? where id = ?";
        jdbcTemplate.update(sql,
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getEmail(),
                user.getId());
    }

    @Override
    public void deleteAll() {
        String sql = "delete from FRIENDSHIP CASCADE; " +
                "delete from users CASCADE;" +
                "ALTER TABLE FRIENDSHIP ALTER COLUMN ID RESTART WITH 1;" +
                "ALTER TABLE users ALTER COLUMN ID RESTART WITH 1;";
        jdbcTemplate.update(sql);
    }

    @Override
    public void deleteById(long id) {
        removeUserFromFriendshipTable(id);
        String sql = "delete from users where ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<List<User>> getUserFriends(long userId) {
        String sql = "select u.* " +
                "FROM USERS as u " +
                "JOIN FRIENDSHIP as fs on u.ID = fs.FRIEND_ID " +
                "WHERE fs.USER_ID = ?";
        List<User> friends = jdbcTemplate.query(sql, getUserRowMapper(), userId);
        return Optional.of(friends);
    }

    @Override
    public Optional<List<User>> getCommonFriends(long userId, long friendId) {
        String sql = "select u.* " +
                "FROM (select u.* " +
                "FROM USERS as u " +
                "JOIN FRIENDSHIP as fs on u.ID = fs.FRIEND_ID " +
                "WHERE FS.USER_ID = ?) as u " +
                "JOIN FRIENDSHIP as fs on u.ID = fs.FRIEND_ID " +
                "WHERE FS.USER_ID = ?";
        List<User> friends = jdbcTemplate.query(sql, getUserRowMapper(), userId, friendId);
        return Optional.of(friends);
    }

    @Override
    public void makeFriends(long userId, long friendId) {
        String sql = "insert into FRIENDSHIP (USER_ID, FRIEND_ID) values (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void removeFromFriends(long userId, long friendId) {
        String sql = "delete from FRIENDSHIP where USER_ID = ? and FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void removeUserFromFriendshipTable(long userId) {
        String sql = "delete from FRIENDSHIP where USER_ID = ? or FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, userId);
    }
}
