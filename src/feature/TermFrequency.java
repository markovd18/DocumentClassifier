package feature;

import utils.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Feature algorithm creating document features (representative words) based on their frequency divided
 * by the total number of words in document.
 *
 * @author <a href=mailto:markovd@students.zcu.cz>David Markov</a>
 */
public class TermFrequency implements IFeatureAlgorithm {


    @Override
    public void createFeatures(Document document) {
        if (document == null || document.getContent().isEmpty()) {
            return;
        }

        Map<String, Double> termFrequencies = computeTermFrequencies(document);
        document.setFeatures(termFrequencies);
    }

    /**
     * Computes term frequencies for each term occurring in document.
     *
     * @param document document to count term frequencies in
     * @return map where keys are terms of the document and values are their term frequencies
     */
    public Map<String, Double> computeTermFrequencies(Document document) {
        Map<String, Double> termFrequencies = new HashMap<>();
        String[] terms = document.getContent().split(" ");
        int totalNumOfWords = 0;

        for (String term : terms) {
            if (term.isEmpty()) {
                continue;
            }
            if (termFrequencies.containsKey(term)) {
                termFrequencies.replace(term, termFrequencies.get(term) + 1);
            } else {
                termFrequencies.put(term, 1.0);
            }
            totalNumOfWords++;
        }

        for (Map.Entry<String, Double> pair : termFrequencies.entrySet()) {
            pair.setValue(pair.getValue() / totalNumOfWords);
        }
        return termFrequencies;
    }


}