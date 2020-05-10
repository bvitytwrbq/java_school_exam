package com.tsystems.javaschool.tasks.subsequence;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Subsequence {

    /**
     * Checks if it is possible to get a sequence which is equal to the first
     * one by removing some elements from the second one.
     *
     * @param x first sequence
     * @param y second sequence
     * @return <code>true</code> if possible, otherwise <code>false</code>
     */
    @SuppressWarnings("rawtypes")
    public boolean find(List x, List y) {

        if( x == null || y == null ) throw new IllegalArgumentException();

        if(x.isEmpty()) return true;

        if(x.size() > y.size()) return false;

        // using queues because we're gonna use the heads only
        Queue yQueue = new LinkedList<>(y);
        Queue xQueue = new LinkedList<>(x);

        Object xElement;
        Object yElement;

        xElement = xQueue.remove();

        // comparing elements from teh second queue to the stored element from the first queue till the matching one
        // occurs or the second queue runs out of the elements and in case elements match replacing the stored one from
        // the first queue by the next one after it
        while(!yQueue.isEmpty())
        {
            yElement = yQueue.remove();
            if(yElement.equals(xElement))
            {
                if(xQueue.isEmpty()) return true;
                xElement = xQueue.remove();
            }
        }
        return false;
    }
}
