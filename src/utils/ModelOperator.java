package utils;

/**
 * File operator class is an abstract class with attributes containing information about tags in file containing saved model.
 * This class will be the base class of all classes that need to either load or save {@link Model}.
 *
 * @author <a href=mailto:markovd@students.zcu.cz>David Markov</a>
 */
public abstract class ModelOperator {

    /**
     * The extension of file where {@link Model} is saved.
     */
    public static final String MODEL_FILE_EXTENSION = ".model";


}
