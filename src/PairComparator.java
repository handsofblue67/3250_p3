import java.util.Comparator;
/**
 * Created by michael on 10/22/15.
 */
public class PairComparator implements Comparator<Pair> {
    @Override
    public int compare(Pair o1, Pair o2) {
        if (o1.getInstances() > o2.getInstances()) {
            return 1;
        }
        else {
            return -1;
        }
    }
}
