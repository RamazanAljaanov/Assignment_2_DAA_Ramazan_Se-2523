package com.assignment2.benchmark;

import com.assignment2.structures.DynamicArray;
import com.assignment2.structures.MinHeap;
import com.assignment2.structures.MyLinkedList;
import org.openjdk.jol.info.GraphLayout;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class MemoryBenchmark {

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("   Bonus Task A: Memory Footprint Analysis via JOL        ");
        System.out.println("==========================================================");

        int[] sizes = {100, 1000, 10000, 50000, 100000};
        File csvFile = new File("results/memory_footprint.csv");
        if (csvFile.getParentFile() != null) {
            csvFile.getParentFile().mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
            writer.println("n,structure,total_bytes,bytes_per_element,total_mb");

            for (int n : sizes) {
                System.out.printf("\n--- Measuring Memory for n = %d ---\n", n);
                Random rng = new Random(42);

                DynamicArray array = new DynamicArray(n);
                for (int i = 0; i < n; i++) {
                    array.add(rng.nextInt());
                }
                long arrayBytes = GraphLayout.parseInstance(array).totalSize();
                double arrayMb = arrayBytes / (1024.0 * 1024.0);
                double arrayPerElem = (double) arrayBytes / n;
                writer.printf("%d,DynamicArray,%d,%.2f,%.4f\n", n, arrayBytes, arrayPerElem, arrayMb);
                System.out.printf("DynamicArray: %10d bytes (%6.2f MB) | ~%.2f bytes/element\n", arrayBytes, arrayMb, arrayPerElem);

                rng = new Random(42);
                MyLinkedList list = new MyLinkedList();
                for (int i = 0; i < n; i++) {
                    list.add(rng.nextInt());
                }
                long listBytes = GraphLayout.parseInstance(list).totalSize();
                double listMb = listBytes / (1024.0 * 1024.0);
                double listPerElem = (double) listBytes / n;
                writer.printf("%d,MyLinkedList,%d,%.2f,%.4f\n", n, listBytes, listPerElem, listMb);
                System.out.printf("MyLinkedList: %10d bytes (%6.2f MB) | ~%.2f bytes/element\n", listBytes, listMb, listPerElem);

                rng = new Random(42);
                MinHeap heap = new MinHeap(n);
                for (int i = 0; i < n; i++) {
                    heap.insert(rng.nextInt());
                }
                long heapBytes = GraphLayout.parseInstance(heap).totalSize();
                double heapMb = heapBytes / (1024.0 * 1024.0);
                double heapPerElem = (double) heapBytes / n;
                writer.printf("%d,MinHeap,%d,%.2f,%.4f\n", n, heapBytes, heapPerElem, heapMb);
                System.out.printf("MinHeap:      %10d bytes (%6.2f MB) | ~%.2f bytes/element\n", heapBytes, heapMb, heapPerElem);
            }

            System.out.println("\n[SUCCESS] Memory footprint data written to results/memory_footprint.csv");
        } catch (IOException e) {
            System.err.println("Error saving memory benchmark: " + e.getMessage());
        }
    }
}
