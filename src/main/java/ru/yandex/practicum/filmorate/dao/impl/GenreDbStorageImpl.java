package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class GenreDbStorageImpl implements GenreDbStorage {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<List<Genre>> getAll() {
        String sql = "select * from genres";
        List<Genre> list = jdbcTemplate.query(sql, getGenreRowMapper());

        return Optional.of(list);
    }

    @Override
    public Optional<Genre> getById(long id) {
        String sql = "select * from genres where id = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sql, id);

        // обрабатываем результат выполнения запроса
        if (genreRows.next()) {
            Genre genre = new Genre();
            genre.setId(genreRows.getLong("id"));
            genre.setName(genreRows.getString("name"));
            return Optional.of(genre);
        } else {
            return Optional.empty();
        }
    }

    private RowMapper<Genre> getGenreRowMapper() {
        return (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("id"));
            genre.setName(rs.getString("name"));
            return genre;
        };
    }

    @Override
    public Optional<List<Genre>> getFilmsGenres(long filmId) {
        String sql = "select g.ID, g.NAME " +
                "FROM FILM_GENRES as fl " +
                "left join GENRES AS g on fl.GENRE_ID = g.ID " +
                "WHERE fl.FILM_ID = ?";

        List<Genre> list = jdbcTemplate.query(sql, getGenreRowMapper(), filmId);
        return Optional.of(list);
    }

    @Override
    public void addFilmGenres(Film film) {
        String sql = "insert into FILM_GENRES (FILM_ID, GENRE_ID) values (?, ?)";
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sql, film.getId(), genre.getId());
            }
        }
    }

    @Override
    public void deleteFilmGenres(long filmId) {
        String sql = "delete from FILM_GENRES where FILM_ID = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public void deleteAll() {
        String sql = "delete from FILM_GENRES CASCADE;" +
                "ALTER TABLE FILM_GENRES ALTER COLUMN ID RESTART WITH 1";
        jdbcTemplate.update(sql);
    }
}
