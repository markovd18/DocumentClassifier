package feature;

import utils.Document;

/**
 * Interface implemented by each feature algorithm.
 *
 * @author <a href=mailto:markovd@students.zcu.cz>David Markov</a>
 */
public interface IFeatureAlgorithm {

    /**
     * Creates feature vector from the content of passed {@link Document}
     *
     * @param document document for which we want to create features
     */
    void createFeatures(Document document);
}
