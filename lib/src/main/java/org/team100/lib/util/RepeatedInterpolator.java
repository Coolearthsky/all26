package org.team100.lib.util;

import edu.wpi.first.math.interpolation.Interpolator;

/**
 * Two-dimensional interpolation by performing one-dimensional interpolation
 * three times.
 * 
 * https://en.wikipedia.org/wiki/Bilinear_interpolation#Repeated_linear_interpolation
 */
public class RepeatedInterpolator<T> implements BiInterpolator<T> {

    private final Interpolator<T> m_i;

    public RepeatedInterpolator(Interpolator<T> i) {
        m_i = i;
    }

    @Override
    public T interpolate(
            T v11, T v12,
            T v21, T v22,
            double t1, double t2) {
        T top = m_i.interpolate(v11, v12, t2);
        T bottom = m_i.interpolate(v21, v22, t2);
        return m_i.interpolate(top, bottom, t1);
    }
}
