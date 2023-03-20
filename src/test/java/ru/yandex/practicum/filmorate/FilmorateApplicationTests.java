package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.impl.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final FilmDbStorageImpl filmStorage;
    private final GenreDbStorageImpl genreDbStorage;
    private final RatingMpaDbStorageImpl ratingMpaDbStorage;
    private final FilmLikesDbStorageImpl filmLikesDbStorage;
    private final UserDbStorageImpl userDbStorage;

    @Test
    public void findGenreById() {


        Optional<Genre> genreOptional = genreDbStorage.getById(1);

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void findAllGenres() {
        Optional<List<Genre>> genreOptional = genreDbStorage.getAll();

        Assertions.assertEquals(6, genreOptional.get().size());
    }

    @Test
    public void findRatingById() {

        Optional<RatingMPA> ratingMPAOptional = ratingMpaDbStorage.getById(1);

        assertThat(ratingMPAOptional)
                .isPresent()
                .hasValueSatisfying(ratingMPA ->
                        assertThat(ratingMPA).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void findAllRatings() {
        Optional<List<RatingMPA>> ratingMPAList = ratingMpaDbStorage.getAll();

        Assertions.assertEquals(5, ratingMPAList.get().size());
    }

    @Test
    public void bigFilmTest() {
        Film film = new Film();
        film.setName("MADAGASKAR");
        film.setDescription("-");
        film.setMpa(new RatingMPA(1));
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(2005, 10, 1));
        film.getGenres().add(new Genre(3));
        film.getGenres().add(new Genre(4));

        filmStorage.add(film);

        User user = new User();
        user.setLogin("Lenz");
        user.setName("Yakov");
        user.setBirthday(LocalDate.of(1988, 01, 11));
        user.setEmail("lenz@bk.ru");
        userDbStorage.add(user);

        user.setLogin("Dex");
        user.setName("Denis");
        user.setBirthday(LocalDate.of(1984, 07, 11));
        user.setEmail("dex@bk.ru");
        userDbStorage.add(user);

        user.setLogin("NeAngel");
        user.setName("July");
        user.setBirthday(LocalDate.of(1990, 04, 11));
        user.setEmail("jul@bk.ru");
        userDbStorage.add(user);

        filmStorage.addLike(1, 1);
        filmStorage.addLike(1, 2);

        userDbStorage.makeFriends(1, 3);
        userDbStorage.makeFriends(2, 3);

        //ставим лайк
        filmStorage.addLike(1, 3);
        long likesCount = filmLikesDbStorage.getFilmLikesCount(1L);

        Assertions.assertEquals(3, likesCount);

        //убераем лайк
        filmStorage.removeLike(1, 1);
        filmStorage.removeLike(1, 2);
        likesCount = filmLikesDbStorage.getFilmLikesCount(1L);

        Assertions.assertEquals(1, likesCount);

        //добавление
        film = new Film();
        film.setName("Rambo");
        film.setMpa(ratingMpaDbStorage.getById(4).get());
        film.setDuration(110);
        film.setReleaseDate(LocalDate.of(1980, 5, 20));
        film.setDescription("Trash");
        film.getGenres().add(genreDbStorage.getById(1).get());
        film.getGenres().add(genreDbStorage.getById(3).get());

        filmStorage.add(film);

        Optional<List<Film>> optionalFilms = filmStorage.getAll();

        Assertions.assertEquals(2, optionalFilms.get().size());

        Optional<Film> filmOptional = filmStorage.getById(2);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film1 ->
                        assertThat(film1).hasFieldOrPropertyWithValue("id", 2L)
                );

        Assertions.assertEquals(2, filmOptional.get().getGenres().size());

        //проверка что лайков 0
        long likes = filmLikesDbStorage.getFilmLikesCount(2L);
        Assertions.assertEquals(0, likes);

        //запрос рейтинга фильмов по лайкам
        optionalFilms = filmStorage.getTopLikedFilms(10);
        Assertions.assertEquals(2, optionalFilms.get().size());

        Assertions.assertEquals(2, optionalFilms.get().get(1).getId());

        //обновление
        film.setName("Scream");
        film.setMpa(ratingMpaDbStorage.getById(5).get());
        film.setDuration(90);
        film.setDescription("---");
        film.setReleaseDate(LocalDate.of(1996, 12, 18));
        film.getGenres().add(genreDbStorage.getById(5).get());

        filmStorage.update(film);

        filmOptional = filmStorage.getById(2);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film1 ->
                        assertThat(film1).hasFieldOrPropertyWithValue("name", "Scream")
                );

        Optional<List<Genre>> optionalGenres = genreDbStorage.getFilmsGenres(2L);
        Assertions.assertEquals(3, optionalGenres.get().size());


        //удаление
        filmStorage.deleteById(2L);
        optionalFilms = filmStorage.getAll();

        Assertions.assertEquals(1, optionalFilms.get().size());

        optionalGenres = genreDbStorage.getFilmsGenres(2L);
        Assertions.assertEquals(0, optionalGenres.get().size());

        //удаляем все
        filmStorage.deleteAll();
        optionalFilms = filmStorage.getAll();

        Assertions.assertEquals(0, optionalFilms.get().size());


    }

    @Test
    public void bigUserTest() {
        //добавляем пользователя
        User user = new User();
        user.setLogin("Hacks");
        user.setEmail("123@321.com");
        user.setBirthday(LocalDate.of(1999, 12, 31));

        userDbStorage.add(user);

        Optional<List<User>> optionalUsers = userDbStorage.getAll();
        Assertions.assertEquals(4, optionalUsers.get().size());

        //изменяем
        user.setId(4);
        user.setBirthday(LocalDate.of(2000, 01, 01));

        userDbStorage.update(user);

        Optional<User> optionalUser = userDbStorage.getById(4);
        Assertions.assertEquals(LocalDate.of(2000, 01, 01), optionalUser.get().getBirthday());


        //дружим
        userDbStorage.makeFriends(4, 3);

        optionalUsers = userDbStorage.getCommonFriends(1, 4);
        Assertions.assertEquals(1, optionalUsers.get().size());

        Assertions.assertEquals(3, optionalUsers.get().get(0).getId());

        //удаляем
        userDbStorage.deleteById(4);

        optionalUsers = userDbStorage.getAll();
        Assertions.assertEquals(3, optionalUsers.get().size());

        userDbStorage.deleteAll();
        filmStorage.deleteAll();
    }

}