package my.alkarps.core.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import my.alkarps.jdbc.mapper.annotation.Id;

/**
 * @author sergey
 * created on 03.02.19.
 */
@Getter
@Setter
@ToString
public class User {
    @Id
    private long id;
    private String name;
    private int age;
}
