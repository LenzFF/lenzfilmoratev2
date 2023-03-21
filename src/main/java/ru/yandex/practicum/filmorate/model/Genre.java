package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Genre extends Model implements Comparable<Genre>{
    private String name;

    public Genre(long id) {
        this.setId(id);
    }

    @Override
    public int compareTo(Genre o) {
        return (int) (this.getId() - o.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return Objects.equals(this.getId(), genre.getId());
    }
}
