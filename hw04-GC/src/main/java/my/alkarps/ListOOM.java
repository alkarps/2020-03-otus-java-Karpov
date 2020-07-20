package my.alkarps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @author alkarps
 * create date 20.07.2020 12:27
 * <p>
 * G1 (Java heap space): ~2451112 - 261.948s; ~2451864 - 258.692s; ~2451800 - 312.890s
 * Serial (Java heap space): 2384338 - 281.276s; 2384330 - 273.419s; 2384370 - 325.922s
 * Parallel (GC overhead limit exceeded): 2094891 - 242.314s ; 2113442 - 206.649s; 2191163 - 226.154s
 * </p>
 */
public class ListOOM {

    public void generateOOM() {
        List<Object> objects = new ArrayList<>();
        try {
            while (true) {
                addElements(objects);
                removeElements(objects);
                System.out.println(String.format("Current size %d.", objects.size()));
            }
        } catch (OutOfMemoryError error) {
            System.out.println(String.format("We crash with size %d.", objects.size()));
            throw error;
        }
    }

    private void addElements(List<Object> objects) {
        int count = 10;
        System.out.println(String.format("Size %d. Count for add %d", objects.size(), count));
        for (int i = 0; i < count; i++) {
            objects.add(getNextRandomString());
        }
    }

    private String getNextRandomString() {
        return UUID.randomUUID().toString();
    }

    private void removeElements(List<Object> objects) {
        int count = 2;
        System.out.println(String.format("Size %d. Count for remove %d", objects.size(), count));
        Iterator<Object> itr = objects.iterator();
        while (count > 0 && itr.hasNext()) {
            itr.next();
            itr.remove();
            count--;
        }
    }
}
