# DAA Assignment 2: In-Memory Workload Engine

Custom, zero-dependency implementations of `DynamicArray`, `MyLinkedList`, and `MinHeap` in Java 17 for the Design and Analysis of Algorithms course.

## Features
- **Zero standard collections**: Pure primitive `int` storage without `java.util.ArrayList`, `LinkedList`, or `PriorityQueue`.
- **Honest physical metrics**: Direct tracking of `steps`, `moves`, and `comparisons` inside operations.
- **JUnit 5 test suite**: 28 exhaustive unit tests covering edge cases, differential testing against JDK classes, and heap property invariants.
- **Benchmarking engine**: Evaluates 4 workloads on sizes $n \in \{100, 1\,000, 10\,000, 100\,000\}$ with JVM warm-up and median of 5 runs.
- **Bonus Tasks**:
    - **Task A (+10%)**: Deep heap memory profiling via OpenJDK JOL (Java Object Layout).
    - **Task B (+5%)**: Floyd's bottom-up $O(n)$ `buildHeap` algorithm.

---

## Prerequisites
- **Java**: OpenJDK 25 
- **Maven**: 3.8+
- **Python 3**: (optional, for regenerating plots) with `matplotlib` and `pandas`

---

## Quick Start (Build & Test)

### 1. Compile the project
```bash
mvn clean compile
```

### 2. Run all JUnit 5 tests
```bash
mvn test
```
All 28 tests will execute, verifying empty structures, boundary insertions, heap property preservation, and differential correctness.

### 3. Run Workload Benchmarks (W1, W2, W3, W4)
```bash
mvn exec:java -Dexec.mainClass="com.assignment2.benchmark.BenchmarkRunner"
```
This runs warm-up cycles, measures median execution times across all 4 workloads, and saves the output to `results/results.csv`.

### 4. Run Bonus Task A: JOL Memory Footprint Benchmark
```bash
mvn exec:java -Dexec.mainClass="com.assignment2.benchmark.MemoryBenchmark"
```
This inspects deep heap byte usage for each structure and saves the output to `results/memory_footprint.csv`.

### 5. Generate PNG Plots
```bash
python3 scripts/generate_plots.py
```
Outputs high-resolution charts to `results/plots/`.

---

## Project Structure
```text
.
├── pom.xml                                  # Maven project descriptor
├── README.md                                # Instructions for build and test
├── REPORT.md                                # Full theoretical report, proofs, and discussion
├── src/
│   ├── main/java/com/assignment2/
│   │   ├── metrics/OpCounter.java           # Operation counting engine
│   │   ├── structures/
│   │   │   ├── IntList.java                 # Common interface for list structures
│   │   │   ├── DynamicArray.java            # Contiguous resizable array (int[])
│   │   │   ├── MyLinkedList.java            # Doubly-linked list (Node: val, next, prev)
│   │   │   └── MinHeap.java                 # Complete binary min-heap + Floyd's buildHeap
│   │   └── benchmark/
│   │       ├── BenchmarkRunner.java         # Workload benchmark runner
│   │       └── MemoryBenchmark.java         # JOL memory analyzer
│   └── test/java/com/assignment2/
│       ├── DynamicArrayTest.java            # Unit tests for DynamicArray
│       ├── MyLinkedListTest.java            # Unit tests for MyLinkedList
│       └── MinHeapTest.java                 # Unit tests for MinHeap
├── results/
│   ├── results.csv                          # Benchmark metric outputs
│   ├── memory_footprint.csv                 # JOL byte measurements
│   └── plots/                               # Generated visual charts
└── scripts/
    └── generate_plots.py                    # Plot generation script
```

---

## Git Workflow & Submission

To create the required feature branches and the v1.0 release tag:
```bash
# Tag the release
git tag v1.0

# Create feature branches reflecting the development history
git branch feature/array
git branch feature/list
git branch feature/heap
git branch feature/metrics
```

To create the submission archive:
```bash
zip -r DAA_Assignment2_name_surname_group.zip src pom.xml README.md REPORT.md results
```
