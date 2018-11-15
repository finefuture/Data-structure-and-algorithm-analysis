package org.gra4j.trochilus;

import java.util.Arrays;

public class MaxHeap<E extends Comparable<? super E>> {

    private static final int DEFAULT_CAPACITY = 10;

    private int currentSize;

    private E[] array;

    public MaxHeap () {
        this(DEFAULT_CAPACITY);
    }

    public MaxHeap (int capacity) {
        array = (E[]) new Comparable[capacity];
    }

    public MaxHeap (E[] items) {
        currentSize = items.length;
        array = (E[]) new Comparable[(currentSize + 2) * 11 / 10];

        int i = 1;
        for (E element : items)
            array[i++] = element;
        buildHeap();
    }

    private void buildHeap() {
        for (int i = currentSize/2; i > 0; i--)
            percolateDown(i);
    }

    public E finMax () {
        return array == null ? null : array[1];
    }

    public void insert (E element) {
        if (currentSize == array.length-1)
            enlargeArray(array.length*2 + 1);

        int hole = ++currentSize;
        for (array[0] = element; element.compareTo(array[hole/2]) > 0; hole /= 2)
            array[hole] = array[hole/2];
        array[hole] = element;
    }

    public E deleteMax () {
        if (isEmpty())
            throw new UnsupportedOperationException("heap is empty");

        E element = finMax();
        array[1] = array[currentSize--];
        percolateDown(1);
        array[currentSize + 1] = element;
        return element;
    }

    private void percolateDown(int hole) {
        int child;
        E element = array[hole];

        for (; hole * 2 <= currentSize; hole = child) {
            child = hole * 2;
            if (child != currentSize && array[child + 1].compareTo(array[child]) > 0)
                child++;
            if (element.compareTo(array[child]) < 0)
                array[hole] = array[child];
            else
                break;
        }
        array[hole] = element;
    }

    public boolean isEmpty () {
        return currentSize == 0;
    }

    public void enlargeArray (int newSize) {
        array = Arrays.copyOf(array, newSize);
    }

    public void printHeap () {

    }


}
