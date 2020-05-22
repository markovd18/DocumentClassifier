package feature;

import utils.Document;

import java.util.List;
import java.util.Map;

/**
 * Feature algorithm creating document features (representative words) based on term frequency
 * multiplied by inverse document frequency.
 *
 * @author <a href=mailto:markovd@students.zcu.cz>David Markov</a>
 */
public class TFIDF extends TermFrequency {

    /**
     * List of documents to search for term occurrencies.
     */
    private final List<Document> documents;

    public TFIDF(List<Document> documents) {
        this.documents = documents;
    }

    @Override
    public void createFeatures(Document document) {
        if (document == null || document.getContent().isEmpty()) {
            return;
        }

        Map<String, Double> tfidfMap = computeTermFrequencies(document);

        for (Map.Entry<String, Double> pair : tfidfMap.entrySet()) {
            double idf = Math.log(documents.size() / (getTermOccurence(pair.getKey()) + 1.0));    // +1 adjustment so we never divide by 0
            pair.setValue(pair.getValue() * idf);
        }

        document.setFeatures(tfidfMap);
    }

    /**
     * Returns number of documents in corpus containing given term.
     *
     * @param term wanted term
     * @return number of documents in corpus containing given term
     */
    public int getTermOccurence(String term) {
        int occurrence = 0;

        for (Document document : documents) {
            if (document.getContent().contains(term)) {
                occurrence++;
            }
        }

        return occurrence;
    }
}
