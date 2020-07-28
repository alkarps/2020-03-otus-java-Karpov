package my.alkarps.json;

import com.google.gson.Gson;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author alkarps
 * create date 28.07.2020 15:45
 */
class DiyJsonSerializerTest {

    private String gsonJson(Object object) {
        return new Gson().toJson(object);
    }

    @ParameterizedTest
    @MethodSource("testData")
    void justObject(Object object) {
        String expected = gsonJson(object);
        assertThat(DiyJsonSerializer.toJson(object))
                .isEqualTo(expected);
    }

    private static Stream<Object> testData() {
        return Stream.of(
                null,
                new Object()
        );
    }

    @ParameterizedTest
    @ValueSource(bytes = {1, -1, 0})
    void primitiveByte(byte object) {
        String expected = gsonJson(object);
        assertThat(DiyJsonSerializer.toJson(object))
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(shorts = {130, -130, 0})
    void primitiveShort(short object) {
        String expected = gsonJson(object);
        assertThat(DiyJsonSerializer.toJson(object))
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {130, -130, 0})
    void primitiveInt(int object) {
        String expected = gsonJson(object);
        assertThat(DiyJsonSerializer.toJson(object))
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(longs = {130, -130, 0})
    void primitiveLong(long object) {
        String expected = gsonJson(object);
        assertThat(DiyJsonSerializer.toJson(object))
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void primitiveBoolean(boolean object) {
        String expected = gsonJson(object);
        assertThat(DiyJsonSerializer.toJson(object))
                .isEqualTo(expected);
    }
}