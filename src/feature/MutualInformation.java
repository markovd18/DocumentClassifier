package feature;

import utils.ClassificationClass;
import utils.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mutual information algorithm selects features based on information each term carries.
 *
 * @author <a href=mailto:markovd@students.zcu.cz>David Markov</a>
 */
public class MutualInformation extends FeatureSelector implements IFeatureAlgorithm {

    /**
     * Training set of documents.
     */
    private final List<Document> documents;

    /**
     * Creates instance of Mutual Information (MI) algorithm. Algorithm needs training set of documents.
     *
     * @param documents training set of documents
     */
    public MutualInformation(List<Document> documents) {
        this.documents = documents;
    }

    @Override
    public void createFeatures(Document document, int featureCount) {
        if (document == null) {
            return;
        }

        List<String> features = new ArrayList<>();
        List<ClassificationClass> docClasses = document.getClassificationClasses();
        String[] terms = document.getContent().split(" ");
        int featuresPerClass = featureCount / docClasses.size();

        for (ClassificationClass clazz : docClasses) {
            Map<String, Double> termInformations = getTermInformationsForClass(clazz, terms);
            features.addAll(selectFeatures(termInformations, featuresPerClass));
        }
        document.setFeatures(features);
    }

    /**
     * Returns information each term in documents carries for given classification class.
     *
     * @param clazz classification class for which we compute term informations
     * @param terms terms to compute informations for
     * @return hash map containing terms and information they carry
     */
    private Map<String, Double> getTermInformationsForClass(ClassificationClass clazz, String[] terms) {
        Map<String, Double> termInformations = new HashMap<>();

        for (String term : terms) {
            if (term.isEmpty()) {
                continue;
            }

            double termInformation = computeTermInformation(term, clazz);
            termInformations.put(term, termInformation);
        }
        return termInformations;
    }

    /**
     * Computes information that given term carries for given classification class.
     *
     * @param term  term to compute information for
     * @param clazz classification class in which the term is
     * @return amount of information that given term carries
     */
    private double computeTermInformation(String term, ClassificationClass clazz) {
        double totalNumOfDocuments = documents.size(); //set to double to evade integer division
        double logDivider = Math.log(2);  //we devide with this the log result to get log with base 2
        int docsInClass = 0;    //documents that are in given classification class
        int docsNoClass = 0;    //documents that are not

        int docsWithoutTerm = 0;
        int docsContainingTerm = 0;

        int docsInClassContainingTerm = 0;
        int docsInClassNoTerm = 0;

        int docsNoClassContainingTerm = 0;
        int docsNoClassNoTerm = 0;

        for (Document corpusDocument : documents) {
            if (corpusDocument.getClassificationClasses().contains(clazz)) {
                docsInClass++;
                if (corpusDocument.getContent().contains(term)) {
                    docsContainingTerm++;
                    docsInClassContainingTerm++;
                } else {
                    docsWithoutTerm++;
                    docsInClassNoTerm++;
                }
            } else {
                docsNoClass++;
                if (corpusDocument.getContent().contains(term)) {
                    docsContainingTerm++;
                    docsNoClassContainingTerm++;
                } else {
                    docsWithoutTerm++;
                    docsNoClassNoTerm++;
                }
            }
        }

        return (docsInClassContainingTerm / totalNumOfDocuments)
                * (Math.log((totalNumOfDocuments * docsInClassContainingTerm) / (docsContainingTerm * docsInClass)) / logDivider)
                + (docsInClassNoTerm / totalNumOfDocuments)
                * (Math.log((totalNumOfDocuments * docsInClassNoTerm) / (docsWithoutTerm * docsInClass)) / logDivider)
                + (docsNoClassContainingTerm / totalNumOfDocuments)
                * (Math.log((totalNumOfDocuments * docsNoClassContainingTerm) / (docsContainingTerm * docsNoClass)) / logDivider)
                + (docsNoClassNoTerm / totalNumOfDocuments)
                * (Math.log((totalNumOfDocuments * docsNoClassNoTerm) / (docsWithoutTerm * docsNoClass)) / logDivider);
    }
}
