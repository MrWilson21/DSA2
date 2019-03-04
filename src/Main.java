import java.util.*;

public class Main
{
    public static int numberOfTests = 10;
    public static int sizeOfInputs = 100;
    public static int rangeOfInputs = 1;
    public static int numberOfRepetitions = 10;

    public static void main(String[] args)
    {
        long[][] resultsList = new long[numberOfTests][];
        for(int i=0; i<numberOfTests; i++)
        {
            resultsList[i] = testEachAlgorithm(sizeOfInputs * (i+1), numberOfRepetitions, rangeOfInputs);
        }

        System.out.println("Size\tSVD1\tSVD2\tSVD3");
        for(int i=0; i < numberOfTests; i++)
        {
            System.out.print(sizeOfInputs * (i+1) + "\t");
            for(int j=0; j < 3; j++)
            {
                System.out.print(resultsList[i][j] + "\t");
            }
            System.out.println();
        }
    }

    //Tests each SVD algorithm and times them, timing results are returned in a long[]
    //Each algorithm is given the same randomly generated input of a specified time with a specified range of values
    //A larger range gives less positive results (An SVD is found less often)
    //Algorithm is repeated numberOFRepetitions times to help remove random differences in time taken
    public static long[] testEachAlgorithm(int sizeOfInput, int numberOfRepetitions, int rangeOfInputs)
    {
        long[] timingResults = new long[3];
        long startTime;

        for(int i=0; i<numberOfRepetitions; i++)
        {
            int[] intArray = generateIntArray(sizeOfInput, 1, 1 + rangeOfInputs);

            System.out.println("Testing with array:");
            for(int j=0; j < sizeOfInput; j++)
            {
                System.out.print(intArray[j] + " ");
            }
            System.out.println();

            startTime = System.nanoTime();
            SVD1(intArray);
            timingResults[0] += System.nanoTime() - startTime;

            startTime = System.nanoTime();
            SVD3(intArray);
            timingResults[2] += System.nanoTime() - startTime;

            //SVD2 done last as it sorts the input array which could affect the other algorithms
            //Other algorithms have no effect on the input array
            startTime = System.nanoTime();
            SVD2(intArray);
            timingResults[1] += System.nanoTime() - startTime;
        }
        //Get average time after repeating tests
        timingResults[0] = timingResults[0] / numberOfRepetitions;
        timingResults[1] = timingResults[1] / numberOfRepetitions;
        timingResults[2] = timingResults[2] / numberOfRepetitions;

        return timingResults;
    }

    //Generate array of random ints to test SVD algorithms
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

    //First implementation for finding SDV of an array of ints
    //Has O(n^2) time complexity
    //For each number in the array it counts the number of times the number shows up
    //Returns the number if it shows up more than n/2 times
    //Throws up an exception if no SVD exists
    public static void SVD1(int[] numbers)
    {
        int minCountToDominate = numbers.length / 2 + 1;
        for(int numberToCheck: numbers)
        {
            int count = 0;
            for(int numberToCount: numbers)
            {
                if(numberToCheck == numberToCount)
                {
                    count++;
                    if(count >= minCountToDominate)
                    {
                        System.out.println(numberToCheck);
                        return;
                    }
                }
            }
        }
        System.out.println("SVD not found");
    }

    //Second implementation for finding SDV of an array of ints
    //Has O(nlog(n)) time complexity
    //Sort array first, Arrays.sort() method has a nlog(n) time complexity
    //For each index check if number at index is equal to number at index + 1
    //If numbers are equal add 1 to count or set count to 1 if not
    //If count goes above length/2 then return the number at the current index
    //If no number is found return -1
    public static int SVD2(int[] numbers)
    {
        Arrays.sort(numbers);
        int lengthOverTwo = numbers.length / 2;
        int count = 1;
        for(int i=0; i < numbers.length - 1; i++)
        {
            if(numbers[i] == numbers[i + 1])
            {
                count += 1;
            }
            else
            {
                count = 0;
            }
            if(count > lengthOverTwo)
            {
                return numbers[i];
            }
        }

        return -1;
    }

    //Third implementation for finding SDV of an array of ints
    //Has o(n) time complexity
    //First finds a candidate number by looping through array once
    //Start by setting major candidate to first index of number and a count variable to 1
    //For each number if it is equal to the major number then increment count by 1, decrement by 1 if it is not equal
    //If count reaches 0 then set major number to the current number and set count to 1
    //Iterate through each number again after finding major candidate to count the number of times it appears in the list
    //Return major candidate if its count is more than length/2 of array
    public static void SVD3(int[] numbers)
    {
        int count = 1;
        int majorNumber = numbers[0];

        for(int i = 1; i < numbers.length; i++)
        {
            if(majorNumber == numbers[i])
            {
                count++;
            }
            else
            {
                count--;
            }
            if(count == 0)
            {
                majorNumber = numbers[i];
                count = 1;
            }
        }

        count = 0;
        for(int number: numbers)
        {
            if(number == majorNumber)
            {
                count++;
            }
        }

        if(count > numbers.length / 2)
        {
            System.out.println(majorNumber);
            return;
        }
        else
        {
            System.out.println("SVD not found");
            return;
        }
    }
}
