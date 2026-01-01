package org.team100.lib.targeting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RangeTest {
    private static final boolean DEBUG = false;
    private static final double DELTA = 0.001;

    @Test
    void testRange() {
        Drag d = new Drag(0.5, 0.025, 0.1, 0.1, 0.1);
        Range r = new Range(d, 8, 50);
        FiringSolution s = r.get(Math.PI / 4);
        assertEquals(2.823, s.range(), DELTA);
        assertEquals(1.00, s.tof(), DELTA);
    }

    /**
     * Is it worth caching at all?
     * 
     * The lookup and interpolation takes about 0.25 us on my machine, so the
     * RoboRIO could probably do it in around 1 us.
     * 
     * This is about 50X faster than the solution itself.
     * 
     * This happens inside the inner loop of a Newton solver.
     * 
     * How many iterations does it use?
     */
    @Test
    void testPerformance() {
        Drag d = new Drag(0.5, 0.025, 0.1, 0.1, 0.1);
        Range r = new Range(d, 10, 1);
        int iterations = 100000;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; ++i) {
            r.get(1);
        }
        long finishTime = System.currentTimeMillis();
        if (DEBUG) {
            System.out.printf("ET (s): %6.3f\n", ((double) finishTime - startTime) / 1000);
            System.out.printf("ET/call (ns): %6.3f\n ", 1000000 * ((double) finishTime - startTime) / iterations);
        }
    }

}
