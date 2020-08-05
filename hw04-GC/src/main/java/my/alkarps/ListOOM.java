package my.alkarps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @author alkarps
 * create date 20.07.2020 12:27
 * <p>
 * Параметры запуска:
 * -Xms200m -Xmx200m -verbose:gc -Xlog:gc*:file=./logs/gc_pid_%p.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/dump
 * </p>
 */
public class ListOOM {
    public static void main(String[] args) {
        new ListOOM().generateOOM();
    }

    private final int addCount, removeCount;

    public ListOOM() {
        this.addCount = 10;
        this.removeCount = 2;
    }

    public ListOOM(int addCount, int removeCount) {
        this.addCount = addCount;
        this.removeCount = removeCount;
    }

    public void generateOOM() {
        List<Object> objects = new ArrayList<>();
        try {
            boolean runnable = true;
            while (runnable) {
                try {
                    addElements(objects);
                    removeElements(objects);
                    System.out.println(String.format("Current size %d.", objects.size()));
                } catch (RuntimeException ex) {
                    runnable = false;
                }
            }
        } catch (OutOfMemoryError error) {
            System.out.println(String.format("We crash with size %d.", objects.size()));
            throw error;
        }
    }

    private void addElements(List<Object> objects) {
        System.out.println(String.format("Size %d. Count for add %d", objects.size(), addCount));
        for (int i = 0; i < addCount; i++) {
            objects.add(getNextRandomString());
        }
    }

    private String getNextRandomString() {
        return UUID.randomUUID().toString();
    }

    private void removeElements(List<Object> objects) {
        int count = removeCount;
        System.out.println(String.format("Size %d. Count for remove %d", objects.size(), count));
        Iterator<Object> itr = objects.iterator();
        while (count > 0 && itr.hasNext()) {
            itr.next();
            itr.remove();
            count--;
        }
    }
}
