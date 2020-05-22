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
     * Sum of values of all the words in each class.
     */
    private Map<String, Double> totalWordsInClass;
    /**
     * Sum of unique words in training set.
     */
    private int totalUniqueWords;


    public NaiveBayesClassifier(List<Document> documents, List<ClassificationClass> classificationClasses) {
        this.documents = documents;
        this.classificationClasses = classificationClasses;
    }

    @Override
    public List<ClassificationClass> classifyDocument(Document document) {
        if (document == null || document.getContent().isEmpty()) {
            return null;
        }
        if (totalUniqueWords == 0 && totalWordsInClass == null) {
            this.totalWordsInClass = new HashMap<>();
            getWordCounts();
        }

        List<ClassificationClass> documentClasses = new ArrayList<>();
        Map<ClassificationClass, Double> classProbabilities = new HashMap<>();

        for (ClassificationClass classificationClass : classificationClasses) {
            double p_documentClass = getClassProbability(classificationClass);

            for (String word : document.getContent().split(" ")) {
                if (word.isEmpty()) {
                    continue;
                }

                double termOccurence = getTermOccurrence(word, classificationClass);

                double p_wordClass = (termOccurence + 1.0) / (this.totalWordsInClass.get(classificationClass.getName()) + totalUniqueWords);
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
    private double getTermOccurrence(String term, ClassificationClass classificationClass) {
        int termOccurrence = 0;

        for (Document document : documents) {
            if (document.getClassificationClasses().contains(classificationClass)) {
                if (document.getFeatures().containsKey(term)) {
                    termOccurrence += document.getFeatures().get(term);
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
        List<String> uniqueWords = new ArrayList<>();

        for (ClassificationClass classificationClass : classificationClasses) {
            uniqueWordsInClass.put(classificationClass.getName(), new ArrayList<>());
        }

        for (Document document : documents) {
            String[] terms = document.getContent().split(" ");

            for (String term : terms) {
                if (term.isEmpty()) {
                    continue;
                }
                if (!uniqueWords.contains(term)) {
                    uniqueWords.add(term);
                }

                for (ClassificationClass docClass : document.getClassificationClasses()) {
                    if (!uniqueWordsInClass.get(docClass.getName()).contains(term)) {
                        uniqueWordsInClass.get(docClass.getName()).add(term);
                    }
                    if (totalWordsInClass.containsKey(docClass.getName())) {
                        this.totalWordsInClass.put(docClass.getName(), this.totalWordsInClass.get(docClass.getName()) + document.getFeatures().get(term));
                    } else {
                        this.totalWordsInClass.put(docClass.getName(), document.getFeatures().get(term));
                    }
                }
            }
        }

        this.totalUniqueWords = uniqueWords.size();
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

    /**
     * Returns sums of values of all words in each classification class
     *
     * @return sums of values of all words in each classification class
     */
    public Map<String, Double> getTotalWordsInClass() {
        return totalWordsInClass;
    }

    /**
     * Sets sums of values of all words in each classification class
     *
     * @param totalWordsInClass sums of values of all words in each classification class
     */
    public void setTotalWordsInClass(Map<String, Double> totalWordsInClass) {
        this.totalWordsInClass = totalWordsInClass;
    }

    /**
     * Returns number of unique words in training set
     *
     * @return number of unique words in training set
     */
    public int getTotalUniqueWords() {
        return totalUniqueWords;
    }

    /**
     * Sets number of unique words in training set to a given nimber
     *
     * @param totalUniqueWords number of unique words in training set
     */
    public void setTotalUniqueWords(int totalUniqueWords) {
        this.totalUniqueWords = totalUniqueWords;
    }
}
