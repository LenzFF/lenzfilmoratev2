package ru.yandex.practicum.filmorate.model;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RatingMPA extends Model{
    private String name;

    public RatingMPA(long id) {
        this.setId(id);
    }
}
