package ru.yandex.practicum.filmorate.dao;

public interface FilmLikesDbStorage {
    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    void deleteFilmLikes(long filmId);

    void deleteUserLikes(long userId);

    void deleteAll();

    long getFilmLikesCount(long filmId);
}
