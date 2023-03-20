package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDbStorage {
    Optional<List<Film>> getAll();

    Optional<Film> getById(long id);

    void add(Film film);

    Film getLastAddedFilm();

    void update(Film film);

    void deleteAll();

    void deleteById(long id);

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    Optional<List<Film>> getTopLikedFilms(long max);
}
