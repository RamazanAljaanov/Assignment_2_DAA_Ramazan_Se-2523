package com.assignment2.structures;

import com.assignment2.metrics.OpCounter;

public class MinHeap {
    private static final int DEFAULT_CAPACITY = 16;

    private int[] heap;
    private int size;
    private OpCounter counter;

    public MinHeap() {
        this(DEFAULT_CAPACITY);
    }

    public MinHeap(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }
        this.heap = new int[Math.max(initialCapacity, 4)];
        this.size = 0;
        this.counter = new OpCounter();
    }

    public void setOpCounter(OpCounter counter) {
        this.counter = (counter != null) ? counter : new OpCounter();
    }

    public OpCounter getOpCounter() {
        return counter;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        size = 0;
    }

    public void insert(int x) {
        ensureCapacity(size + 1);
        counter.addMove();
        heap[size] = x;
        size++;
        bubbleUp(size - 1);
    }

    public int peekMin() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot peekMin from an empty heap");
        }
        counter.addStep();
        return heap[0];
    }

    public int extractMin() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot extractMin from an empty heap");
        }

        counter.addStep();
        int minVal = heap[0];

        counter.addStep();
        counter.addMove();
        heap[0] = heap[size - 1];
        size--;

        if (size > 0) {
            bubbleDown(0);
        }

        return minVal;
    }

    public void bubbleUp(int index) {
        int curr = index;
        while (curr > 0) {
            int parent = (curr - 1) / 2;

            counter.addStep();
            counter.addStep();
            counter.addComparison();

            if (heap[curr] < heap[parent]) {
                swap(curr, parent);
                curr = parent;
            } else {
                break;
            }
        }
    }

    public void bubbleDown(int index) {
        int curr = index;
        while (true) {
            int left = 2 * curr + 1;
            int right = 2 * curr + 2;
            int smallest = curr;

            if (left < size) {
                counter.addStep();
                counter.addStep();
                counter.addComparison();
                if (heap[left] < heap[smallest]) {
                    smallest = left;
                }
            }

            if (right < size) {
                counter.addStep();
                counter.addStep();
                counter.addComparison();
                if (heap[right] < heap[smallest]) {
                    smallest = right;
                }
            }

            if (smallest != curr) {
                swap(curr, smallest);
                curr = smallest;
            } else {
                break;
            }
        }
    }

    public static MinHeap buildHeap(int[] array, OpCounter counter) {
        MinHeap h = new MinHeap(array.length);
        if (counter != null) {
            h.setOpCounter(counter);
        }
        h.size = array.length;

        for (int i = 0; i < array.length; i++) {
            if (counter != null) {
                counter.addStep();
                counter.addMove();
            }
            h.heap[i] = array[i];
        }

        for (int i = (h.size / 2) - 1; i >= 0; i--) {
            h.bubbleDown(i);
        }

        return h;
    }

    private void swap(int i, int j) {
        counter.addStep();
        counter.addStep();
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
        counter.addMoves(2);
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > heap.length) {
            int newCapacity = heap.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            int[] newHeap = new int[newCapacity];
            for (int i = 0; i < size; i++) {
                counter.addStep();
                counter.addMove();
                newHeap[i] = heap[i];
            }
            heap = newHeap;
        }
    }

    public boolean isValidHeap() {
        for (int i = 0; i < size; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (left < size && heap[i] > heap[left]) {
                return false;
            }
            if (right < size && heap[i] > heap[right]) {
                return false;
            }
        }
        return true;
    }

    public int[] getInternalArrayCopy() {
        int[] copy = new int[size];
        System.arraycopy(heap, 0, copy, 0, size);
        return copy;
    }
}
