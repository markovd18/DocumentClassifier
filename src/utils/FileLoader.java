package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

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
     * @param filePath path do file to be loaded as document
     * @return loaded document
     */
    public Document loadDocument(String filePath) {
        return null;
    }

    /**
     * Loads training data set in given folder.
     *
     * @param folderPath path to folder with training data
     * @return List of loaded training documents
     */
    public List<Document> loadTrainingSet(String folderPath) {
        return null;
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

            reader.close();
            return classificationClasses;
        } catch (Exception e) {
            System.out.println("File containing classification classes not found! (" + filePath + ")");
            return null;
        }
    }
}
