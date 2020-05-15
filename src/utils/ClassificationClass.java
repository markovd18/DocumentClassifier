package utils;

/**
 * Classification class given to classified documents.
 *
 * @author <a href=mailto:markovd@students.zcu.cz>David Markov</a>
 */
public class ClassificationClass {
    private final String NAME;
    private int documentsInClass;

    /**
     * Creates new classification class instance with given name.
     *
     * @param name name of the classification class
     */
    public ClassificationClass(String name) {
        this.NAME = name;
        this.documentsInClass = 0;
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
        return documentsInClass;
    }

    /**
     * Increments the number of documents that fall into this classification class by one.
     */
    public void incDocumentsInClass() {
        documentsInClass++;
    }

    /**
     * Decrements the number of documents that fall into this classification class by one.
     */
    public void decDocumentsInClass() {
        documentsInClass--;
    }

    /**
     * Returns true if this classification class has given name.
     *
     * @param name name of clasification class
     * @return true if this classification class has given name
     */
    public boolean equals(String name) {
        return name.equals(this.NAME);
    }
}
