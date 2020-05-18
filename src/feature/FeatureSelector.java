package feature;

import java.util.*;

/**
 * Base class for selecting features from map of terms and their values.
 * In practice, all feature selection algorithms are derived from this class.
 *
 * @author <a href=mailto:markovd@students.zcu.cz>David Markov</a>
 */
public class FeatureSelector {

    /**
     * Selects given number of features from given term-to-value map
     *
     * @param termValueMap map of terms and their values
     * @param featureCount number of features to select
     * @return list of features
     */
    public List<String> selectFeatures(Map<String, Double> termValueMap, int featureCount) {
        List<String> features = new ArrayList<>();
        for (int i = 0; i < featureCount; i++) {
            if (termValueMap.isEmpty()) {
                break;
            }
            String term = Collections.max(termValueMap.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();
            termValueMap.remove(term);
            features.add(term);
        }
        return features;
    }
}
