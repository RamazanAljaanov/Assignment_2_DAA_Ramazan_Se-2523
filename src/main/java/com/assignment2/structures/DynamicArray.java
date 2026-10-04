package com.assignment2.structures;

import com.assignment2.metrics.OpCounter;

public class DynamicArray implements IntList {
    private static final int DEFAULT_CAPACITY = 10;

    private int[] data;
    private int size;
    private OpCounter counter;

    public DynamicArray() {
        this(DEFAULT_CAPACITY);
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        this.data = new int[Math.max(initialCapacity, 1)];
        this.size = 0;
        this.counter = new OpCounter();
    }

    @Override
    public void setOpCounter(OpCounter counter) {
        this.counter = (counter != null) ? counter : new OpCounter();
    }

    @Override
    public OpCounter getOpCounter() {
        return this.counter;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        this.size = 0;
    }

    public int capacity() {
        return data.length;
    }

    @Override
    public void add(int x) {
        ensureCapacity(size + 1);
        counter.addMove();
        data[size] = x;
        size++;
    }

    @Override
    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacity(size + 1);

        for (int i = size; i > index; i--) {
            counter.addStep();
            counter.addMove();
            data[i] = data[i - 1];
        }

        counter.addMove();
        data[index] = x;
        size++;
    }

    @Override
    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        counter.addStep();
        int removedValue = data[index];

        for (int i = index; i < size - 1; i++) {
            counter.addStep();
            counter.addMove();
            data[i] = data[i + 1];
        }

        size--;
        return removedValue;
    }

    @Override
    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        counter.addStep();
        return data[index];
    }

    @Override
    public boolean contains(int x) {
        for (int i = 0; i < size; i++) {
            counter.addStep();
            counter.addComparison();
            if (data[i] == x) {
                return true;
            }
        }
        return false;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > data.length) {
            int newCapacity = data.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            int[] newData = new int[newCapacity];
            for (int i = 0; i < size; i++) {
                counter.addStep();
                counter.addMove();
                newData[i] = data[i];
            }
            data = newData;
        }
    }

    public int[] toArray() {
        int[] arr = new int[size];
        System.arraycopy(data, 0, arr, 0, size);
        return arr;
    }
}