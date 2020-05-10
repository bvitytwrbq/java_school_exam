package com.tsystems.javaschool.tasks.pyramid;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        if(inputNumbers.contains(null)){
            throw new CannotBuildPyramidException();
        }

        int rows = rowsAmount(inputNumbers);

        if(rows == -1){
            throw new CannotBuildPyramidException();
        }

        Collections.sort(inputNumbers);

        int columns = 2*rows-1;

        int [][]pyramid = new int[rows][columns];

        // middle point of the first row for the top
        int startPosition = pyramid[0].length/2;

        Queue<Integer> queue = new LinkedList<>(inputNumbers);

        for (int i = 0; i <pyramid.length ; i++) {
            int start = startPosition;
            // putting an element on a position 1 slot left of the previous start position then
            // putting as many queue heads as the length of the array allows after 1 empty slot
            for (int j = 0; j <= i ; j++) {
                pyramid[i][start]=queue.remove();
                start += 2;
            }
            startPosition--;
        }

        return pyramid;
    }

    /**
     * method to calculate a quadratic equation's root for n^2+n-2 -> (-1+sqrt(1+8n))/2
    */
    public int rowsAmount(List<Integer> inputNumbers){
        double result = (Math.sqrt(1 + 8*inputNumbers.size()) - 1)/2;
        // checking if the result is a proper integer
        if(result == Math.ceil(result)) {
            return (int) result;
        }

        return -1;
    }


}
