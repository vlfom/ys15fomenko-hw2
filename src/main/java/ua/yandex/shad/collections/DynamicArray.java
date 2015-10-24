package ua.yandex.shad.collections;

import ua.yandex.shad.tries.Tuple;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DynamicArray<T extends Comparable> implements Iterable<T> {
    private static final int DEFAULT_CAPACITY = 10;

    private int capacity = DEFAULT_CAPACITY;
    private int size;
    private Object[] elements;

    public DynamicArray() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    public DynamicArray(Iterable<T> tuples) {
        elements = new Object[DEFAULT_CAPACITY];
        for (T tuple : tuples) {
            add(tuple);
        }
    }

    private void ensureCapacity(int capacity) {
        if (this.capacity < capacity) {
            while (this.capacity < capacity) {
                this.capacity *= 2;
            }
            elements = Arrays.copyOf(elements, this.capacity);
        }
    }

    public int size() {
        return size;
    }

    public T get(int index) {
        return (T) elements[index];
    }

    public void set(int index, T value) {
        elements[index] = value;
    }

    public boolean add(T o) {
        ensureCapacity(size + 1);
        elements[size] = o;
        size += 1;
        return true;
    }

    public boolean merge(DynamicArray<T> o) {
        ensureCapacity(size + o.size);
        for (int i = size; i < size + o.size; ++i) {
            elements[i] = o.elements[i - size];
        }
        size += o.size;
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

    public void sort() {
        quickSort(0, size);
    }

    private int partition(int l, int r) {
        T temp;
        int i;
        i = l + (r-l)/2;
        temp = (T) elements[i];
        elements[i] = elements[r-1];
        elements[r-1] = temp;
        i = l;
        for (int j = l; j < r - 1; ++j) {
            if (((T) elements[j]).compareTo(elements[r - 1]) <= 0) {
                temp = (T) elements[i];
                elements[i] = elements[j];
                elements[j] = temp;
                i += 1;
            }
        }
        temp = (T) elements[i];
        elements[i] = elements[r - 1];
        elements[r - 1] = temp;
        return i;
    }

    private void quickSort(int l, int r) {
        if (l < r) {
            int q = partition(l, r);
            quickSort(l, q);
            quickSort(q + 1, r);
        }
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < size; ++i) {
            s += elements[i] + " ";
        }
        return s;
    }

    private class Itr implements Iterator<T> {
        int cursor;
        int lastRet;

        private Itr() {
            lastRet = -1;
        }

        public boolean hasNext() {
            return cursor != size;
        }

        public T next() {
            int i = this.cursor;
            if (i >= size) {
                throw new NoSuchElementException();
            } else {
                cursor = i + 1;
                return (T)elements[this.lastRet = i];
            }
        }

        public void remove() {
            int i = cursor;
            if (i >= size) {
                throw new NoSuchElementException();
            } else {
                for (int j = i; j < size - 1; ++j) {
                    elements[j] = elements[j + 1];
                }
                --size;
            }
        }
    }
}
