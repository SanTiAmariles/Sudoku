package model.list;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<T> implements IList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    @Override
    public void addFirst(T elem) {
        Node<T> newNode = new Node<>(elem);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        size++;
    }

    @Override
    public void addLast(T elem) {
        Node<T> newNode = new Node<>(elem);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    @Override
    public T getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return head.data;
    }

    @Override
    public T getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return tail.data;
    }

    @Override
    public void removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        head = head.next;
        size--;
        if (head == null) {
            tail = null; // If head becomes null, tail should also be null
        }
    }

    @Override
    public void removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        if (size == 1) {
            head = null;
            tail = null;
            size--;
            return;
        }
        Node<T> current = head;
        while (current.next != tail) {
            current = current.next;
        }
        current.next = null;
        tail = current;
        size--;
    }

    @Override
    public Boolean contains(T elem) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(elem)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public void removeElement(T elem) {
        Node<T> current = head;
        Node<T> prev = null;
        while (current != null) {
            if (current.data.equals(elem)) {
                if (prev == null) {
                    head = current.next;
                } else {
                    prev.next = current.next;
                    if (current.next == null) {
                        tail = prev;
                    }
                }
                size--;
                return;
            }
            prev = current;
            current = current.next;
        }
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public T get(Integer index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    @Override
    public void set(Integer index, T elem) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        current.data = elem;
    }

    @Override
    public void add(Integer index, T elem) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        if (index == 0) {
            addFirst(elem);
        } else if (index == size) {
            addLast(elem);
        } else {
            Node<T> newNode = new Node<>(elem);
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
            size++;
        }
    }

    @Override
    public void removeElementByIndex(Integer index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        if (index == 0) {
            removeFirst();
        } else if (index == size - 1) {
            removeLast();
        } else {
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            current.next = current.next.next;
            size--;
        }
    }

    @Override
    public Integer indexOf(T elem) {
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            if (current.data.equals(elem)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    @Override
    public Integer lastIndexOf(T elem) {
        Node<T> current = head;
        int lastIndex = -1;
        int index = 0;
        while (current != null) {
            if (current.data.equals(elem)) {
                lastIndex = index;
            }
            current = current.next;
            index++;
        }
        return lastIndex;
    }

    @Override
    public Boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Integer size() {
        return size;
    }

    @Override
    public IList<T> deepCopy() {
        LinkedList<T> copy = new LinkedList<>();
        Node<T> current = head;
        while (current != null) {
            copy.addLast(current.data);
            current = current.next;
        }
        return copy;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }
}
