package my.alkarps.engine.model;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * @author alkarps
 * create date 13.07.2020 11:57
 */
@Value
@RequiredArgsConstructor
@AllArgsConstructor
public class Statistics {
    String classTestName;
    long total;
    @NonFinal
    long success;

    public void addSuccess() {
        success++;
    }

    public String toString() {
        return String.format("%s результат: всего тестов: %d; успешно выполненных: %d; упавших тестов: %d",
                classTestName, total, success, total - success);
    }
}
