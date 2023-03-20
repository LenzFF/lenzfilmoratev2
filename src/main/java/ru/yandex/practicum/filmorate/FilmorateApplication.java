package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.dao.impl.GenreDbStorageImpl;

@SpringBootApplication
public class FilmorateApplication {
	private static GenreDbStorageImpl genreDbStorageImpl;
	public static void main(String[] args) {
		SpringApplication.run(FilmorateApplication.class, args);
	}
}
