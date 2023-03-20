package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.List;
import java.util.Optional;

public interface RatingMpaDbStorage {
    Optional<List<RatingMPA>> getAll();
    Optional<RatingMPA> getById(long id);
}
