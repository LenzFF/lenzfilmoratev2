package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmLikesDbStorage;

@Repository
public class FilmLikesDbStorageImpl implements FilmLikesDbStorage {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmLikesDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(long filmId, long userId) {
        String sql = "insert into FILM_LIKES (FILM_ID, USER_ID) values (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        String sql = "delete from FILM_LIKES where FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteFilmLikes(long filmId) {
        String sql = "delete from FILM_LIKES where FILM_ID = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public void deleteUserLikes(long userId) {
        String sql = "delete from FILM_LIKES where USER_ID = ?";
        jdbcTemplate.update(sql, userId);
    }

    @Override
    public void deleteAll() {
        String sql = "delete from FILM_LIKES CASCADE;" +
                "ALTER TABLE FILM_LIKES ALTER COLUMN ID RESTART WITH 1;";
        jdbcTemplate.update(sql);
    }

    @Override
    public long getFilmLikesCount(long filmId) {
        SqlRowSet sql = jdbcTemplate.queryForRowSet("select COUNT(*) AS i FROM FILM_LIKES where FILM_ID = ?",
                filmId);
        if (sql.next()) {
            return sql.getLong("i");
        }
        return 0;
    }
}
