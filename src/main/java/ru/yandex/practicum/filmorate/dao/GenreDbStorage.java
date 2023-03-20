package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDbStorage {
    Optional<List<Genre>> getAll();
    Optional<Genre> getById(long id);
    Optional<List<Genre>> getFilmsGenres(long filmId);
    void addFilmGenres(Film film);
    void deleteFilmGenres(long filmId);
    void deleteAll();
}
