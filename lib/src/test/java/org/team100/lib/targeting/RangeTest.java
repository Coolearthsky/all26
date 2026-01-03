package org.team100.lib.targeting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RangeTest {
    private static final boolean DEBUG = false;
    private static final double DELTA = 0.001;

    @Test
    void testRange() {
        Drag d = new Drag(0.5, 0.025, 0.1, 0.1, 0.1);
        Range r = new Range(d, 8, 50, false);
        FiringSolution s = r.get(Math.PI / 4);
        assertEquals(2.825, s.range(), DELTA);
        assertEquals(1.011, s.tof(), DELTA);
    }

    /**
     * Is it worth caching?
     * 
     * Without caching, a range solution takes about 50 us on my machine.
     * Wih caching, it takes much much less, maybe 0.1 us.
     * 
     * The range solver is inside the inner loop of a Newton solver which takes 5 or
     * 10 iterations to converge, so uncached, up to 0.5 ms on my machine, or maybe
     * 2 or 3 ms on the RoboRIO -- a substantial fraction of the time budget.
     * 
     * So caching seems like a good idea.
     */
    @Test
    void testPerformance() {
        double uncachedETperCall;
        {
            Drag d = new Drag(0.5, 0.025, 0.1, 0.1, 0.1);
            Range r = new Range(d, 10, 1, false);
            int iterations = 10000;
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < iterations; ++i) {
                r.get(1);
            }
            long finishTime = System.currentTimeMillis();
            if (DEBUG) {
                System.out.printf("Uncached ET (s): %6.3f\n", ((double) finishTime - startTime) / 1000);
                uncachedETperCall = 1000000 * ((double) finishTime - startTime) / iterations;
                System.out.printf("Uncached ET/call (ns): %6.3f\n ", uncachedETperCall);
            }
        }
        double cachedETperCall;
        {
            Drag d = new Drag(0.5, 0.025, 0.1, 0.1, 0.1);
            Range r = new Range(d, 10, 1, true);
            int iterations = 10000000;
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < iterations; ++i) {
                r.get(1);
            }
            long finishTime = System.currentTimeMillis();
            if (DEBUG) {
                System.out.printf("Cached ET (s): %6.3f\n", ((double) finishTime - startTime) / 1000);
                cachedETperCall = 1000000 * ((double) finishTime - startTime) / iterations;
                System.out.printf("Cached ET/call (ns): %6.3f\n ", cachedETperCall);
            }
        }
        if (DEBUG)
            System.out.printf("cache benefit %f\n", uncachedETperCall / cachedETperCall);
    }

}
