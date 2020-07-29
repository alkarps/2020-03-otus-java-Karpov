package my.alkarps;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author alkarps
 * create date 16.07.2020 12:57
 */
public class CollectionsTest {

    @Test
    public void addAllTest_whenDiyArrayListIsEmpty() {
        int sourceSize = 100;
        List<Integer> source = getIntegerValue(sourceSize);
        List<Integer> destination = new DiyArrayList<>();
        Collections.addAll(destination, source.toArray(new Integer[0]));
        assertNotNull(destination);
        assertEquals(sourceSize, destination.size());
        for (int i = 0; i < source.size(); i++) {
            assertEquals(source.get(i), destination.get(i));
        }
    }

    @Test
    public void addAllTest_whenDiyArrayListIsFill() {
        int destOriginalSize = 100;
        int sourceSize = 100;
        List<Integer> source = getIntegerValue(sourceSize);
        List<Integer> destination = new DiyArrayList<>();
        destination.addAll(IntStream.rangeClosed(1, destOriginalSize)
                .boxed()
                .map(integer -> 0)
                .collect(Collectors.toList()));
        Collections.addAll(destination, source.toArray(new Integer[0]));
        assertNotNull(destination);
        assertEquals(destOriginalSize + sourceSize, destination.size());
        for (int i = 0; i < destOriginalSize; i++) {
            assertEquals(0, destination.get(i));
        }
        for (int i = 0; i < source.size(); i++) {
            assertEquals(source.get(i), destination.get(i + destOriginalSize));
        }
    }

    @Test
    public void copy() {
        int sourceSize = 100;
        List<Integer> source = getIntegerValue(sourceSize);
        List<Integer> destination = new DiyArrayList<>();
        destination.addAll(IntStream.rangeClosed(1, sourceSize)
                .boxed()
                .map(integer -> 0)
                .collect(Collectors.toList()));
        Collections.copy(destination, source);
        assertNotNull(destination);
        assertEquals(sourceSize, destination.size());
        for (int i = 0; i < source.size(); i++) {
            assertEquals(source.get(i), destination.get(i));
        }
    }

    @Test
    public void revers() {
        int size = 100;
        List<Integer> destination = new DiyArrayList<>(getIntegerValue(size));
        assertEquals(size, destination.size());
        Collections.reverse(destination);
        assertEquals(size, destination.size());
        for (int i = size; i > 0; i--) {
            assertEquals(i, destination.get(size - i));
        }
    }

    @Test
    public void sort() {
        int size = 100;
        List<Integer> source = getIntegerValue(size);
        Collections.reverse(source);
        List<Integer> destination = new DiyArrayList<>(source);
        Collections.sort(destination);
        assertNotNull(destination);
        assertEquals(size, destination.size());
        for (int i = 0; i < source.size(); i++) {
            assertEquals(i + 1, destination.get(i));
        }
    }

    private List<Integer> getIntegerValue(int sourceSize) {
        if (sourceSize < 1) {
            sourceSize = 1;
        }
        return IntStream.rangeClosed(1, sourceSize)
                .boxed()
                .collect(Collectors.toList());
    }
}
