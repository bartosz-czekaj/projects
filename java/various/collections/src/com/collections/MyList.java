package com.collections;

import java.util.Optional;

class MyList<E> {
    private static int counter;
    private Node<E> head;

    public MyList() {

    }

    public void add(E data) {

        if (head == null) {
            head = new Node(data);
        }

        Node<E> crunchifyTemp = new Node(data);
        Node<E> crunchifyCurrent = head;

        if (crunchifyCurrent != null) {
            while (crunchifyCurrent.getNext() != null) {
                crunchifyCurrent = crunchifyCurrent.getNext();
            }
            crunchifyCurrent.setNext(crunchifyTemp);
        }
        incrementCounter();
    }

    private static int getCounter() {
        return counter;
    }

    private static void incrementCounter() {
        counter++;
    }

    private static void decrementCounter() {
        counter--;
    }

    public void add(E data, int index) {
        Node<E> crunchifyTemp = new Node(data);
        Node<E> crunchifyCurrent = head;

        if (crunchifyCurrent != null) {
            for (int i = 0; i < index && crunchifyCurrent.getNext() != null; i++) {
                crunchifyCurrent = crunchifyCurrent.getNext();
            }
        }
        crunchifyTemp.setNext(crunchifyCurrent.getNext());
        crunchifyCurrent.setNext(crunchifyTemp);

        incrementCounter();
    }

    public Optional<E> get(int index)
    {
        if (index < 0)
            return Optional.empty();
        Node<E> crunchifyCurrent = null;
        if (head != null) {
            crunchifyCurrent = head.getNext();
            for (int i = 0; i < index; i++) {
                if (crunchifyCurrent.getNext() == null)
                    return Optional.empty();

                crunchifyCurrent = crunchifyCurrent.getNext();
            }
            return Optional.of(crunchifyCurrent.getData());
        }

        return crunchifyCurrent == null ?  Optional.empty() : Optional.of(crunchifyCurrent.getData());
    }

    public boolean remove(int index) {
        if (index < 1 || index > size())
            return false;

        Node<E> crunchifyCurrent = head;
        if (head != null) {
            for (int i = 0; i < index; i++) {
                if (crunchifyCurrent.getNext() == null)
                    return false;

                crunchifyCurrent = crunchifyCurrent.getNext();
            }
            crunchifyCurrent.setNext(crunchifyCurrent.getNext().getNext());

            decrementCounter();
            return true;
        }
        return false;
    }
    public int size() {
        return getCounter();
    }

    public String toString() {
        String output = "";

        if (head != null) {
            Node<E> crunchifyCurrent = head.getNext();
            while (crunchifyCurrent != null) {
                output += "[" + crunchifyCurrent.getData().toString() + "]";
                crunchifyCurrent = crunchifyCurrent.getNext();
            }

        }
        return output;
    }

    private class Node<E> {
        Node<E> next;
        E data;

        public Node(E dataValue) {
            next = null;
            data = dataValue;
        }
        @SuppressWarnings("unused")
        public Node(E dataValue, Node nextValue) {
            next = nextValue;
            data = dataValue;
        }

        public E getData() {
            return data;
        }

        @SuppressWarnings("unused")
        public void setData(E dataValue) {
            data = dataValue;
        }

        public Node<E> getNext() {
            return next;
        }

        public void setNext(Node<E> nextValue) {
            next = nextValue;
        }
    }
}