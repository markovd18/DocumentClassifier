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
    /**
     * Tag in model file followed by the name of file containing the list of available classification classes.
     */
    public static final String MODEL_CLASSIFICATION_CLASSES_FILE_TAG = "<CLASSIFICATION_CLASSES_FILE>";
    /**
     * Tag in model file followed by the name of used feature algorithm.
     */
    public static final String MODEL_FEATURE_ALGORITHM_TAG = "<MODEL_FEATURE_ALGORITHM>";
    /**
     * Tag in model file followed by the name of used classifier.
     */
    public static final String MODEL_CLASSIFIER_TAG = "<MODEL_CLASSIFIER>";
    /**
     * Tag in model file followed by information about training documents nad their features.
     */
    public static final String MODEL_TRAINING_SET_TAG = "<TRAINING_SET>";
    /**
     * Tag in model file followed by information about training document.
     */
    public static final String MODEL_DOCUMENT_TAG = "<DOCUMENT>";
}
