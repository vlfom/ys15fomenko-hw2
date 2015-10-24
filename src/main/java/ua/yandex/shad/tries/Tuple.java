package ua.yandex.shad.tries;

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
        Tuple o = (Tuple) x;
        return o.term.equals(this.term) && o.weight == this.weight;
    }

    @Override
    public int compareTo(Object x) {
        Tuple o = (Tuple) x;
        if (weight < o.weight) {
            return -1;
        } else if (weight > o.weight) {
            return 1;
        } else {
            return this.term.compareTo(o.term);
        }
    }

    @Override
    public int hashCode() {
        return 42;
    }
}
