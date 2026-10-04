package com.assignment2.structures;

import com.assignment2.metrics.OpCounter;

public interface IntList {
    void add(int x);

    void add(int index, int x);

    int remove(int index);

    int get(int index);

    boolean contains(int x);

    int size();

    boolean isEmpty();

    void clear();

    void setOpCounter(OpCounter counter);

    OpCounter getOpCounter();
}

