package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.service.RatingMpaService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RatingMpaController {
    public final RatingMpaService ratingMpaService;

    @GetMapping("/mpa")
    public List<RatingMPA> getAll() {
        Optional<List<RatingMPA>> ratingMPAS = ratingMpaService.getAll();
        log.info("Запрошен список всех рейтингов MPA");
        return ratingMPAS.orElse(null);
    }

    @GetMapping("/mpa/{id}")
    public RatingMPA get(@PathVariable long id) {
        Optional<RatingMPA> ratingMPA = ratingMpaService.getById(id);
        log.info("Запрошен рейтинг MPA - {}", id);
        return ratingMPA.orElse(null);
    }
}
