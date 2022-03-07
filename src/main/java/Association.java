import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Association {
    private final Random rand = new Random();

    public String word;
    public ArrayList<String> phrases = new ArrayList<>();
    public ArrayList<Double> weights = new ArrayList<>();

    public Association(String word) {
        this.word = word;
    }

    public void addPhrase(String phrase, double weight) {
        this.phrases.add(phrase);
        this.weights.add(weight);
    }

    private int weightedRandom()
    {
        double selection = rand.nextDouble();
        double total = 0;
        int i = 0;
        for (i = 0; (i < weights.size()) && (total <= selection); i++) {
            total += weights.get(i);
        }
        return i - 1;
    }

    public String getPhrase() {
        int index = weightedRandom();
        return phrases.get(index);
    }
}
