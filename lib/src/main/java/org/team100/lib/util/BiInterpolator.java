package org.team100.lib.util;

/**
 * Two-dimensional interpolation.
 * Common example would be bilinear interpolation.
 * 
 * matrix notation is used:
 * 
 * .....t2 --->
 * ..t1.+---------+
 * ..|..| v11 v12 |
 * ..v..| v21 v22 |
 * .....+---------+
 */
public interface BiInterpolator<T> {
    T interpolate(T v11, T v12, T v21, T v22, double t1, double t2);
}
