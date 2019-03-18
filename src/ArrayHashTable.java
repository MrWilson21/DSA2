import java.util.HashSet;
import java.util.Random;

public class ArrayHashTable extends HashTable
{
    private Object[][] table;
    private int[] counts;
    private int chainSize;

    private ArrayHashTable(int capacity)
    {
        this.capacity = capacity;
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
            size += 1;
            return true;
        }

        //Else array is already initialised
        //Check if object already exists in hash table at hash index
        for(int i=0; i < counts[hash]; i++)
        {
            if (table[hash][i].equals(obj))
            {
                return false;
            }
        }

        //If object doesnt already exist in hash table then add it
        table[hash][counts[hash]] = obj;
        counts[hash] += 1;
        size += 1;
        //Check if chain at hash index is full
        if(counts[hash] == table[hash].length)
        {
            Object[] tempArray = table[hash];
            Object[] newArray = new Object[table[hash].length * 2];

            for(int i=0; i<tempArray.length; i++)
            {
                newArray[i] = tempArray[i];
            }

            table[hash] = newArray;
        }
        return true;
    }

    @Override
    public boolean contains(Object obj)
    {
        //Get hash index for object
        int hash = obj.hashCode() % capacity;

        if(table[hash] == null)
        {
            return false;
        }

        //Check each element in chain at hash index to see if object exists in hash table
        for(int i=0; i < counts[hash]; i++)
        {
            if (table[hash][i].equals(obj))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean remove(Object obj)
    {
        int hash = obj.hashCode() % capacity;

        //If chain at hash index isn't initialised then return false
        if(table[hash] == null)
        {
            return false;
        }

        int indexToRemove = -1;
        //Check each element in chain at hash index to see if object exists in hash table and get the index of the object to remove
        for(int i=0; i < counts[hash]; i++)
        {
            if (table[hash][i].equals(obj))
            {
                indexToRemove = i;
                break;
            }
        }
        //If object not found then return false
        if(indexToRemove == -1)
        {
            return false;
        }

        //Create new array with the same size as the old array -1 (minimum size of chainSize) and copy over its contents except the object being removed
        int sizeOfNewArray = table[hash].length-1;
        if(sizeOfNewArray < chainSize)
        {
            sizeOfNewArray = chainSize;
        }
        Object[] newArray = new Object[sizeOfNewArray];

        //Copy objects from indexes before the index to remove
        for(int i=0; i<indexToRemove; i++)
        {
            newArray[i] = table[hash][i];
        }

        //Copy objects from indexes after the index to remove
        for(int i=indexToRemove+1; i<counts[hash]; i++)
        {
            newArray[i-1] = table[hash][i];
        }

        //Replace old array in table with new one
        table[hash] = newArray;
        counts[hash] -= 1;
        size -= 1;
        return true;
    }

    //toString method to help with debugging
    @Override
    public String toString()
    {
        StringBuffer returnString = new StringBuffer();
        for(int i=0; i<table.length; i++)
        {
            returnString.append("[" + i + "] ");
            if(table[i] != null)
            {
                returnString.append("[");
                for(int j=0; j<table[i].length; j++)
                {
                    returnString.append(table[i][j]);
                    if(j < table[i].length-1)
                    {
                        returnString.append(", ");
                    }
                }
                returnString.append("]");
            }
            else
            {
                returnString.append("null");
            }

            if(i < table.length-1)
            {
                returnString.append("\n");
            }
        }

        return returnString.toString();
    }

    public static long[] testAlgorithm(int sizeOfInput, int numberOfRepetitions, int rangeOfInputs)
    {
        //3 longs are needed to record time taken to add and time taken to remove items from hash table and total time taken
        long[] timingResults = new long[4];
        long startTime;

        //Repeat timing experiment numberOfRepetitions times for each size of input
        for(int i=0; i<numberOfRepetitions; i++)
        {
            //Generate random int array
            int[] intArray = generateIntArray(sizeOfInput, 1, 1 + rangeOfInputs);

            //Start timer after initialing an ArrayHashTable
            ArrayHashTable arrayHashTable = new ArrayHashTable(10);
            HashSet hashSet = new HashSet();
            startTime = System.nanoTime();
            //Add all numbers from intArray to the ArrayHashTable
            for(int number: intArray)
            {
                arrayHashTable.add(number);
            }
            //Add time taken to timing results
            timingResults[0] += System.nanoTime() - startTime;

            //Remove all numbers from intArray to the ArrayHashTable after restarting timer
            startTime = System.nanoTime();
            for(int number: intArray)
            {
                arrayHashTable.remove(number);
            }
            //Add time taken to timing results
            timingResults[1] += System.nanoTime() - startTime;

            //Add all numbers from intArray to the HashSet
            startTime = System.nanoTime();
            for(int number: intArray)
            {
                hashSet.add(number);
            }
            //Remove all numbers from intArray to the HashSet
            for(int number: intArray)
            {
                hashSet.remove(number);
            }
            //Add time taken to timing results
            timingResults[3] += System.nanoTime() - startTime;

        }
        //Get average times after repeating test
        timingResults[0] = timingResults[0] / numberOfRepetitions;
        timingResults[1] = timingResults[1] / numberOfRepetitions;
        timingResults[3] = timingResults[3] / numberOfRepetitions;
        //Get total time taken
        timingResults[2] = timingResults[0] + timingResults[1];

        return timingResults;
    }

    //Generate array of random ints to test ArrayHashTable
    //Ints are in range of min to max
    public static int[] generateIntArray(int length, int min, int max)
    {
        int[] intArray = new int[length];
        Random r = new Random();

        for(int i=0; i < length; i++)
        {
            intArray[i] = r.nextInt((max - min) + 1) + min;
        }

        return intArray;
    }

    public static void main(String[] args)
    {
        int numberOfTests = 50; //Number of different tests, each test will have (test Number * sizeOfInputs) sized inputs.
        //Each test generates one data point
        int sizeOfInputs = 1000; //Number of elements in each array
        int rangeOfInputs = Integer.MAX_VALUE-1; //Amount of different possible numbers being used when generating arrays to test with
        int numberOfRepetitions = 25; //Number of times a test will be repeated. Repeated with different inputs of the same size of that test

        //"Warm up" code, can help get more consistent results
        for(int i=0; i<3; i++)
        {
            testAlgorithm(sizeOfInputs * (i+1), numberOfRepetitions, rangeOfInputs);
        }

        long[][] resultsList = new long[numberOfTests][]; //List of timings for each test
        for(int i=0; i<numberOfTests; i++)
        {
            System.out.println("Current test number: " + (i + 1) + " of " + (numberOfTests) + " with size of input: " + sizeOfInputs * (i+1));
            resultsList[i] = testAlgorithm(sizeOfInputs * (i+1), numberOfRepetitions, rangeOfInputs);
        }

        //Print out results of tests
        System.out.println("\nResults:\n");
        System.out.println("Size\tAdding to list\tRemoving from list\tTotal time taken\thashSet total time");
        for(int i=0; i < numberOfTests; i++)
        {
            System.out.print(sizeOfInputs * (i+1) + "\t");
            System.out.print((resultsList[i][0]) + "\t");
            System.out.print((resultsList[i][1]) + "\t");
            System.out.print((resultsList[i][2]) + "\t");
            System.out.print((resultsList[i][3]));
            System.out.println();
        }
    }
}
