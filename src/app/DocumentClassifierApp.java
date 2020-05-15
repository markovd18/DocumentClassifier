package app;

import classifier.NaiveBayesClassifier;
import utils.ClassificationClass;
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
     * @param classesFile      path to file with the list of classification classes
     * @param trainingSetFile  path to folder with training data
     * @param testingSetFile   path to folder with testing data
     * @param featureAlgorithm name of feature algorithm
     * @param classifier       name of classifier
     * @param modelName        name which will be given to model when saving
     */
    public void doSupervisedLearning(String classesFile, String trainingSetFile, String testingSetFile,
                                     String featureAlgorithm, String classifier, String modelName) {
        if (!isClassifier(classifier)) {
            System.out.println("No classifier with this name found! (passed name: " + classifier + ")");
            return;
        }
        fileLoader = new FileLoader();
        List<ClassificationClass> classificationClasses = fileLoader.loadClassificationClasses(classesFile);
        classificationClasses.forEach((c) -> {
            if (c.equals("kul")) c.incDocumentsInClass();
        });


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
}