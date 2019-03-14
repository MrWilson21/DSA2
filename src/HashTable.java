public abstract class HashTable {
    int capacity=10;
    int size=0;

    public int size()
    {
        return size;
    }

    /**
     * Adds the specified element to this hash table if it is not already present
     *
     * @param obj
     * @return true if the element is successfully added
     */
    abstract boolean add(Object obj);

    /**
     *
     * @param obj
     * @return true if this hash table contains the specified element
     */
    abstract boolean contains(Object obj);
    /**
     *
     * @param obj
     * @return Removes the specified element from this set if it is present
     */
    abstract boolean remove(Object obj);
}