package ua.yandex.shad.tries;

public class Tuple implements Comparable {
    public final String term;
    public final int weight;

    public Tuple(String term, int weight) {
        this.term = term;
        this.weight = weight;
    }

    @Override
    public int compareTo(Object o) {
        int wo = ((Tuple)o).weight;
        if( weight < wo )
            return -1;
        else if( weight > wo )
            return 1;
        else
            return 0;
    }
}
