package my.alkarps.engine.model;

import lombok.Builder;
import lombok.Singular;

import java.time.Duration;
import java.util.List;

/**
 * @author alkarps
 * create date 13.07.2020 11:57
 */
@Builder
public class Statistics {
    private final String classTestName;
    private final Duration classTestTime;
    @Singular
    private final List<TestMethodResult> testMethodResults;

    @Builder
    public static class TestMethodResult {
        private final String methodName;
        private final Duration methodTestTime;
        private final boolean success;
        private final Throwable throwable;

        @Override
        public String toString() {
            return "Метод " + methodName +
                    " выполнялся " + Statistics.toString(methodTestTime) +
                    " и завершился" + (success ? " успешно " :
                    (" с ошибкой: " + throwable));
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append("Тестирование класса ")
                .append(classTestName)
                .append(" длилось ")
                .append(toString(classTestTime))
                .append(":");
        testMethodResults.forEach(testMethodResult -> sb.append("\n").append(testMethodResults));
        return sb.toString();
    }

    private static String toString(Duration duration) {
        StringBuilder sb = new StringBuilder();
        if (duration.toHoursPart() > 0) {
            sb.append(duration.toHoursPart()).append(" ч.");
        }
        if (duration.toMinutesPart() > 0) {
            sb.append(duration.toMinutesPart()).append(" м.");
        }
        if (duration.toSecondsPart() > 0) {
            sb.append(duration.toSecondsPart()).append(" с.");
        }
        int ms = Math.max(duration.toMillisPart(), 1);
        return sb.append(ms).append(" мс.").toString();
    }
}
