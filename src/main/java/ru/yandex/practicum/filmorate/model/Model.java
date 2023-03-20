package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public abstract class Model {
    @NonNull
    private long id;
}
