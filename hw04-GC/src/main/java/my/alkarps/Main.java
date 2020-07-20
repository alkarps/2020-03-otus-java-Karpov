package my.alkarps;

/**
 * @author alkarps
 * create date 20.07.2020 10:07
 * <p>
 * -Xms200m
 * -Xmx200m
 * -verbose:gc
 * -Xlog:gc*:file=./logs/gc_pid_%p.log
 * -XX:+HeapDumpOnOutOfMemoryError
 * -XX:HeapDumpPath=./logs/dump
 * </p>
 */
public class Main {
    public static void main(String[] args) {
        ListOOM listOOM = new ListOOM();
        listOOM.generateOOM();
    }
}
