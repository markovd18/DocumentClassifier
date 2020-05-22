package feature;

import utils.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple binary feature algorithm, assigning value to 1 to every word appearing in given document.
 *
 * @author <a href=mailto:markovd@students.zcu.cz>David Markov</a>
 */
public class TermBinary implements IFeatureAlgorithm {

    @Override
    public void createFeatures(Document document) {
        Map<String, Double> features = new HashMap<>();

        for (String term : document.getContent().split(" ")) {
            if (!term.isEmpty() && !features.containsKey(term)) {
                features.put(term, 1.0);
            }
        }

        document.setFeatures(features);
    }
}
