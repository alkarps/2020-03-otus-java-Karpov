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
    }
}
