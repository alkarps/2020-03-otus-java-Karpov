package my.alkarps;

import java.util.*;

/**
 * @author alkarps
 * create date 16.07.2020 12:47
 */
public class DiyArrayList<T> implements List<T> {

    private int size = 0;
    private Object[] elements;

    public <T> DiyArrayList() {
        elements = new Object[0];
    }

    public <T> DiyArrayList(int initSize) {
        if (initSize < 1) {
            throw new IllegalArgumentException("Неверное значение входного параметра");
        }
        elements = new Object[initSize];
    }

    public <T> DiyArrayList(Collection<T> collection) {
        if (collection == null) {
            throw new IllegalArgumentException("Неверное значение входного параметра");
        }
        elements = collection.toArray();
        size = collection.size();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Операция не поддержана");
    }

    @Override
    public Iterator<T> iterator() {
        return new DiyIterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a == null) {
            throw new IllegalArgumentException("Неверное значение входного параметра");
        }
        if (a.length < size) {
            return (T1[]) Arrays.copyOf(elements, size, a.getClass());
        } else {
            System.arraycopy(elements, 0, a, 0, size);
            Arrays.fill(a, size, a.length, null);
        }
        return a;
    }

    @Override
    public boolean add(T t) {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, elements.length + 5);
        }
        elements[size] = t;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Операция не поддержана");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Операция не поддержана");
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c == null) {
            throw new IllegalArgumentException("Неверное значение входного параметра");
        }
        if (c.isEmpty()) {
            return false;
        }
        for (T element : c) {
            add(element);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException("Операция не поддержана");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Операция не поддержана");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Операция не поддержана");
    }

    @Override
    public void clear() {
        size = 0;
        elements = new Object[0];
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("Неверное значение входного параметра");
        }
        return (T) elements[index];
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException("Операция не поддержана");
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("Неверное значение входного параметра index");
        }
        if (index == size) {
            add(element);
        } else {
            if (size == elements.length) {
                elements = Arrays.copyOf(elements, elements.length + 5);
            }
            System.arraycopy(elements, index + 1, elements, index, elements.length - index);
            elements[index] = element;
            size++;
        }
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("Неверное значение входного параметра");
        }
        T removedElement = (T) elements[index];
        if (size - 1 > index) {
            System.arraycopy(elements, index + 1, elements, index, elements.length - index);
        }
        size -= -1;
        elements[size] = null;
        return removedElement;
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException("Операция не поддержана");
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Операция не поддержана");
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DiyListIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new DiyListIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Операция не поддержана");
    }

    private class DiyIterator implements Iterator<T> {

        int current = 0;
        int lastRet = -1;

        @Override
        public boolean hasNext() {
            return size != current;
        }

        @Override
        public T next() {
            if (current >= size) {
                throw new NoSuchElementException();
            }
            lastRet = current++;
            return (T) elements[lastRet];
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            DiyArrayList.this.remove(lastRet);
            current = lastRet;
            lastRet = -1;
        }
    }

    private class DiyListIterator extends DiyIterator implements ListIterator<T> {

        public DiyListIterator(int index) {
            super();
            current = index;
        }

        @Override
        public boolean hasPrevious() {
            return current != 0;
        }

        @Override
        public T previous() {
            if (current - 1 < 0) {
                throw new NoSuchElementException();
            }
            current -= 1;
            return (T) elements[lastRet = current];
        }

        @Override
        public int nextIndex() {
            return current;
        }

        @Override
        public int previousIndex() {
            return current - 1;
        }

        @Override
        public void set(T t) {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            elements[lastRet] = t;
        }

        @Override
        public void add(T t) {
            DiyArrayList.this.add(current, t);
            current++;
            lastRet = -1;
        }
    }
}
