package classifier;

import utils.ClassificationClass;
import utils.Document;

import java.util.List;

/**
 * Interface implemented by every classifier.
 *
 * @author <a href=mailto:markovd@students.zcu.cz>David Markov</a>
 */
public interface IClassifier {

    /**
     * Classifies given document into one or more classification classes.
     *
     * @param document classified document
     * @return classification classes assigned to document
     */
    List<ClassificationClass> classifyDocument(Document document);
}
