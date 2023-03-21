package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/films")
    public List<Film> getAll() {
        List<Film> films = filmService.getAll();
        log.info("Запрошен список всех фильмов");
        return films;
    }

    @GetMapping("/films/popular")
    public List<Film> getTopLikedFilms(@RequestParam(defaultValue = "10") long count) {
        List<Film> films = filmService.topLikedFilms(count);
        log.info("Запрошен список лучших фильмов");
        return films;
    }

    @GetMapping("/films/{filmId}")
    public Film get(@PathVariable long filmId) {
        return filmService.get(filmId);
    }


    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        filmService.create(film);
        log.info("Добавлен фильм - {}", film);
        film.setId(filmService.getLastId());
        return film;
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        filmService.update(film);
        log.info("Обновлен фильм - {}", film);
        return film;
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public void addLike(@PathVariable long filmId, @PathVariable long userId) {
        filmService.addLike(filmId, userId);
        log.info("Фильму {} поставил лайк пользователь - {}", filmId, userId);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public void removeLike(@PathVariable long filmId, @PathVariable long userId) {
        filmService.removeLike(filmId, userId);
        log.info("Пользователь {} удалил лайк у фильма - {}", userId, filmId);
    }
}
