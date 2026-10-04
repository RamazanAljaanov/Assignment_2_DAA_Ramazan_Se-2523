package com.assignment2.metrics;

public class OpCounter {
    private long steps;
    private long moves;
    private long comparisons;

    public OpCounter() {
        this.steps = 0;
        this.moves = 0;
        this.comparisons = 0;
    }

    public void addStep() {
        this.steps++;
    }

    public void addSteps(long count) {
        this.steps += count;
    }

    public void addMove() {
        this.moves++;
    }

    public void addMoves(long count) {
        this.moves += count;
    }

    public void addComparison() {
        this.comparisons++;
    }

    public void addComparisons(long count) {
        this.comparisons += count;
    }

    public long getSteps() {
        return steps;
    }

    public long getMoves() {
        return moves;
    }

    public long getComparisons() {
        return comparisons;
    }

    public void reset() {
        this.steps = 0;
        this.moves = 0;
        this.comparisons = 0;
    }

    public OpCounter copy() {
        OpCounter c = new OpCounter();
        c.steps = this.steps;
        c.moves = this.moves;
        c.comparisons = this.comparisons;
        return c;
    }

    @Override
    public String toString() {
        return "OpCounter{steps=" + steps + ", moves=" + moves + ", comparisons=" + comparisons + "}";
    }
}
