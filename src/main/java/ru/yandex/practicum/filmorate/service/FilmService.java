package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorageImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorageImpl;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {

    @Qualifier("fIlmDbStorageImpl")
    private FilmDbStorageImpl fIlmStorage;
    @Qualifier("userDbStorageImpl")
    private UserDbStorageImpl userStorage;

    @Autowired
    public FilmService(FilmDbStorageImpl filmStorage, UserDbStorageImpl userStorage) {
        this.fIlmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    private Film getFilmOrThrowException(long id) {
        Optional<Film> film = fIlmStorage.getById(id);
        if (film.isEmpty()) {
            log.info("Фильм не найден - {}", id);
            throw new NotFoundException("Фильм не найден");
        }
        return film.get();
    }

    private User getUserOfThrowException(long id) {
        Optional<User> user = userStorage.getById(id);
        if (user.isEmpty()) {
            log.info("Пользователь не найден - {}", id);
            throw new NotFoundException("Пользователь не найден");
        }
        return user.get();
    }

    public long getLastId() {
        return fIlmStorage.getLastAddedFilm().getId();
    }

    public void addLike(Long filmId, Long userId) {
        getFilmOrThrowException(filmId);
        getUserOfThrowException(userId);
        fIlmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        getFilmOrThrowException(filmId);
        getUserOfThrowException(userId);
        fIlmStorage.removeLike(filmId, userId);
    }

    public void create(Film film) {
        validate(film);
        fIlmStorage.add(film);
    }

    public List<Film> topLikedFilms(Long max) {
        return fIlmStorage.getTopLikedFilms(max).get();
    }

    public Film get(long id) {
        return getFilmOrThrowException(id);
    }

    public List<Film> getAll() {
        return fIlmStorage.getAll().orElse(new ArrayList<>());
    }

    public void update(Film film) {
        getFilmOrThrowException(film.getId());
        validate(film);
        fIlmStorage.update(film);
    }

    public void delete(long id) {
        getFilmOrThrowException(id);
        fIlmStorage.deleteById(id);
    }

    protected void validate(Film film) {
        if (film.getName() == null ||
                film.getDescription() == null ||
                film.getReleaseDate() == null)
            throw new ValidationException("Ошибка валидации фильма");

        if (!(film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 27)) &&
                !film.getName().isEmpty() &&
                film.getDescription().length() <= 200 &&
                film.getDuration() > 0)) {
            log.info("Ошибка валидации фильма - {}", film);
            throw new ValidationException("Ошибка валидации фильма");
        }
    }
}
