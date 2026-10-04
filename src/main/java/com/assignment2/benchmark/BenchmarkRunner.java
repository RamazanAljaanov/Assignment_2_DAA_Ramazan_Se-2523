package com.assignment2.benchmark;

import com.assignment2.metrics.OpCounter;
import com.assignment2.structures.DynamicArray;
import com.assignment2.structures.IntList;
import com.assignment2.structures.MinHeap;
import com.assignment2.structures.MyLinkedList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BenchmarkRunner {

    public static class ResultRow {
        public String workload;
        public String variant;
        public String structure;
        public int n;
        public double timeMs;
        public long steps;
        public long moves;
        public long comparisons;

        public ResultRow(String workload, String variant, String structure, int n,
                         double timeMs, long steps, long moves, long comparisons) {
            this.workload = workload;
            this.variant = variant;
            this.structure = structure;
            this.n = n;
            this.timeMs = timeMs;
            this.steps = steps;
            this.moves = moves;
            this.comparisons = comparisons;
        }

        public String toCsv() {
            return String.format("%s,%s,%s,%d,%.4f,%d,%d,%d",
                    workload, variant, structure, n, timeMs, steps, moves, comparisons);
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("  In-Memory Workload Engine Benchmark (DAA Assignment 2)   ");
        System.out.println("==========================================================");

        int[] sizes = {100, 1000, 10000, 100000};
        List<ResultRow> results = new ArrayList<>();

        System.out.println("Starting JVM Warm-up...");
        performWarmUp();
        System.out.println("Warm-up complete. Running benchmark suites...\n");

        for (int n : sizes) {
            System.out.printf("--- Running benchmarks for n = %d ---\n", n);

            results.add(benchmarkW1("DynamicArray", n));
            results.add(benchmarkW1("MyLinkedList", n));

            results.add(benchmarkW2("DynamicArray", n));
            results.add(benchmarkW2("MyLinkedList", n));

            results.add(benchmarkW3("DynamicArray", n, "head"));
            results.add(benchmarkW3("MyLinkedList", n, "head"));

            results.add(benchmarkW3("DynamicArray", n, "middle"));
            results.add(benchmarkW3("MyLinkedList", n, "middle"));

            results.add(benchmarkW4(n));
        }

        saveToCsv(results, "results/results.csv");

        System.out.println("\nRunning Bonus Task B (Floyd buildHeap vs Naive inserts)...");
        runBonusHeapBenchmark(sizes);

        System.out.println("\nAll benchmarks successfully completed. Results written to results/results.csv");
    }

    private static void performWarmUp() {
        for (int i = 0; i < 3; i++) {
            benchmarkW1("DynamicArray", 1000);
            benchmarkW1("MyLinkedList", 1000);
            benchmarkW2("DynamicArray", 1000);
            benchmarkW2("MyLinkedList", 1000);
            benchmarkW3("DynamicArray", 1000, "head");
            benchmarkW3("MyLinkedList", 1000, "head");
            benchmarkW4(1000);
        }
    }

    private static IntList createList(String structureType, int capacity) {
        if ("DynamicArray".equalsIgnoreCase(structureType)) {
            return new DynamicArray(capacity);
        } else {
            return new MyLinkedList();
        }
    }

    private static ResultRow benchmarkW1(String structureType, int n) {
        int queries = 10000;
        List<Double> times = new ArrayList<>();
        OpCounter recordedCounter = null;

        for (int run = 0; run < 5; run++) {
            Random rng = new Random(42);
            IntList list = createList(structureType, n);
            for (int i = 0; i < n; i++) {
                list.add(rng.nextInt());
            }

            int[] indices = new int[queries];
            for (int i = 0; i < queries; i++) {
                indices[i] = rng.nextInt(n);
            }

            OpCounter counter = new OpCounter();
            list.setOpCounter(counter);

            long start = System.nanoTime();
            int dummy = 0;
            for (int idx : indices) {
                dummy ^= list.get(idx);
            }
            long end = System.nanoTime();
            consumeDummy(dummy);

            times.add((end - start) / 1_000_000.0);
            recordedCounter = counter;
        }

        Collections.sort(times);
        double medianTime = times.get(2);
        System.out.printf("  W1 [Random Access]  %-13s n=%-6d -> Time: %9.3f ms, Steps: %-10d, Moves: %-5d\n",
                structureType, n, medianTime, recordedCounter.getSteps(), recordedCounter.getMoves());

        return new ResultRow("W1", "-", structureType, n, medianTime,
                recordedCounter.getSteps(), recordedCounter.getMoves(), recordedCounter.getComparisons());
    }

    private static ResultRow benchmarkW2(String structureType, int n) {
        int queries = 1000;
        List<Double> times = new ArrayList<>();
        OpCounter recordedCounter = null;

        for (int run = 0; run < 5; run++) {
            Random rng = new Random(42);
            IntList list = createList(structureType, n);
            int[] presentPool = new int[n];
            for (int i = 0; i < n; i++) {
                int val = rng.nextInt(1_000_000);
                presentPool[i] = val;
                list.add(val);
            }

            int[] queryVals = new int[queries];
            for (int i = 0; i < queries; i++) {
                if (i % 2 == 0) {
                    queryVals[i] = presentPool[rng.nextInt(n)];
                } else {
                    queryVals[i] = 2_000_000 + rng.nextInt(1_000_000);
                }
            }

            OpCounter counter = new OpCounter();
            list.setOpCounter(counter);

            long start = System.nanoTime();
            int dummy = 0;
            for (int q : queryVals) {
                if (list.contains(q)) {
                    dummy++;
                }
            }
            long end = System.nanoTime();
            consumeDummy(dummy);

            times.add((end - start) / 1_000_000.0);
            recordedCounter = counter;
        }

        Collections.sort(times);
        double medianTime = times.get(2);
        System.out.printf("  W2 [Search]         %-13s n=%-6d -> Time: %9.3f ms, Steps: %-10d, Comparisons: %-10d\n",
                structureType, n, medianTime, recordedCounter.getSteps(), recordedCounter.getComparisons());

        return new ResultRow("W2", "-", structureType, n, medianTime,
                recordedCounter.getSteps(), recordedCounter.getMoves(), recordedCounter.getComparisons());
    }

    private static ResultRow benchmarkW3(String structureType, int n, String variant) {
        int ops = 1000;
        List<Double> times = new ArrayList<>();
        OpCounter recordedCounter = null;

        for (int run = 0; run < 5; run++) {
            Random rng = new Random(42);
            IntList list = createList(structureType, n + ops);
            for (int i = 0; i < n; i++) {
                list.add(rng.nextInt());
            }

            int[] toInsert = new int[ops];
            for (int i = 0; i < ops; i++) {
                toInsert[i] = rng.nextInt();
            }

            OpCounter counter = new OpCounter();
            list.setOpCounter(counter);

            long start = System.nanoTime();
            if ("head".equalsIgnoreCase(variant)) {
                for (int i = 0; i < ops; i++) {
                    list.add(0, toInsert[i]);
                }
                for (int i = 0; i < ops; i++) {
                    list.remove(0);
                }
            } else {
                for (int i = 0; i < ops; i++) {
                    list.add(list.size() / 2, toInsert[i]);
                }
                for (int i = 0; i < ops; i++) {
                    list.remove(list.size() / 2);
                }
            }
            long end = System.nanoTime();

            times.add((end - start) / 1_000_000.0);
            recordedCounter = counter;
        }

        Collections.sort(times);
        double medianTime = times.get(2);
        System.out.printf("  W3 [%-6s]       %-13s n=%-6d -> Time: %9.3f ms, Steps: %-10d, Moves: %-10d\n",
                variant, structureType, n, medianTime, recordedCounter.getSteps(), recordedCounter.getMoves());

        return new ResultRow("W3", variant, structureType, n, medianTime,
                recordedCounter.getSteps(), recordedCounter.getMoves(), recordedCounter.getComparisons());
    }

    private static ResultRow benchmarkW4(int n) {
        List<Double> times = new ArrayList<>();
        OpCounter recordedCounter = null;

        for (int run = 0; run < 5; run++) {
            Random rng = new Random(42);
            int[] data = new int[n];
            for (int i = 0; i < n; i++) {
                data[i] = rng.nextInt();
            }

            MinHeap heap = new MinHeap(n);
            OpCounter counter = new OpCounter();
            heap.setOpCounter(counter);

            long start = System.nanoTime();
            for (int i = 0; i < n; i++) {
                heap.insert(data[i]);
            }

            int prev = Integer.MIN_VALUE;
            for (int i = 0; i < n; i++) {
                int min = heap.extractMin();
                if (min < prev) {
                    throw new IllegalStateException("Heap violated sorted order at step " + i);
                }
                prev = min;
            }
            long end = System.nanoTime();

            times.add((end - start) / 1_000_000.0);
            recordedCounter = counter;
        }

        Collections.sort(times);
        double medianTime = times.get(2);
        System.out.printf("  W4 [Priority]       %-13s n=%-6d -> Time: %9.3f ms, Steps: %-10d, Moves: %-10d, Comps: %-10d\n",
                "MinHeap", n, medianTime, recordedCounter.getSteps(), recordedCounter.getMoves(), recordedCounter.getComparisons());

        return new ResultRow("W4", "-", "MinHeap", n, medianTime,
                recordedCounter.getSteps(), recordedCounter.getMoves(), recordedCounter.getComparisons());
    }

    private static void runBonusHeapBenchmark(int[] sizes) {
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s | %-12s | %-12s | %-12s | %-12s\n", "n", "Naive Time", "Floyd Time", "Naive Comps", "Floyd Comps");
        System.out.println("-------------------------------------------------------------------------------------------------");

        for (int n : sizes) {
            Random rng = new Random(42);
            int[] data = new int[n];
            for (int i = 0; i < n; i++) {
                data[i] = rng.nextInt();
            }

            OpCounter naiveCounter = new OpCounter();
            MinHeap naiveHeap = new MinHeap(n);
            naiveHeap.setOpCounter(naiveCounter);
            long start1 = System.nanoTime();
            for (int val : data) {
                naiveHeap.insert(val);
            }
            long end1 = System.nanoTime();
            double naiveMs = (end1 - start1) / 1_000_000.0;

            OpCounter floydCounter = new OpCounter();
            long start2 = System.nanoTime();
            MinHeap.buildHeap(data, floydCounter);
            long end2 = System.nanoTime();
            double floydMs = (end2 - start2) / 1_000_000.0;

            System.out.printf("%-10d | %9.3f ms | %9.3f ms | %-12d | %-12d\n",
                    n, naiveMs, floydMs, naiveCounter.getComparisons(), floydCounter.getComparisons());
        }
        System.out.println("-------------------------------------------------------------------------------------------------");
    }

    private static void saveToCsv(List<ResultRow> rows, String filePath) {
        File file = new File(filePath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");
            for (ResultRow row : rows) {
                writer.println(row.toCsv());
            }
            System.out.println("\n[SUCCESS] Successfully saved " + rows.size() + " benchmark rows to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving results to CSV: " + e.getMessage());
        }
    }

    private static volatile int dummySink;
    private static void consumeDummy(int val) {
        dummySink = val;
    }
}