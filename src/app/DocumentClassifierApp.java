package app;

import classifier.IClassifier;
import classifier.KNN;
import classifier.NaiveBayesClassifier;
import feature.IFeatureAlgorithm;
import feature.TFIDF;
import feature.TermBinary;
import feature.TermFrequency;
import utils.*;

import javax.swing.*;
import java.awt.*;
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
     * String representing name of {@link KNN} passed as parameter in command-line.
     */
    public static final String KNN_CLASSIFIER = "knn";
    /**
     * String representing name of {@link TermFrequency} algorithm, passed as parameter in command-line.
     */
    public static final String TF_FEATURE_ALG = "tf";
    /**
     * String representing name of {@link TFIDF} algorithm, passed as parameter in command-line.
     */
    public static final String TF_IDF_FEATURE_ALG = "tfidf";
    /**
     * String representing name of {@link TermFrequency} algorithm, passed as parameter in command-line.
     */
    public static final String BIN_FEATURE_ALG = "binary";

    /**
     * List of available classification classes.
     */
    private List<ClassificationClass> classificationClasses;

    /**
     * Training set of documents.
     */
    private List<Document> trainingSet;

    /**
     * Classifier used to classify documents / input.
     */
    private IClassifier classifier;

    /**
     * Feature algorithm used to compute features of each document.
     */
    private IFeatureAlgorithm featureAlgorithm;


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
                doInputClassification(args[0]);
                break;
            default:
                System.out.println("Invalid number of parameters inserted!");
                break;
        }
    }

    /**
     * Executes user input classification process.
     *
     * @param modelName name of loaded model
     */
    private void doInputClassification(String modelName) {
        FileLoader fileLoader = new FileLoader();
        Model model = fileLoader.loadModel(modelName);

        if (model == null) {
            System.out.println("Model not found! (name: " + modelName + ")");
            return;
        }

        this.classificationClasses = fileLoader.loadClassificationClasses(model.getClassificationClassesFile());
        this.trainingSet = model.getTrainingSet();
        switch (model.getFeatureAlgorithm()) {
            case TF_FEATURE_ALG:
                this.featureAlgorithm = new TermFrequency();
                break;
            case TF_IDF_FEATURE_ALG:
                this.featureAlgorithm = new TFIDF(trainingSet);
                break;
            case BIN_FEATURE_ALG:
                this.featureAlgorithm = new TermBinary();
                break;
            default:
                System.out.println("Invalid feature algorithm name! (name: " + model.getFeatureAlgorithm() + ")");
                return;
        }

        switch (model.getClassifier()) {
            case NAIVE_BAYES_CLASSIFIER:
                NaiveBayesClassifier classifier = new NaiveBayesClassifier(trainingSet, classificationClasses);
                classifier.setTotalUniqueWords(model.getTotalUniqueWords());
                classifier.setTotalWordsInClass(model.getTotalWordsInClass());
                this.classifier = classifier;
                break;
            case KNN_CLASSIFIER:
                this.classifier = new KNN(trainingSet);
                break;
            default:
                System.out.println("Invalid classifier name! (name: " + model.getClassifier() + ")");
                return;
        }

        createGui(modelName);
    }

    /**
     * Creates simple GUI for input classification.
     */
    private void createGui(String modelName) {
        JFrame frame = new JFrame();

        JPanel centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(640, 320));
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(centerPanel, BorderLayout.CENTER);

        TextArea inputTextArea = new TextArea();

        JButton classifyBtn = new JButton("Classify");
        JPanel bottomPanel = new JPanel();
        Label classLabel = new Label("Classified category: ");
        classifyBtn.addActionListener(listener -> {
            classLabel.setText("Classified category: " + classifyUserInput(inputTextArea.getText()));
            bottomPanel.validate();
        });
        centerPanel.add(inputTextArea);
        centerPanel.add(classifyBtn);


        bottomPanel.add(new Label("Model name: " + modelName));

        bottomPanel.add(classLabel);

        frame.add(bottomPanel, BorderLayout.PAGE_END);
        frame.setTitle("Document classifier");
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Classifies user input in given string.
     *
     * @param userInput user input
     * @return name of classification class
     */
    private String classifyUserInput(String userInput) {
        Document input = new Document();
        input.setContent(userInput);

        featureAlgorithm.createFeatures(input);
        List<ClassificationClass> resultClass = classifier.classifyDocument(input);
        if (resultClass != null && !resultClass.isEmpty()) {
            return resultClass.get(0).getName();
        } else {
            return "";
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
            System.out.println("knn - k-nearest neighbours classifier");
            return;
        }
        if (!isFeatureAlgorithm(featureAlgorithm)) {
            System.out.println("No feature algorithm with this name found! (passed name: " + featureAlgorithm + ")");
            System.out.println("Available feature algorithms:\n<passed_name> - <description>");
            System.out.println("tf - term frequency (document frequency) algorithm");
            System.out.println("tfidf - term frequency-inverse document frequency algorithm");
            System.out.println("binary - binary feature algorithm");
            return;
        }
        FileLoader fileLoader = new FileLoader();
        classificationClasses = fileLoader.loadClassificationClasses(classesFile);

        System.out.println("Loading training set...");
        trainingSet = fileLoader.loadDataSet(trainingSetFolder);
        createFeatures(featureAlgorithm, trainingSet);

        System.out.println("Loading testing set...");
        List<Document> testingSet = fileLoader.loadDataSet(testingSetFolder);
        createFeatures(featureAlgorithm, testingSet);
        classifyDocuments(classifier, trainingSet, testingSet, classificationClasses);

        Model model = new Model(modelName);
        model.setTrainingSet(trainingSet);
        model.setFeatureAlgorithm(featureAlgorithm);
        model.setClassifier(classifier);
        model.setClassificationClassesFile(classesFile);
        if (this.classifier instanceof NaiveBayesClassifier) {
            NaiveBayesClassifier bayes = (NaiveBayesClassifier) this.classifier;
            model.setTotalUniqueWords(bayes.getTotalUniqueWords());
            model.setTotalWordsInClass(bayes.getTotalWordsInClass());
        }
        System.out.println("Saving model as \"" + modelName + "\"...");
        FileSaver fileSaver = new FileSaver();
        fileSaver.saveModel(model);
        System.out.println("Model \"" + modelName + "\" saved.");
    }

    /**
     * Returns true if there is name that represents any classifier.
     *
     * @param classifierName name that represents classifier
     * @return true if classifier with given name exists
     */
    public boolean isClassifier(String classifierName) {
        return classifierName.equals(NAIVE_BAYES_CLASSIFIER) || classifierName.equals(KNN_CLASSIFIER);
    }

    /**
     * Returns true if there is name that represents any feature algorithm.
     *
     * @param featureAlgName name that represents feature algorithm
     * @return true if algorithm with given name exists
     */
    public boolean isFeatureAlgorithm(String featureAlgName) {
        return featureAlgName.equals(TF_FEATURE_ALG) || featureAlgName.equals(TF_IDF_FEATURE_ALG)
                || featureAlgName.equals(BIN_FEATURE_ALG);
    }

    /**
     * Creates features for list of given documents with given algorithm.
     *
     * @param featureAlgName name of feature algorithm
     * @param documents      list of documents to create features for
     */
    private void createFeatures(String featureAlgName, List<Document> documents) {

        switch (featureAlgName) {
            case TF_FEATURE_ALG:
                featureAlgorithm = new TermFrequency();
                break;
            case TF_IDF_FEATURE_ALG:
                featureAlgorithm = new TFIDF(documents);
                break;
            case BIN_FEATURE_ALG:
                featureAlgorithm = new TermBinary();
                break;
            default:
                System.out.println("Invalid feature algorithm name passed! (passed name: " + featureAlgName + ")");
                return;
        }

        System.out.println("Computing features...");
        for (Document document : documents) {
            featureAlgorithm.createFeatures(document);
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
        System.out.println("Classifying documents...");
        switch (classifierName) {
            case NAIVE_BAYES_CLASSIFIER:
                classifier = new NaiveBayesClassifier(trainingSet, classificationClasses);
                break;
            case KNN_CLASSIFIER:
                classifier = new KNN(trainingSet);
                break;
            default:
                System.out.println("Invalid classifier name passed! (passed name: " + classifierName + ")");
                return;
        }

        double numOfClassifiedDocuments = testingSet.size();     //double to evade integer division
        int numOfCorrectlyClassified = 0;
        for (Document document : testingSet) {
            List<ClassificationClass> classified = classifier.classifyDocument(document);

            for (ClassificationClass classificationClass : classified) {
                if (document.getClassificationClasses().contains(classificationClass)) {
                    numOfCorrectlyClassified++;
                    break;
                }
            }
        }
        double accuracy = numOfCorrectlyClassified / numOfClassifiedDocuments;
        System.out.println("Classification complete.");
        System.out.println("Number of classified documents: " + numOfClassifiedDocuments);
        System.out.println("Number of correctly classified documents: " + numOfCorrectlyClassified);
        System.out.println("Accuracy: " + accuracy);
    }

}