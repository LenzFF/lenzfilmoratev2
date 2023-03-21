package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GenreController {
    public final GenreService genreService;

    @GetMapping("/genres")
    public List<Genre> getAll() {
        Optional<List<Genre>> genres = genreService.getAll();
        log.info("Запрошен список всех жанров");
        return genres.orElse(null);
    }

    @GetMapping("/genres/{id}")
    public Genre get(@PathVariable long id) {
        Optional<Genre> genre = genreService.getById(id);
        log.info("Запрошен жанр - {}", id);
        return genre.orElse(null);
    }
}
