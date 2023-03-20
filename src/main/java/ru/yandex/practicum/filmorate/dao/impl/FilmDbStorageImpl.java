package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
@Qualifier("fIlmDbStorageImpl")
public class FilmDbStorageImpl implements FilmDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RatingMpaDbStorageImpl ratingMpaDbStorage;
    private final GenreDbStorageImpl genreDbStorage;
    private final FilmLikesDbStorageImpl filmLikesDbStorage;

    @Autowired
    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate, RatingMpaDbStorageImpl ratingMpaDbStorage,
                             GenreDbStorageImpl genreDbStorage, FilmLikesDbStorageImpl filmLikesDbStorage) {

        this.jdbcTemplate = jdbcTemplate;
        this.ratingMpaDbStorage = ratingMpaDbStorage;
        this.genreDbStorage = genreDbStorage;
        this.filmLikesDbStorage = filmLikesDbStorage;
    }

    private RowMapper<Film> getFilmRowMapper() {
        return (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getLong("id"));
            film.setName(rs.getString("name"));
            film.setDuration(rs.getInt("duration"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());

            long mpaId = rs.getLong("RATING_ID");
            Optional<RatingMPA> optionalRatingMPA = ratingMpaDbStorage.getById(mpaId);
            optionalRatingMPA.ifPresent(film::setMpa);

            Optional<List<Genre>> optionalGenreList = genreDbStorage.getFilmsGenres(film.getId());
            if (optionalGenreList.isPresent()) {
                for (Genre genre : optionalGenreList.get()) {
                    film.getGenres().add(genre);
                }
            }
            return film;
        };
    }

    @Override
    public Optional<List<Film>> getAll() {
        String sql = "select * from films";

        return Optional.of(jdbcTemplate.query(sql, getFilmRowMapper()));
    }

    @Override
    public Optional<Film> getById(long id) {
        String sql = "select * from films where id = ?";
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet(sql, id);

        // обрабатываем результат выполнения запроса
        if (filmRow.next()) {
            Film film = new Film();
            film.setId(filmRow.getLong("id"));
            film.setName(filmRow.getString("name"));

            film.setReleaseDate(Objects.requireNonNull(filmRow.getDate("release_date")).toLocalDate());
            film.setDescription(filmRow.getString("description"));
            film.setDuration(filmRow.getInt("duration"));

            RatingMPA mpa = ratingMpaDbStorage.getById(filmRow.getInt("rating_id")).get();
            film.setMpa(mpa);

            Optional<List<Genre>> optionalGenreList = genreDbStorage.getFilmsGenres(id);
            if (optionalGenreList.isPresent()) {
                for (Genre genre : optionalGenreList.get()) {
                    film.getGenres().add(genre);
                }
            }
            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void add(Film film) {
        String sql = "insert into FILMS (NAME, RATING_ID, RELEASE_DATE, DESCRIPTION, DURATION) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getMpa().getId(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration());

        Film lastFilm = getLastAddedFilm();

        film.setId(lastFilm.getId());
        genreDbStorage.addFilmGenres(film);
    }

    @Override
    public Film getLastAddedFilm() {
        return jdbcTemplate.query("select * from films order by id desc limit 1", getFilmRowMapper()).
                get(0);
    }

    @Override
    public void update(Film film) {
        genreDbStorage.deleteFilmGenres(film.getId());
        genreDbStorage.addFilmGenres(film);

        String sql = "update films set NAME = ?, RATING_ID = ?,  RELEASE_DATE = ?, DESCRIPTION = ?, DURATION = ? " +
                "where id = ?";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getMpa().getId(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getId());
    }

    @Override
    public void deleteAll() {
        genreDbStorage.deleteAll();
        filmLikesDbStorage.deleteAll();
        String sql = "delete from FILMS CASCADE;" +
                "ALTER TABLE FILMS ALTER COLUMN ID RESTART WITH 1";
        jdbcTemplate.update(sql);
    }

    @Override
    public void deleteById(long id) {
        genreDbStorage.deleteFilmGenres(id);
        filmLikesDbStorage.deleteFilmLikes(id);
        String sql = "delete from FILMS where ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void addLike(long filmId, long userId) {
        filmLikesDbStorage.addLike(filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        filmLikesDbStorage.removeLike(filmId, userId);
    }

    @Override
    public Optional<List<Film>> getTopLikedFilms(long max) {
        String sql = "SELECT f.* " +
                "FROM FILMS f " +
                "LEFT JOIN FILM_LIKES fl ON f.ID = fl.FILM_ID  " +
                "GROUP BY F.ID " +
                "ORDER BY COUNT(fl.USER_ID) DESC " +
                "LIMIT ?";
        List<Film> topFilms = jdbcTemplate.query(sql, getFilmRowMapper(), max);
        return Optional.of(topFilms);
    }
}
