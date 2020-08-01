package my.alkarps.core.model;

import lombok.*;
import my.alkarps.jdbc.mapper.annotation.Id;

/**
 * @author sergey
 * created on 03.02.19.
 */
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    @Id
    private final long id;
    private final String name;
    @Setter
    private int age;
}
