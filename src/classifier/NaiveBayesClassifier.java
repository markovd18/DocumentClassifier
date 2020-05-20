package classifier;

import utils.ClassificationClass;
import utils.Document;

import java.util.*;

/**
 * Classifier based on probability model. Counts the probability of document belonging to classification class.
 *
 * @author <a href=mailto:markovd@students.zcu.cz>David Markov</a>
 */
public class NaiveBayesClassifier implements IClassifier {

    /**
     * List of documents in model.
     */
    private final List<Document> documents;
    /**
     * List of available classification classes.
     */
    private final List<ClassificationClass> classificationClasses;
    /**
     * Stores number of unique words in each classification class.
     */
    private final Map<String, Integer> totalWordsInClass;
    /**
     * Number of possible (unique) words in training set.
     */
    private int possibleCorpusWords;

    public NaiveBayesClassifier(List<Document> documents, List<ClassificationClass> classificationClasses) {
        this.documents = documents;
        this.classificationClasses = classificationClasses;
        this.possibleCorpusWords = 0;
        totalWordsInClass = new HashMap<>();
    }

    @Override
    public List<ClassificationClass> classifyDocument(Document document) {
        if (document == null || document.getContent().isEmpty() || document.getFeatures().isEmpty()) {
            return null;
        }
        if (possibleCorpusWords == 0) {
            getWordCounts();
        }

        List<ClassificationClass> documentClasses = new ArrayList<>();
        List<String> features = document.getFeatures();
        Map<ClassificationClass, Double> classProbabilities = new HashMap<>();

        for (ClassificationClass classificationClass : classificationClasses) {
            double p_documentClass = getClassProbability(classificationClass);
            //TODO optimalizace class probability a term occurrence
            for (String feature : features) {
                int termOccurence = getTermOccurrence(feature, classificationClass);

                double p_wordClass = (termOccurence + 1.0) / (totalWordsInClass.get(classificationClass.getName()) + possibleCorpusWords);
                // + slova v klasifikovanem dokumentu ??
                p_documentClass *= p_wordClass;
            }
            classProbabilities.put(classificationClass, p_documentClass);
        }

        documentClasses.add(Collections.max(classProbabilities.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey());
        return documentClasses;
    }

    /**
     * Returns number of occurrences of given term in training data set of documents belonging to given classification class.
     *
     * @param term                term that we count occurrences of
     * @param classificationClass classification class we look into
     * @return number of occurrences of given term
     */
    private int getTermOccurrence(String term, ClassificationClass classificationClass) {
        int termOccurrence = 0;

        for (Document document : documents) {
            if (document.getClassificationClasses().contains(classificationClass)) {
                String[] words = document.getContent().split(" ");

                for (String word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }

                    if (word.equals(term)) {
                        termOccurrence++;
                    }
                }
            }
        }

        return termOccurrence;
    }

    /**
     * Counts number of unique words in corpus and each classification class.
     */
    private void getWordCounts() {
        Map<String, List<String>> uniqueWordsInClass = new HashMap<>();
        for (ClassificationClass classificationClass : classificationClasses) {
            uniqueWordsInClass.put(classificationClass.getName(), new ArrayList<>());
        }

        for (Document document : documents) {
            String[] terms = document.getContent().split(" ");
            List<String> uniqueTerms = new ArrayList<>();

            for (String term : terms) {
                if (term.isEmpty()) {
                    continue;
                }
                if (!uniqueTerms.contains(term)) {
                    uniqueTerms.add(term);
                    this.possibleCorpusWords++;
                }
                for (ClassificationClass docClass : document.getClassificationClasses()) {
                    if (!uniqueWordsInClass.get(docClass.getName()).contains(term)) {
                        uniqueWordsInClass.get(docClass.getName()).add(term);
                    }
                }
            }
        }

        for (ClassificationClass classificationClass : classificationClasses) {
            totalWordsInClass.put(classificationClass.getName(), uniqueWordsInClass.get(classificationClass.getName()).size());
        }
    }

    /**
     * Returns the probability of document belonging to given classification class based on model data.
     *
     * @param classificationClass classification class
     * @return probability of document belonging to given classification class
     */
    private double getClassProbability(ClassificationClass classificationClass) {
        if (classificationClass == null) {
            return 0;
        }

        double documentsInClass = 0;

        for (Document document : documents) {
            if (document.getClassificationClasses().contains(classificationClass)) {
                documentsInClass++;
            }
        }

        return documentsInClass / documents.size();
    }
}
