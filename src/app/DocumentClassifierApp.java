package app;

import classifier.IClassifier;
import classifier.NaiveBayesClassifier;
import feature.IFeatureAlgorithm;
import feature.MutualInformation;
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
     * String representing name of {@link TermFrequency} algorithm, passed as parameter in command-line.
     */
    public static final String TF_FEATURE_ALG = "tf";
    /**
     * String representing name of {@link TFIDF} algorithm, passed as parameter in command-line.
     */
    public static final String TF_IDF_FEATURE_ALG = "tfidf";
    /**
     * String representing name of {@link MutualInformation} algorithm, passed as parameter in command-line.
     */
    public static final String MI_FEATURE_ALG = "mi";
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
                System.out.println("Executing supervised learning...");
                doSupervisedLearning(args[0], args[1], args[2], args[3], args[4], args[5]);
                break;
            case CLASSIFYING_PARAMS_COUNT:
                System.out.println("Executing input classification...");
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
            System.out.println("tfidf - term frequency-inverse document frequency algorithm");
            System.out.println("mi - mutual information algorithm");
            return;
        }
        fileLoader = new FileLoader();
        List<ClassificationClass> classificationClasses = fileLoader.loadClassificationClasses(classesFile);
        List<Document> trainingSet = fileLoader.loadDataSet(trainingSetFolder);
        createFeatures(featureAlgorithm, trainingSet);

        List<Document> testingSet = fileLoader.loadDataSet(testingSetFolder);
        createFeatures(featureAlgorithm, testingSet);
        classifyDocuments(classifier, trainingSet, testingSet, classificationClasses);

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
        return featureAlgName.equals(TF_FEATURE_ALG) || featureAlgName.equals(TF_IDF_FEATURE_ALG)
                || featureAlgName.equals(MI_FEATURE_ALG);
    }

    /**
     * Creates features for list of given documents with given algorithm.
     *
     * @param featureAlgName name of feature algorithm
     * @param documents      list of documents to create features for
     */
    private void createFeatures(String featureAlgName, List<Document> documents) {
        IFeatureAlgorithm featureAlgorithm;

        switch (featureAlgName) {
            case TF_FEATURE_ALG:
                featureAlgorithm = new TermFrequency();
                break;
            case TF_IDF_FEATURE_ALG:
                featureAlgorithm = new TFIDF(documents);
                break;
            case MI_FEATURE_ALG:
                featureAlgorithm = new MutualInformation(documents);
                break;
            default:
                System.out.println("Invalid feature algorithm name passed! (passed name: " + featureAlgName + ")");
                return;
        }

        System.out.println("Computing features...");
        for (Document document : documents) {
            featureAlgorithm.createFeatures(document, FEATURE_COUNT);
        }
        System.out.println("Features for document set computed.");
    }

    /**
     * Classifies all documents in given list with given classifier.
     *
     * @param classifierName        name of classifier
     * @param trainingSet           list of training documents
     * @param testingSet            list of documents to classify
     * @param classificationClasses list of available classification classes
     */
    private void classifyDocuments(String classifierName, List<Document> trainingSet, List<Document> testingSet,
                                   List<ClassificationClass> classificationClasses) {
        IClassifier classifier;

        switch (classifierName) {
            case NAIVE_BAYES_CLASSIFIER:
                classifier = new NaiveBayesClassifier(trainingSet, classificationClasses);
                break;
            default:
                System.out.println("Invalid classifier name passed! (passed name: " + classifierName + ")");
                return;
        }

        double numOfClassifiedUnits = 0;     //double to evade integer division
        int numOfCorrectlyClassified = 0;
        System.out.println("Classifying documents...");
        for (Document document : testingSet) {
            List<ClassificationClass> classified = classifier.classifyDocument(document);
            numOfClassifiedUnits += document.getClassificationClasses().size();

            for (ClassificationClass classificationClass : classified) {
                if (document.getClassificationClasses().contains(classificationClass)) {
                    numOfCorrectlyClassified++;
                }
            }
        }
        double accuracy = numOfCorrectlyClassified / numOfClassifiedUnits;
        System.out.println("Classification complete.");
        System.out.println("Number of recognised classification classes: " + numOfClassifiedUnits);
        System.out.println("Number of correctly recognised classification classes: " + numOfCorrectlyClassified);
        System.out.println("Accuracy: " + accuracy);
    }
}