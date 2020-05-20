package utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

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

            printWriter.close();
        } catch (IOException ioException) {
            System.out.println("Error while saving model!");
        }


    }
}
