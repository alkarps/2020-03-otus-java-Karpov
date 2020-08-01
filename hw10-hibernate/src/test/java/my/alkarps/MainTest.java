package my.alkarps;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * @author alkarps
 * create date 01.08.2020 17:26
 */
class MainTest {

    @Test
    void main() {
        assertThatCode(() -> Main.main(null)).doesNotThrowAnyException();
    }
}