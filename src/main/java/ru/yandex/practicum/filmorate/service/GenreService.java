package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.dao.impl.GenreDbStorageImpl;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    private GenreDbStorageImpl genreDbStorageImpl;

    @Autowired
    public GenreService(GenreDbStorageImpl genreDbStorageImpl) {
        this.genreDbStorageImpl = genreDbStorageImpl;
    }

    public Optional<List<Genre>> getAll() {
        return genreDbStorageImpl.getAll();
    }

    public Optional<Genre> getById(long id) {
        Optional<Genre> optionalGenre =  genreDbStorageImpl.getById(id);
        if (optionalGenre.isEmpty()) {
            throw new NotFoundException("Жанр не найден");
        }
        return optionalGenre;
    }
}
