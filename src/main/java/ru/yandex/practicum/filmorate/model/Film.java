package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Film extends Model{
    @NotBlank
    private String name;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    @Size(min = 1, max = 200)
    private String description;
    private RatingMPA mpa;
    private TreeSet<Genre> genres = new TreeSet<>();

    public Film(String name, LocalDate releaseDate, int duration, String description, RatingMPA mpa) {
        this.setName(name);
        this.setReleaseDate(releaseDate);
        this.setDuration(duration);
        this.setMpa(mpa);
        this.setDescription(description);

    }
}
