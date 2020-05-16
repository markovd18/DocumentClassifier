package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Classification class given to classified documents.
 *
 * @author <a href=mailto:markovd@students.zcu.cz>David Markov</a>
 */
public class ClassificationClass {

    private static final Map<String, Integer> numberOfDocumentsInClasses = new HashMap<>();
    private final String NAME;

    /**
     * Creates new classification class instance with given name.
     *
     * @param name name of the classification class
     */
    public ClassificationClass(String name) {
        this.NAME = name;
        if (!numberOfDocumentsInClasses.containsKey(this.NAME)) {
            numberOfDocumentsInClasses.put(this.NAME, 0);
        }
    }

    /**
     * Returns the name of the classification class.
     *
     * @return name of the classification class
     */
    public String getName() {
        return NAME;
    }

    /**
     * Returns number of documents that fall into this classification class.
     *
     * @return number of documents that fall into this classification class.
     */
    public int getDocumentsInClass() {
        return numberOfDocumentsInClasses.get(this.NAME);
    }

    /**
     * Increments the number of documents that fall into this classification class by one.
     */
    public void incDocumentsInClass() {
        numberOfDocumentsInClasses.replace(this.NAME, getDocumentsInClass() + 1);
    }

    /**
     * Decrements the number of documents that fall into this classification class by one.
     */
    public void decDocumentsInClass() {
        numberOfDocumentsInClasses.replace(this.NAME, getDocumentsInClass() - 1);
    }

    /**
     * Returns true if this classification class is the same (has the same name) as given
     * classification class. Returns {@code false} if parameter is {@code null} or is not
     * an instance of ClassificationClass.
     *
     * @param object compared classification class
     * @return true if this classification class has the same name
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ClassificationClass)) {
            return false;
        }

        ClassificationClass obj = (ClassificationClass) object;
        return obj.getName().equals(this.NAME);
    }

}
