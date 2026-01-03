package org.team100.lib.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.wpi.first.math.MathUtil;

public class DoubleBilinearInterpolatorTest {
    private static final double DELTA = 0.001;

    @Test
    void test0() {
        RepeatedInterpolator<Double> dbi = new RepeatedInterpolator<>(MathUtil::interpolate);
        double v11 = 0;
        double v12 = 1;
        double v21 = 2;
        double v22 = 3;
        assertEquals(0.00, dbi.interpolate(v11, v12, v21, v22, 0.00, 0.00), DELTA);
        assertEquals(0.50, dbi.interpolate(v11, v12, v21, v22, 0.25, 0.00), DELTA);
        assertEquals(1.00, dbi.interpolate(v11, v12, v21, v22, 0.50, 0.00), DELTA);
        assertEquals(1.50, dbi.interpolate(v11, v12, v21, v22, 0.75, 0.00), DELTA);
        assertEquals(2.00, dbi.interpolate(v11, v12, v21, v22, 1.00, 0.00), DELTA);
        assertEquals(0.25, dbi.interpolate(v11, v12, v21, v22, 0.00, 0.25), DELTA);
        assertEquals(0.75, dbi.interpolate(v11, v12, v21, v22, 0.25, 0.25), DELTA);
        assertEquals(1.25, dbi.interpolate(v11, v12, v21, v22, 0.50, 0.25), DELTA);
        assertEquals(1.75, dbi.interpolate(v11, v12, v21, v22, 0.75, 0.25), DELTA);
        assertEquals(2.25, dbi.interpolate(v11, v12, v21, v22, 1.00, 0.25), DELTA);
        assertEquals(0.50, dbi.interpolate(v11, v12, v21, v22, 0.00, 0.50), DELTA);
        assertEquals(1.00, dbi.interpolate(v11, v12, v21, v22, 0.25, 0.50), DELTA);
        assertEquals(1.50, dbi.interpolate(v11, v12, v21, v22, 0.50, 0.50), DELTA);
        assertEquals(2.00, dbi.interpolate(v11, v12, v21, v22, 0.75, 0.50), DELTA);
        assertEquals(2.50, dbi.interpolate(v11, v12, v21, v22, 1.00, 0.50), DELTA);
        assertEquals(0.75, dbi.interpolate(v11, v12, v21, v22, 0.00, 0.75), DELTA);
        assertEquals(1.25, dbi.interpolate(v11, v12, v21, v22, 0.25, 0.75), DELTA);
        assertEquals(1.75, dbi.interpolate(v11, v12, v21, v22, 0.50, 0.75), DELTA);
        assertEquals(2.25, dbi.interpolate(v11, v12, v21, v22, 0.75, 0.75), DELTA);
        assertEquals(2.75, dbi.interpolate(v11, v12, v21, v22, 1.00, 0.75), DELTA);
        assertEquals(1.00, dbi.interpolate(v11, v12, v21, v22, 0.00, 1.00), DELTA);
        assertEquals(1.50, dbi.interpolate(v11, v12, v21, v22, 0.25, 1.00), DELTA);
        assertEquals(2.00, dbi.interpolate(v11, v12, v21, v22, 0.50, 1.00), DELTA);
        assertEquals(2.50, dbi.interpolate(v11, v12, v21, v22, 0.75, 1.00), DELTA);
        assertEquals(3.00, dbi.interpolate(v11, v12, v21, v22, 1.00, 1.00), DELTA);
    }

}
