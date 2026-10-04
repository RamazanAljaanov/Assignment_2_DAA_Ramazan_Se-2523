package com.assignment2;

import com.assignment2.metrics.OpCounter;
import com.assignment2.structures.DynamicArray;
import com.assignment2.structures.MinHeap;
import com.assignment2.structures.MyLinkedList;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        System.out.println(">>> 1. DEMO: DynamicArray (int[], 2x growth strategy)");
        OpCounter arrayCounter = new OpCounter();
        DynamicArray array = new DynamicArray(4);
        array.setOpCounter(arrayCounter);

        System.out.printf("Initial: size = %d, capacity = %d\n", array.size(), array.capacity());

        for (int val : new int[]{10, 20, 30, 40}) {
            array.add(val);
        }
        System.out.printf("After adding 4 elements: %s (size = %d, capacity = %d)\n",
                Arrays.toString(array.toArray()), array.size(), array.capacity());

        System.out.println("Adding 50 (should trigger 2x capacity resize from 4 to 8)...");
        array.add(50);
        System.out.printf("After resize: %s (size = %d, capacity = %d)\n",
                Arrays.toString(array.toArray()), array.size(), array.capacity());

        System.out.println("Inserting 15 at index 1...");
        array.add(1, 15);
        System.out.printf("Array content: %s\n", Arrays.toString(array.toArray()));

        System.out.printf("get(2) = %d (O(1) random access in 1 physical step)\n", array.get(2));
        System.out.printf("contains(30) = %b, contains(99) = %b\n", array.contains(30), array.contains(99));

        int removedFromArray = array.remove(1);
        System.out.printf("remove(index 1) returned: %d. Array now: %s\n",
                removedFromArray, Arrays.toString(array.toArray()));
        System.out.printf("DynamicArray metrics: %s\n\n", arrayCounter);

        System.out.println("----------------------------------------------------------------------");
        System.out.println(">>> 2. DEMO: MyLinkedList (Doubly-linked nodes: val, next, prev)");
        OpCounter listCounter = new OpCounter();
        MyLinkedList list = new MyLinkedList();
        list.setOpCounter(listCounter);

        list.add(100);
        list.add(200);
        list.add(300);
        System.out.printf("List content: %s (size = %d)\n", Arrays.toString(list.toArray()), list.size());

        System.out.println("Inserting 50 at head (index 0)...");
        list.add(0, 50);
        System.out.printf("After head insert: %s (O(1) pointer updates)\n", Arrays.toString(list.toArray()));

        System.out.printf("get(2) = %d (bidirectional search)\n", list.get(2));
        System.out.printf("contains(200) = %b, contains(777) = %b\n", list.contains(200), list.contains(777));

        int removedFromList = list.remove(0);
        System.out.printf("remove(index 0) returned: %d. List now: %s\n",
                removedFromList, Arrays.toString(list.toArray()));
        System.out.printf("MyLinkedList metrics: %s\n\n", listCounter);

        System.out.println("----------------------------------------------------------------------");
        System.out.println(">>> 3. DEMO: MinHeap (Complete binary min-heap)");
        OpCounter heapCounter = new OpCounter();
        MinHeap heap = new MinHeap();
        heap.setOpCounter(heapCounter);

        int[] valuesToInsert = {45, 12, 89, 3, 27, 60, 15};
        System.out.printf("Inserting values: %s\n", Arrays.toString(valuesToInsert));
        for (int v : valuesToInsert) {
            heap.insert(v);
        }

        System.out.printf("Internal array representation: %s\n", Arrays.toString(heap.getInternalArrayCopy()));
        System.out.printf("peekMin() = %d (O(1) root inspection)\n", heap.peekMin());
        System.out.printf("Heap invariant a[parent] <= a[child] valid: %b\n", heap.isValidHeap());

        System.out.print("Extracting all minimum elements in order: [");
        while (!heap.isEmpty()) {
            System.out.print(heap.extractMin() + (heap.isEmpty() ? "" : ", "));
        }
        System.out.println("] (strictly non-decreasing sorted order)");
        System.out.printf("MinHeap metrics: %s\n\n", heapCounter);

        System.out.println("----------------------------------------------------------------------");
        System.out.println(">>> 4. BONUS DEMO: Floyd's O(n) buildHeap");
        int[] rawArray = {90, 80, 70, 60, 50, 40, 30, 20, 10};
        System.out.printf("Unordered input: %s\n", Arrays.toString(rawArray));
        MinHeap floydHeap = MinHeap.buildHeap(rawArray, null);
        System.out.printf("Heapified array: %s (Valid heap: %b)\n",
                Arrays.toString(floydHeap.getInternalArrayCopy()), floydHeap.isValidHeap());

        System.out.println("\n======================================================================");
        System.out.println("  All structures demonstrated successfully!                           ");
        System.out.println("  To run the full 4-workload benchmark:                               ");
        System.out.println("  mvn compile exec:java -Dexec.mainClass=\"com.assignment2.benchmark.BenchmarkRunner\"");
        System.out.println("======================================================================");
    }
}

