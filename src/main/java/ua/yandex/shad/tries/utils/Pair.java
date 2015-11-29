package ua.yandex.shad.tries.utils;

/**
 * @author Vladimir Fomenko
 */
public class Pair<T, E> {
    public T first;
    public E second;

    public Pair() {
    }

    public Pair(T first, E second) {
        this.first = first;
        this.second = second;
    }
}
