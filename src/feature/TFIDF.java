package feature;

import utils.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Feature algorithm creating document features (representative words) based on term frequency
 * multiplied by inverse document frequency.
 *
 * @author <a href=mailto:markovd@students.zcu.cz>David Markov</a>
 */
public class TFIDF extends TermFrequency {
    private final List<Document> documents;

    public TFIDF(List<Document> documents) {
        this.documents = documents;
    }

    @Override
    public void createFeatures(Document document, int featureCount) {
        if (document == null || document.getContent().isEmpty() || featureCount == 0) {
            return;
        }

        Map<String, Double> termFrequencies = computeTermFrequencies(document);
        Map<String, Double> tfidfMap = new HashMap<>();

        for (Map.Entry<String, Double> pair : termFrequencies.entrySet()) {
            //TODO TFIDF
        }
    }
}
