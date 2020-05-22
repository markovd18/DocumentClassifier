package classifier;

import utils.ClassificationClass;
import utils.Document;

import java.util.*;

/**
 * Finds k nearest neighbours of classified document and returns classification class based on theirs.
 *
 * @author <a href=mailto:markovd@students.zcu.cz>David Markov</a>
 */
public class KNN implements IClassifier {

    /**
     * Number of nearest neighbours to find.
     */
    private static final int K = 3;

    /**
     * List of documents in model.
     */
    private final List<Document> documents;

    public KNN(List<Document> documents) {
        this.documents = documents;
    }

    @Override
    public List<ClassificationClass> classifyDocument(Document document) {
        if (document == null || document.getContent().isEmpty()) {
            return null;
        }
        Map<Document, Double> distances = new HashMap<>();
        double distance = 0;
        for (Document trainingDocument : documents) {
            for (Map.Entry<String, Double> wordValue : document.getFeatures().entrySet()) {
                double value = 0;
                try {
                    value = trainingDocument.getFeatures().get(wordValue.getKey());
                } catch (NullPointerException e) {
                    // if not found, we let value at 0
                }
                distance = (wordValue.getValue() - value) *
                        (wordValue.getValue() - value);
            }
            distance = Math.sqrt(distance);
            distances.put(trainingDocument, distance);
        }

        Map<ClassificationClass, Integer> classificationClasses = new HashMap<>();
        for (int i = 0; i < K; i++) {
            Document nearestNeighbour = Collections.max(distances.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();
            distances.remove(nearestNeighbour);
            for (ClassificationClass classificationClass : nearestNeighbour.getClassificationClasses()) {
                if (classificationClasses.containsKey(classificationClass)) {
                    classificationClasses.put(classificationClass, classificationClasses.get(classificationClass) + 1);
                } else {
                    classificationClasses.put(classificationClass, 1);
                }
            }
        }
        List<ClassificationClass> result = new ArrayList<>();
        result.add(Collections.max(classificationClasses.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey());
        return result;
    }
}
