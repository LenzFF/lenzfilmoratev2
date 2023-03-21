package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.RatingMpaDbStorageImpl;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.List;
import java.util.Optional;

@Service
public class RatingMpaService {
    private RatingMpaDbStorageImpl ratingMpaDbStorage;

    @Autowired
    public RatingMpaService(RatingMpaDbStorageImpl ratingMpaDbStorage) {
        this.ratingMpaDbStorage = ratingMpaDbStorage;
    }

    public Optional<List<RatingMPA>> getAll() {
        return ratingMpaDbStorage.getAll();
    }

    public Optional<RatingMPA> getById(long id) {
        Optional<RatingMPA> optionalRatingMPA = ratingMpaDbStorage.getById(id);
        if (optionalRatingMPA.isEmpty()) {
            throw new NotFoundException("Рейтинг не найден");
        }
        return optionalRatingMPA;
    }

}
