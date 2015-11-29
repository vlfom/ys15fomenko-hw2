package ua.yandex.shad.tries.utils;

public class Tuple implements Comparable {
    private final String term;
    private final int weight;

    public Tuple(String term, int weight) {
        this.term = term;
        this.weight = weight;
    }

    public String getTerm() {
        return term;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object x) {
        if (!(x instanceof Tuple)) {
            return false;
        }
        Tuple that = (Tuple) x;
        if ((this.term == null) != (that.term == null)) {
            return false;
        } else if (this.term == null) {
            return this.weight == that.weight;
        } else {
            return that.term.equals(this.term) && that.weight == this.weight;
        }
    }

    @Override
    public int compareTo(Object x) {
        if (!(x instanceof Tuple)) {
            return -1;
        }
        Tuple that = (Tuple) x;
        if (weight < that.weight) {
            return -1;
        } else if (weight > that.weight) {
            return 1;
        } else {
            return this.term.compareTo(that.term);
        }
    }

    @Override
    public int hashCode() {
        return 42;
    }
}
