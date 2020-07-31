package my.alkarps.core.model;

import lombok.Getter;
import lombok.Setter;
import my.alkarps.jdbc.mapper.annotation.Id;

/**
 * @author sergey
 * created on 03.02.19.
 */
@Getter
public class User {
    @Id
    private final long id;
    private final String name;
    @Setter
    private int age;

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
