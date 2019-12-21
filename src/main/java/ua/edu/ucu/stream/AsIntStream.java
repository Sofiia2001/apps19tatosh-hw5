package ua.edu.ucu.stream;

import ua.edu.ucu.function.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class AsIntStream implements IntStream {
    private StreamIterator it;

    private AsIntStream() {
    }

    private AsIntStream(StreamIterator iterator) {
        this();
        it = iterator;
    }

    public static IntStream of(int... values) {
        Integer[] intVal = new Integer[values.length];
        for (int i = 0; i < values.length; i++) {
            intVal[i] = values[i];
        }
        StreamIterator iterator = new StreamIterator(intVal);
        return new AsIntStream(iterator);
    }

    private void checkIfEmpty() {
        if (!it.hasNext()) {
            throw new IllegalArgumentException();
        }
    }

    public Integer[] arr() {
        Integer[] arr = new Integer[0];
        int i = 0;
        while (it.hasNext()) {
            arr = Arrays.copyOf(arr, arr.length + 1);
            arr[i] = it.next();
            i++;
        }
        if (arr.length == 0) {
            throw new NoSuchElementException();
        }
        return arr;
    }

    @Override
    public Double average() {
        checkIfEmpty();
        Double sum = 0.0;
        int length = 0;
        while (it.hasNext()) {
            sum += it.next();
            length += 1;
        }
        return sum / length;
    }

    @Override
    public Integer max() {
        checkIfEmpty();
        Integer max = it.next();
        Integer comp;
        while (it.hasNext()) {
            comp = it.next();
            if (max < comp) {
                max = comp;
            }
        }
        return max;
    }

    @Override
    public Integer min() {
        checkIfEmpty();
        Integer min = it.next();
        Integer comp;
        while (it.hasNext()) {
            comp = it.next();
            if (min > comp) {
                min = comp;
            }
        }
        return min;
    }

    @Override
    public long count() {
        long counter = 0;
        while (it.hasNext()) {
            it.next();
            counter++;
        }
        return counter;
    }

    @Override
    public Integer sum() {
        checkIfEmpty();
        Integer sum = 0;
        while (it.hasNext()) {
            sum += it.next();
        }
        return sum;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        Integer[] arr = arr();
        StreamIterator iterator = new StreamIterator(arr) {
            @Override
            public Integer next() {
                if (!hasNext()) {
                    return null;
                }
                if (!predicate.test(stream.get(next))) {
                    next++;
                    return next();
                }
                return stream.get(next++);
            }
        };
        return new AsIntStream(iterator);
    }

    @Override
    public void forEach(IntConsumer action) {
        while (it.hasNext()) {
            action.accept(it.next());
        }
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        Integer[] arr = arr();
        StreamIterator iterator = new StreamIterator(arr) {
            @Override
            public Integer next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return mapper.apply(stream.get(next++));
            }
        };
        return new AsIntStream(iterator);
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        Integer[] arr = arr();
        StreamIterator iterator = new StreamIterator(arr) {
            @Override
            public void streamNext() {
                Integer[] general = new Integer[0];
                while (next < arr.length) {
                    Integer[] array =
                            func.applyAsIntStream(stream.get(next)).arr();
                    int j = general.length;
                    general = Arrays.copyOf(general,
                            array.length + general.length);
                    int index = 0;
                    for (int i = j; i < general.length; i++) {
                        general[i] = array[index];
                        index++;
                    }
                    next++;
                }
                stream = Arrays.asList(general);
                next = 0;
            }
        };
        iterator.streamNext();
        return new AsIntStream(iterator);
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        while (it.hasNext()) {
            identity = op.apply(identity, it.next());
        }
        return identity;
    }

    @Override
    public int[] toArray() {
        List<Integer> lst = new ArrayList<>();
        while (it.hasNext()) {
            Integer val = it.next();
            if (val != null) {
                lst.add(val);
            }
        }
        int[] toReturn = new int[lst.size()];
        int i = 0;
        for (Integer x : lst) {
            toReturn[i] = x;
            i++;
        }
        return toReturn;
    }

}
