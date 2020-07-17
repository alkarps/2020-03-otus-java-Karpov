package my.alkarps.engine;

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
        long s = duration.toNanos();
        StringBuilder sb = new StringBuilder();
        long hours = s / 3600_000_000_000L;
        boolean print = false;
        if (print = hours > 0) {
            sb.append(hours).append(" ч.");
        }
        long minutes = (s % 3600_000_000_000L) / 60_000_000_000L;
        if (print = print || minutes > 0) {
            sb.append(minutes).append(" м.");
        }
        long seconds = (s % 60_000_000_000L) / 1000_000_000L;
        if (print = print || seconds > 0) {
            sb.append(seconds).append(" с.");
        }
        long millis = (s % 1000_000_000L) / 1000_000L;
        if (print = print || millis > 0) {
            sb.append(millis).append(" мс.");
        }
        long micros = (s % 1000_000L) / 1000L;
        if (print || micros > 0) {
            sb.append(micros).append(" мкс.");
        }
        long nano = s % 1000L;
        sb.append(nano).append(" н.");
        return sb.toString();
    }
}
