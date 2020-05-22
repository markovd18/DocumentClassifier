package utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * File saver is used to save information into files.
 *
 * @author <a href=mailto:markovd@students.zcu.cz>David Markov</a>
 */
public class FileSaver extends ModelOperator {

    /**
     * Saves given model into {@code .model} file with name matching the name of given model.
     *
     * @param model saved model
     */
    public void saveModel(Model model) {
        try {
            File outputFile = new File(model.getName() + MODEL_FILE_EXTENSION);
            outputFile.createNewFile();

            PrintWriter printWriter = new PrintWriter(model.getName() + MODEL_FILE_EXTENSION);
            //saving classification classes file
            printWriter.println(MODEL_CLASSIFICATION_CLASSES_FILE_TAG);
            printWriter.println(model.getClassificationClassesFile());

            printWriter.println(MODEL_FEATURE_ALGORITHM_TAG);
            printWriter.println(model.getFeatureAlgorithm());

            printWriter.println(MODEL_CLASSIFIER_TAG);
            printWriter.println(model.getClassifier());
            if (model.getTotalUniqueWords() != 0 && model.getTotalWordsInClass() != null) {
                printWriter.println(model.getTotalUniqueWords());
                for (Map.Entry<String, Double> classWords : model.getTotalWordsInClass().entrySet()) {
                    printWriter.print(classWords.getKey() + ";" + classWords.getValue() + " ");
                }
                printWriter.println();
            }

            printWriter.println(MODEL_TRAINING_SET_TAG);
            for (Document document : model.getTrainingSet()) {
                printWriter.println(MODEL_DOCUMENT_TAG);
                for (ClassificationClass classificationClass : document.getClassificationClasses()) {
                    printWriter.print(classificationClass.getName() + " ");
                }
                printWriter.println();
                for (Map.Entry<String, Double> feature : document.getFeatures().entrySet()) {
                    printWriter.print(feature.getKey() + ";" + feature.getValue() + " ");
                }
                printWriter.println();
            }
            printWriter.close();
        } catch (IOException ioException) {
            System.out.println("Error while saving model!");
        }


    }
}
