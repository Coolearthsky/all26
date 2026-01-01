package org.team100.lib.targeting;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N6;
import edu.wpi.first.math.system.NumericalIntegration;

/** Integrates the drag model until position is less than zero. */
public class RangeSolver {
    /**
     * RK4 integration with this resolution. It seems pretty coarse but yields an
     * acceptable error of around 1 cm.
     * 
     * See
     * RangeSolverTest.testDt()
     * https://docs.google.com/spreadsheets/d/1DTxWqwi1vgajUmj-J2YUuK79FHySJTi9KHT-RQxUAb0
     */
    static final double INTEGRATION_DT = 0.1;

    /**
     * @param d         drag model
     * @param v         muzzle speed in m/s
     * @param omega     spin in rad/s, positive is backspin
     * @param elevation in rad
     */
    public static FiringSolution getSolution(
            Drag d, double v, double omega, double elevation) {
        return solveWithDt(d, v, omega, elevation, INTEGRATION_DT);
    }

    static FiringSolution solveWithDt(
            Drag d, double v, double omega, double elevation, double dt) {
        if (dt < 1e-6)
            throw new IllegalArgumentException("must use nonzero dt");
        double vx = v * Math.cos(elevation);
        double vy = v * Math.sin(elevation);
        // state is (x, y, theta, vx, vy, omega)
        Matrix<N6, N1> x = VecBuilder.fill(0, 0, 0, vx, vy, omega);
        Matrix<N6, N1> prevX = x;
        double t = 0;
        double prevT = 0;
        for (t = 0; t < 10; t += dt) {
            x = NumericalIntegration.rk4(d, x, dt);
            double height = x.get(1, 0);
            if (height < 0) {
                // interpolate using the floor position
                double prevHeight = prevX.get(1, 0);
                double lerp = prevHeight / (prevHeight - height);
                double range = MathUtil.interpolate(prevX.get(0, 0), x.get(0, 0), lerp);
                double tof = MathUtil.interpolate(prevT, t, lerp);
                return new FiringSolution(range, tof);
            }
            prevX = x;
            prevT = t;
        }
        // if we got to the end, there's no (useful) solution.
        return null;
    }

}
