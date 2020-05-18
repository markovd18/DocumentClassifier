package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Utility class to load data from files.
 *
 * @author <a href=mailto:markovd@students.zcu.cz>David Markov</a>
 */
public class FileLoader {

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
     * Loads training data set in given folder.
     *
     * @param folderPath path to folder with training data
     * @return List of loaded training documents
     */
    public List<Document> loadTrainingSet(String folderPath) {
        File trainingSetFolder = new File(folderPath);
        if (!trainingSetFolder.isDirectory()) {
            System.out.println("Directory in given path was not found! (path: " + folderPath + ")");
            return null;
        }

        List<Document> trainingSet = new ArrayList<>();
        try {
            for (File file : Objects.requireNonNull(trainingSetFolder.listFiles())) {

                Document trainingDocument = loadDocument(file);
                for (ClassificationClass docClass : trainingDocument.getClassificationClasses()) {
                    docClass.incDocumentsInClass();
                }

                trainingSet.add(trainingDocument);
            }
        } catch (Exception e) {
            System.out.println("Directory is empty! (path: " + folderPath + ")");
            return null;
        }
        System.out.println("Training document set loaded.");
        return trainingSet;
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
}
