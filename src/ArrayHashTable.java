public class ArrayHashTable extends HashTable
{
    //Define default capacity
    static final int CAPACITY = 10;

    Object[][] table;
    int[] counts;
    int chainSize;

    ArrayHashTable()
    {
        capacity = CAPACITY;
        chainSize = 5;
        counts = new int[capacity];
        table = new Object[capacity][];
    }

    @Override
    public boolean add(Object obj)
    {
        int hash = obj.hashCode() % capacity;

        //If array at hash index is not initialised then initialise it and add object to first index
        if(table[hash] == null)
        {
            table[hash] = new Object[chainSize];
            table[hash][0] = obj;
            counts[hash] += 1;
            return true;
        }
        //Else array is already initialised
        else
        {
            //Check if object already exists in hash table
            for(int i=0; i < counts[hash] + 1; i++)
            {
                if (table[hash][i].equals(obj))
                {
                    return false;
                }
            }

            //If object doesnt already exist in hash table then add it
            table[hash][counts[hash]] = obj;
            counts[hash] += 1;
            //Check if chain at hash index is full
            if(counts[hash] == table[hash].length)
            {
                //double the chain length at hash index
            }
        }
        return false;
    }

    @Override
    public boolean contains(Object obj)
    {
        return false;
    }

    @Override
    public boolean remove(Object obj)
    {
        return false;
    }

    public static void main(String[] args)
    {
        ArrayHashTable arrayHashTable = new ArrayHashTable();
        arrayHashTable.add(1);
        arrayHashTable.add(2);
        arrayHashTable.add(3);
        arrayHashTable.add(4);
        arrayHashTable.add(5);
        arrayHashTable.add(6);
        arrayHashTable.add(7);
        arrayHashTable.add(8);
        arrayHashTable.add(9);
        arrayHashTable.add(10);
        arrayHashTable.add(11);
    }
}
