package ua.edu.ucu.stream;

import java.util.*;

public class StreamIterator implements Iterator<Integer> {
    List<Integer> stream;
    int next;

    StreamIterator() {
        stream = new ArrayList<>();
    }

    StreamIterator(Integer[] input) {
        this();
        Collections.addAll(stream, input);
    }

    @Override
    public boolean hasNext() {
        return (next != stream.size() && stream.get(next) != null);
    }

    @Override
    public Integer next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return stream.get(next++);
    }

    public void streamNext() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
    }
}
