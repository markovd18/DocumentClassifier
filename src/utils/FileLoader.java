package utils;

import app.DocumentClassifierApp;

import java.io.*;
import java.util.*;

/**
 * Utility class to load data from files.
 *
 * @author <a href=mailto:markovd@students.zcu.cz>David Markov</a>
 */
public class FileLoader extends ModelOperator {

    /**
     * Buffered reader to load data.
     */
    private BufferedReader reader;

    /**
     * Loads document at given path.
     *
     * @param file existing document to be loaded
     * @return loaded document
     */
    public Document loadDocument(File file) {
        try {
            reader = new BufferedReader(new FileReader(file));
            Document document = new Document();
            String line;
            StringBuilder buffer = new StringBuilder();
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                if (isFirstLine) {   //if we are loading the first line of document, we are loading it's classification classes
                    String[] classes = line.split(" ");
                    List<ClassificationClass> classificationClasses = new ArrayList<>();

                    for (String className : classes) {
                        classificationClasses.add(new ClassificationClass(className));
                    }

                    document.setClassificationClasses(classificationClasses);
                    isFirstLine = false;
                } else {
                    buffer.append(line);
                }
            }
            document.setContent(buffer.toString().replaceAll("[^a-zA-Zá-žÁ-Ž ]", "").toLowerCase());
            reader.close();
            return document;

        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("File was not found! (file: " + file.getAbsolutePath() + ")");
            return null;
        } catch (IOException ioException) {
            System.out.println("Error while reading from file! (file: " + file.getAbsolutePath() + ")");
            return null;
        }
    }

    /**
     * Loads data set in given folder.
     *
     * @param folderPath path to folder with data
     * @return List of loaded documents
     */
    public List<Document> loadDataSet(String folderPath) {
        File dataSetFolder = new File(folderPath);
        if (!dataSetFolder.isDirectory()) {
            System.out.println("Directory in given path was not found! (path: " + folderPath + ")");
            return null;
        }

        List<Document> dataSet = new ArrayList<>();
        try {
            for (File file : Objects.requireNonNull(dataSetFolder.listFiles())) {

                Document document = loadDocument(file);
                for (ClassificationClass docClass : document.getClassificationClasses()) {
                    docClass.incDocumentsInClass();
                }

                dataSet.add(document);
            }
        } catch (Exception e) {
            System.out.println("Directory is empty! (path: " + folderPath + ")");
            return null;
        }
        System.out.println("Data set loaded.");
        return dataSet;
    }

    /**
     * Loads list of classification classes from given file.
     *
     * @param filePath path to file with classification classes
     * @return list of classification classes
     */
    public List<ClassificationClass> loadClassificationClasses(String filePath) {
        try {
            reader = new BufferedReader(new FileReader(filePath));
            List<ClassificationClass> classificationClasses = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                classificationClasses.add(new ClassificationClass(line));
            }
            System.out.println("Classification classes loaded.");
            reader.close();
            return classificationClasses;
        } catch (Exception e) {
            System.out.println("File containing classification classes not found! (" + filePath + ")");
            return null;
        }
    }

    /**
     * Loads trained model with given name. Returns null if given file name is not found.
     *
     * @param modelName name of the model (and it's file)
     * @return loaded model
     */
    public Model loadModel(String modelName) {
        try {
            reader = new BufferedReader(new FileReader(modelName + MODEL_FILE_EXTENSION));
            Model model = new Model(modelName);

            String line;
            while ((line = reader.readLine()) != null) {
                switch (line) {
                    case MODEL_CLASSIFICATION_CLASSES_FILE_TAG:
                        model.setClassificationClassesFile(reader.readLine());
                        break;
                    case MODEL_FEATURE_ALGORITHM_TAG:
                        model.setFeatureAlgorithm(reader.readLine());
                        break;
                    case MODEL_CLASSIFIER_TAG:
                        loadClassifier(model);
                        break;
                    case MODEL_TRAINING_SET_TAG:
                        model.setTrainingSet(new ArrayList<>());
                        break;
                    case MODEL_DOCUMENT_TAG:
                        loadModelDocument(model);
                        break;
                    default:
                        System.out.println("Invalid model file format!");
                        reader.close();
                        return null;
                }
            }

            reader.close();
            return model;

        } catch (IOException fileNotFoundException) {
            System.out.println("Model file not found! (path: " + modelName + MODEL_FILE_EXTENSION + ")");
            return null;
        }

    }

    /**
     * Loads classifier data into the model
     *
     * @param model loaded model
     */
    private void loadClassifier(Model model) throws IOException {
        model.setClassifier(reader.readLine());

        if (model.getClassifier().equals(DocumentClassifierApp.NAIVE_BAYES_CLASSIFIER)) {
            model.setTotalUniqueWords(Integer.parseInt(reader.readLine()));
            Map<String, Double> totalWordsMap = new HashMap<>();
            String[] uniqueWordsInClasses = reader.readLine().split(" ");
            for (String totalWordsInClass : uniqueWordsInClasses) {
                String[] classWordsPair = totalWordsInClass.split(";");
                totalWordsMap.put(classWordsPair[0], Double.parseDouble(classWordsPair[1]));
            }
            model.setTotalWordsInClass(totalWordsMap);
        }
    }

    /**
     * Loads document from model file and stores it into training set of given model.
     *
     * @param model loaded model
     * @throws IOException thrown when error occurs
     */
    private void loadModelDocument(Model model) throws IOException {
        Document document = new Document();

        List<ClassificationClass> classificationClasses = new ArrayList<>();
        String[] classes = reader.readLine().split(" ");

        for (String clazz : classes) {
            classificationClasses.add(new ClassificationClass(clazz));
        }
        document.setClassificationClasses(classificationClasses);

        Map<String, Double> documentFeatures = new HashMap<>();
        String[] features = reader.readLine().split(" ");
        StringBuilder content = new StringBuilder();
        for (String feature : features) {
            String[] featureParts = feature.split(";");
            documentFeatures.put(featureParts[0], Double.parseDouble(featureParts[1]));
            content.append(featureParts[0]).append(" ");
        }
        document.setFeatures(documentFeatures);
        document.setContent(content.toString());
        model.getTrainingSet().add(document);
    }
}
