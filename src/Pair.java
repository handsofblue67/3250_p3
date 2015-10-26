/**
 * Created by michael on 10/22/15.
 * pair class, holds the word string and the number of instances the word was found.
 * created because tables cannot sort by key, this class allows to sort words
 * in descending order according to the number of occurrences of each word.
 */
public class Pair {
    Integer instances;
    String word;
    Pair (Integer _instances, String _word) {
        instances = _instances;
        word = _word;
    }

    public Integer getInstances() {
        return instances;
    }

    public String getWord() {
        return word;
    }
}
