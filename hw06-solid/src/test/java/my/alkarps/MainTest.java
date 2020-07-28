package my.alkarps;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * @author alkarps
 * create date 28.07.2020 15:05
 */
class MainTest {
    @Test
    void main() {
        assertThatCode(() -> Main.main(null)).doesNotThrowAnyException();
    }
}