package utils;

import java.util.List;
import java.util.Map;

/**
 * Model class is used to simplify loading and saving of trained classifier;
 *
 * @author <a href=mailto:markovd@students.zcu.cz>David Markov</a>
 */
public class Model {
    /**
     * Name of the model. When saving, model will be stored in {@code .model} file with this name.
     */
    private String name;
    /**
     * List of documents used to train classifier.
     */
    private List<Document> trainingSet;
    /**
     * Name of feature algorithm used to compute features.
     */
    private String featureAlgorithm;
    /**
     * Name of classifier used for document classification.
     */
    private String classifier;
    /**
     * Number of unique words in trained model.
     */
    private int totalUniqueWords;
    /**
     * Sum of values of all the words in each class.
     */
    private Map<String, Double> totalWordsInClass;
    /**
     * Name of file containing the list of available classification classes.
     */
    private String classificationClassesFile;


    /**
     * Creates model with given name.
     *
     * @param name name of the model
     */
    public Model(String name) {
        this.name = name;
    }

    /**
     * Returns the name of this model.
     *
     * @return name of this model
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of this model to given string.
     *
     * @param name name of the model
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the list of documents used for training the classifier in this model.
     *
     * @return list of documents used for training the classifier
     */
    public List<Document> getTrainingSet() {
        return trainingSet;
    }

    /**
     * Sets the list of documents used for training the classifier to given list.
     *
     * @param trainingSet list of documents used for training the classifier
     */
    public void setTrainingSet(List<Document> trainingSet) {
        this.trainingSet = trainingSet;
    }

    /**
     * Returns the name of feature algorithm used to compute features in this model
     *
     * @return name of feature algorithm used to compute features
     */
    public String getFeatureAlgorithm() {
        return featureAlgorithm;
    }

    /**
     * Sets the name of feature algorithm used to compute features in this model to given string.
     *
     * @param featureAlgorithm name of feature algorithm used to compute features
     */
    public void setFeatureAlgorithm(String featureAlgorithm) {
        this.featureAlgorithm = featureAlgorithm;
    }

    /**
     * Returns the name of the classifier used to classify documents in this model to given string.
     *
     * @return name of the classifier used to classify documents in this model
     */
    public String getClassifier() {
        return classifier;
    }

    /**
     * Sets the name of the classifier used to classify documents in this model to given string.
     *
     * @param classifier name of the classifier used to classify documents in this model
     */
    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    /**
     * Returns path to file with list of available classification classes.
     *
     * @return path to file with list of available classification classes
     */
    public String getClassificationClassesFile() {
        return classificationClassesFile;
    }

    /**
     * Sets path to file with list of available classification classes.
     *
     * @param classificationClassesFile path to file with list of available classes
     */
    public void setClassificationClassesFile(String classificationClassesFile) {
        this.classificationClassesFile = classificationClassesFile;
    }

    /**
     * Returns number of unique words in training set.
     *
     * @return number of unique words in training set
     */
    public int getTotalUniqueWords() {
        return totalUniqueWords;
    }

    /**
     * Sets number of unique words in training set to given number
     *
     * @param totalUniqueWords number of unique words in training set
     */
    public void setTotalUniqueWords(int totalUniqueWords) {
        this.totalUniqueWords = totalUniqueWords;
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
}
