package com.assignment2.structures;
import com.assignment2.metrics.OpCounter;


public class MyLinkedList implements IntList {

    public static class Node {
        public int val;
        public Node next;
        public Node prev;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.prev = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;
    private OpCounter counter;

    public MyLinkedList() {
        this.head = null;
        this.tail = null;
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
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public void add(int x) {
        Node newNode = new Node(x);
        if (size == 0) {
            head = newNode;
            tail = newNode;
            counter.addMoves(2);
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
            counter.addMoves(3);
        }
        size++;
    }

    @Override
    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (index == size) {
            add(x);
            return;
        }

        Node newNode = new Node(x);

        if (index == 0) {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
            counter.addMoves(3);
        } else {
            Node curr = getNode(index);
            Node prevNode = curr.prev;

            newNode.next = curr;
            newNode.prev = prevNode;
            prevNode.next = newNode;
            curr.prev = newNode;
            counter.addMoves(4);
        }
        size++;
    }

    @Override
    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node target;
        if (index == 0) {
            target = head;
            counter.addStep();
            int val = target.val;
            head = head.next;
            counter.addMove();
            if (head != null) {
                head.prev = null;
                counter.addMove();
            } else {
                tail = null;
                counter.addMove();
            }
            size--;
            return val;
        } else if (index == size - 1) {
            target = tail;
            counter.addStep();
            int val = target.val;
            tail = tail.prev;
            counter.addMove();
            if (tail != null) {
                tail.next = null;
                counter.addMove();
            } else {
                head = null;
                counter.addMove();
            }
            size--;
            return val;
        } else {
            target = getNode(index);
            counter.addStep();
            int val = target.val;
            Node prevNode = target.prev;
            Node nextNode = target.next;

            prevNode.next = nextNode;
            nextNode.prev = prevNode;
            counter.addMoves(2);

            size--;
            return val;
        }
    }

    @Override
    public int get(int index) {
        Node node = getNode(index);
        counter.addStep();
        return node.val;
    }

    @Override
    public boolean contains(int x) {
        Node curr = head;
        while (curr != null) {
            counter.addStep();
            counter.addComparison();
            if (curr.val == x) {
                return true;
            }
            curr = curr.next;
            counter.addStep();
        }
        return false;
    }

    private Node getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (index < size / 2) {
            Node curr = head;
            for (int i = 0; i < index; i++) {
                counter.addStep();
                curr = curr.next;
            }
            return curr;
        } else {
            Node curr = tail;
            for (int i = size - 1; i > index; i--) {
                counter.addStep();
                curr = curr.prev;
            }
            return curr;
        }
    }

    public int[] toArray() {
        int[] arr = new int[size];
        Node curr = head;
        int i = 0;
        while (curr != null) {
            arr[i++] = curr.val;
            curr = curr.next;
        }
        return arr;
    }
}
