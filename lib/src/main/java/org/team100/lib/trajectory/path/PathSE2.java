package org.team100.lib.trajectory.path;

import java.util.ArrayList;
import java.util.List;

import org.team100.lib.geometry.Metrics;

/**
 * Represents a 2d holonomic path, i.e. with heading independent from course.
 * 
 * There's no timing information here. For that, see TrajectorySE2
 */
public class PathSE2 {
    private final List<PathSE2Entry> m_points;
    /**
     * Cumulative translational distance, just the xy plane, not the Twist arc
     * or anything else, just xy distance.
     */
    final double[] m_distances;

    public PathSE2(final List<PathSE2Entry> states) {
        int n = states.size();
        m_points = new ArrayList<>(n);
        m_distances = new double[n];
        if (states.isEmpty()) {
            return;
        }
        m_distances[0] = 0.0;
        m_points.add(states.get(0));
        for (int i = 1; i < n; ++i) {
            m_points.add(states.get(i));
            PathSE2Point p0 = getEntry(i - 1).point();
            PathSE2Point p1 = getEntry(i).point();
            double dist = Metrics.translationalDistance(p0.waypoint().pose(), p1.waypoint().pose());
            m_distances[i] = m_distances[i - 1] + dist;
        }
    }

    public int length() {
        return m_points.size();
    }

    public PathSE2Entry getEntry(int index) {
        if (m_points.isEmpty())
            return null;
        return m_points.get(index);
    }

    public double distance(int index) {
        if (m_points.isEmpty())
            return 0;
        return m_distances[index];
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length(); ++i) {
            builder.append(i);
            builder.append(": ");
            builder.append(getEntry(i).point());
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

}
