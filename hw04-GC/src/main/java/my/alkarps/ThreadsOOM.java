package my.alkarps;

import java.time.Duration;
import java.time.Instant;

import static java.lang.Thread.sleep;

/**
 * @author alkarps
 * create date 20.07.2020 16:36
 * <p>
 * Параметры запуска:
 * -Xms200m -Xmx200m -verbose:gc -Xlog:gc*:file=./logs/gc_pid_%p.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/dump
 * </p>
 */
public class ThreadsOOM {
    public static void main(String[] args) {
        new ThreadsOOM().generateOOM();
    }

    private final int stopAfterNSecond = 4 * 60;

    public void generateOOM() {
        boolean runnable = true;
        Instant startTime = Instant.now();
        while (runnable) {
            try {
                Thread thread = new Thread(new Factorial());
                thread.start();
                sleep(3000L);
                runnable = timeToStop(startTime);
            } catch (InterruptedException ex) {
                runnable = false;
            }
        }
        new ListOOM(1000, 10).generateOOM();
    }

    private boolean timeToStop(Instant startTime) {
        Duration duration = Duration.between(startTime, Instant.now());
        return duration.getSeconds() < stopAfterNSecond;
    }

    private static class Factorial implements Runnable {
        @Override
        public void run() {
            new ListOOM(500, 10).generateOOM();
        }
    }

}
