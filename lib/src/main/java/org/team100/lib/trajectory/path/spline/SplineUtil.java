package org.team100.lib.trajectory.path.spline;

import edu.wpi.first.math.Num;
import edu.wpi.first.math.Vector;

public class SplineUtil {
    /**
     * Curvature vector in any dimensionality.
     * 
     * Note for a spline in SE(n), the curvature describes the path in Rn, i.e. the
     * rotational part of the spline is not included.
     * 
     * See MATH.md.
     * 
     * @param rprime      position derivative with respect to any parameterization
     * @param rprimeprime second derivative
     */
    public static <N extends Num> Vector<N> K(Vector<N> rprime, Vector<N> rprimeprime) {
        double rprimenorm = rprime.norm();
        Vector<N> T = rprime.div(rprimenorm);
        Vector<N> p2 = rprimeprime.div(rprimenorm * rprimenorm);
        Vector<N> K = p2.minus(T.times(T.dot(p2)));
        return K;
    }

}
