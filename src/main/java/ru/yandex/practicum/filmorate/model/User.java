package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User extends Model{
    private String name;
    @Past
    private LocalDate birthday;
    @NotBlank
    private String login;
    @NonNull
    @Email
    private String email;

    public User(int id, String login, String name, String email, LocalDate birthday) {
        this.setId(id);
        this.setLogin(login);
        this.setName(name);
        this.setBirthday(birthday);
        this.setEmail(email);
    }
}
