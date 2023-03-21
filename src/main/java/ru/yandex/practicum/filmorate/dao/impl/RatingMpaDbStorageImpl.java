package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.RatingMpaDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.List;
import java.util.Optional;

@Repository
public class RatingMpaDbStorageImpl implements RatingMpaDbStorage {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RatingMpaDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<List<RatingMPA>> getAll() {
        String sql = "select * from rating_mpa";
        return Optional.of(jdbcTemplate.query(sql, getRatingMpaRowMapper()));
    }

    @Override
    public Optional<RatingMPA> getById(long id) {
        String sql = "select * from rating_mpa where id = ?";
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(sql, id);

        // обрабатываем результат выполнения запроса
        if (mpaRow.next()) {
            RatingMPA mpa = new RatingMPA();
            mpa.setId(mpaRow.getLong("id"));
            mpa.setName(mpaRow.getString("name"));
            return Optional.of(mpa);
        } else {
            return Optional.empty();
        }
    }

    private RowMapper<RatingMPA> getRatingMpaRowMapper() {
        return (rs, rowNum) -> {
            RatingMPA ratingMPA = new RatingMPA();
            ratingMPA.setId(rs.getInt("id"));
            ratingMPA.setName(rs.getString("name"));
            return ratingMPA;
        };
    }
}
