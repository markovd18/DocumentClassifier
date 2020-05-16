package app;

import classifier.NaiveBayesClassifier;
import feature.IFeatureAlgorithm;
import feature.TFIDF;
import feature.TermFrequency;
import utils.ClassificationClass;
import utils.Document;
import utils.FileLoader;

import java.util.List;

/**
 * Entry point for the DocumentClassifier application.
 *
 * @author <a href=mailto:markovd@students.zcu.cz>David Markov</a>
 */
public class DocumentClassifierApp {

    /**
     * Number of parameters required to perform supervised learning.
     */
    public static final int TRAINING_PARAMS_COUNT = 6;
    /**
     * Number of parameters required to perform classifying of user input.
     */
    public static final int CLASSIFYING_PARAMS_COUNT = 1;
    /**
     * String representing name of {@link NaiveBayesClassifier} passed as parameter in command-line.
     */
    public static final String NAIVE_BAYES_CLASSIFIER = "bayes";
    /**
     * String representing name of {} passed as parameter in command-line.
     */
    public static final String TF_FEATURE_ALG = "tf";
    /**
     * String representing name of {} passed as parameter in command-line.
     */
    public static final String TF_IDF_FEATURE_ALG = "tfidf";
    /**
     * Number of features that will represent each document.
     */
    public static final int FEATURE_COUNT = 30;
    /**
     * File loader for loading data.
     */
    private FileLoader fileLoader;

    public static void main(String[] args) {
        DocumentClassifierApp documentClassifierApp = new DocumentClassifierApp();
        documentClassifierApp.start(args);
    }

    /**
     * Executes DocumentClassifier application process.
     *
     * @param args arguments passed from command-line.
     */
    public void start(String[] args) {
        switch (args.length) {
            case TRAINING_PARAMS_COUNT:
                System.out.println("Executing supervised learning.");
                doSupervisedLearning(args[0], args[1], args[2], args[3], args[4], args[5]);
                break;
            case CLASSIFYING_PARAMS_COUNT:
                System.out.println("Executing input classification.");
                //TODO klasifikace textu z GUI
                break;
            default:
                System.out.println("Invalid number of parameters inserted!");
                break;
        }
    }

    /**
     * Executes supervised learning process.
     *
     * @param classesFile       path to file with the list of classification classes
     * @param trainingSetFolder path to folder with training data
     * @param testingSetFolder  path to folder with testing data
     * @param featureAlgorithm  name of feature algorithm
     * @param classifier        name of classifier
     * @param modelName         name which will be given to model when saving
     */
    public void doSupervisedLearning(String classesFile, String trainingSetFolder, String testingSetFolder,
                                     String featureAlgorithm, String classifier, String modelName) {
        if (!isClassifier(classifier)) {
            System.out.println("No classifier with this name found! (passed name: " + classifier + ")");
            System.out.println("Available classifiers:\n<passed_name> - <description>");
            System.out.println("bayes - Naive Bayes classifier");
            //TODO druhý klasifikátor
            return;
        }
        if (!isFeatureAlgorithm(featureAlgorithm)) {
            System.out.println("No feature algorithm with this name found! (passed name: " + featureAlgorithm + ")");
            System.out.println("Available feature algorithms:\n<passed_name> - <description>");
            System.out.println("tf - term frequency (document frequency) algorithm");
            System.out.println("tfidf - term frequency-inverse document frequency");
            //TODO třetí algoritmus
            return;
        }
        fileLoader = new FileLoader();
        List<ClassificationClass> classificationClasses = fileLoader.loadClassificationClasses(classesFile);
        List<Document> trainingSet = fileLoader.loadTrainingSet(trainingSetFolder);
        createFeatures(featureAlgorithm, trainingSet);


    }

    /**
     * Returns true if there is name that represents any classifier.
     *
     * @param classifierName name that represents classifier
     * @return true if classifier with given name exists
     */
    public boolean isClassifier(String classifierName) {
        //TODO druhý klasifikátor
        return classifierName.equals(NAIVE_BAYES_CLASSIFIER);
    }

    /**
     * Returns true if there is name that represents any feature algorithm.
     *
     * @param featureAlgName name that represents feature algorithm
     * @return true if algorithm with given name exists
     */
    public boolean isFeatureAlgorithm(String featureAlgName) {
        //TODO třetí parametrizační algoritmus
        return featureAlgName.equals(TF_FEATURE_ALG) || featureAlgName.equals(TF_IDF_FEATURE_ALG);
    }

    /**
     * Creates features for list of given documents with given algorithm.
     *
     * @param featureAlgName name of feature algorithm
     * @param documents      list of documents to create features for
     */
    private void createFeatures(String featureAlgName, List<Document> documents) {
        IFeatureAlgorithm featureAlgorithm;

        if (featureAlgName.equals(TF_FEATURE_ALG)) {
            featureAlgorithm = new TermFrequency();
        } else if (featureAlgName.equals(TF_IDF_FEATURE_ALG)) {
            featureAlgorithm = new TFIDF(documents);
        } else {
            System.out.println("Invalid feature algorithm name passed! (passed name: " + featureAlgName + ")");
            return;
        }
        for (Document document : documents) {
            featureAlgorithm.createFeatures(document, FEATURE_COUNT);
        }
    }
}