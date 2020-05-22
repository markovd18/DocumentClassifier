package utils;

import java.util.List;
import java.util.Map;

/**
 * Document to be classified or to train the classifier by.
 *
 * @author <a href=mailto:markovd@students.zcu.cz>David Markov</a>
 */
public class Document {

    /**
     * Content of the document.
     */
    private String content;

    /**
     * Classification classes which this document belongs to.
     */
    private List<ClassificationClass> classificationClasses;

    /**
     * List of features representing this document, selected by feature any {@link feature.IFeatureAlgorithm}
     */
    private Map<String, Double> features;

    /**
     * Returns content of this document.
     *
     * @return content of this document
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets content of this document to given string.
     *
     * @param content desired content of the document
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Returns list of classification classes which this document belongs to.
     *
     * @return list of classification classes which this document belongs to
     */
    public List<ClassificationClass> getClassificationClasses() {
        return classificationClasses;
    }

    /**
     * Sets classification classes which this document belongs to to given list.
     *
     * @param classificationClasses desired list of classification classes
     */
    public void setClassificationClasses(List<ClassificationClass> classificationClasses) {
        this.classificationClasses = classificationClasses;
    }

    /**
     * Returns vector (list) of features (words) representing this document.
     *
     * @return vector of features representing this document
     */
    public Map<String, Double> getFeatures() {
        return features;
    }

    /**
     * Sets the vector (list) of features (words) representing this document to given vector.
     *
     * @param features desired vector of features
     */
    public void setFeatures(Map<String, Double> features) {
        this.features = features;
    }
}
