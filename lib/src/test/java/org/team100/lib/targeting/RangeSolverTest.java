package org.team100.lib.targeting;

import org.junit.jupiter.api.Test;

public class RangeSolverTest {
    private static final boolean DEBUG = false;

    /**
     * How should we choose a value for DT? We should choose a value such that the
     * expected integration error is tolerable. How do we know the integration
     * error? Try varying DT to see how it affects the outcome. If smaller DT gets
     * the same answer as larger DT, then there's no reason to use the smaller
     * one.
     * 
     * This can be viewed here:
     * 
     * https://docs.google.com/spreadsheets/d/1DTxWqwi1vgajUmj-J2YUuK79FHySJTi9KHT-RQxUAb0
     * 
     * The interpolation in the solver makes the variation of the solution with dt
     * very small.
     * 
     * Assuming the value for very small dt is "correct," the absolute error is very
     * roughly the square of dt, a little worse (dt^2.5) at larger values.
     * 
     * For 1 cm range accuracy in integration (for this middle-of-the-road
     * trajectory), a reasonable dt would be something like 0.1 sec.
     * 
     * Note 0.1 sec is really coarse. RK4 integration works!
     */
    @Test
    void testDt() {
        Drag d = new Drag(0.5, 0.1, 0.1, 0.1, 0.1);
        double ddt = 0.0001;
        double dtmax = 0.25;
        for (double dt = ddt; dt < dtmax; dt += ddt) {
            FiringSolution s = RangeSolver.solveWithDt(d, 10, 1, 1, dt);
            System.out.printf("%20.10f %20.10f\n", dt, s.range());
        }
    }

    /**
     * Using the coarse DT value above, what's the performance of the integration?
     * 
     * On my machine, which is ~4x faster than a RoboRIO, I get something like 10 us
     * per solve, so the RoboRIO might get 50 us.
     */
    @Test
    void testPerformance() {
        Drag d = new Drag(0.5, 0, 0.1, 0.1, 0);
        int iterations = 100000;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; ++i) {
            RangeSolver.getSolution(d, 10, 0, 1);
        }
        long finishTime = System.currentTimeMillis();
        if (DEBUG) {
            System.out.printf("ET (s): %6.3f\n", ((double) finishTime - startTime) / 1000);
            System.out.printf("ET/call (ns): %6.3f\n ", 1000000 * ((double) finishTime - startTime) / iterations);
        }
    }

}
