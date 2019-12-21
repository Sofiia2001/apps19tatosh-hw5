package ua.edu.ucu;

        import ua.edu.ucu.stream.*;
        import org.junit.Test;
        import static org.junit.Assert.*;
        import org.junit.Before;

        import java.util.NoSuchElementException;


public class AsIntStreamTest {

    private IntStream intStream;

    @Before
    public void init() {
        int[] intArr = {-100, -50, 0, 1, 2, 3, 4};
        intStream = AsIntStream.of(intArr);
    }

    @Test
    public void testStreamOperations() {
        int expRes = 361;
        int val = intStream
                .filter(x -> x > 1) //[2, 3, 4]
                .map(x -> x * 2) //[4, 6, 8]
                .flatMap(x -> AsIntStream.of(x - 1, x, x + 100))  //[3, 4, 104, 5, 6, 106, 7, 8, 108]
                .reduce(10, (sum, x) -> sum += x); // 361

        assertEquals(expRes, val);
    }

    @Test
    public void testFilterWithNoSuitableElements() {
        assertArrayEquals(intStream.filter(x -> x > 100).toArray(), new int[] {});
    }

    @Test
    public void testAvOperations() {
        Double expAv = -20.0;
        Double resAv = intStream.average();
        assertEquals(expAv, resAv);
    }

    @Test
    public void testMinOperations() {
        Integer expMin = -100;
        Integer resMin = intStream.min();
        assertEquals(expMin, resMin);
    }

    @Test
    public void testMaxOperations() {
        Integer expMax = 4;
        Integer resMax = intStream.max();
        assertEquals(expMax, resMax);
    }

    @Test
    public void testSumOperations() {
        Integer expSum = -140;
        Integer resSum = intStream.sum();
        assertEquals(expSum, resSum);
    }

    @Test(expected = NoSuchElementException.class)
    public void testPreviousClosedStream() {
        Integer max = intStream.max();
        intStream.map(x -> x * 2);
    }

}

