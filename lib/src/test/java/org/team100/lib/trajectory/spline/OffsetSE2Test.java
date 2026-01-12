package org.team100.lib.trajectory.spline;

import java.util.ArrayList;
import java.util.List;

import org.jfree.data.xy.VectorSeries;
import org.junit.jupiter.api.Test;
import org.team100.lib.geometry.DirectionSE2;
import org.team100.lib.geometry.WaypointSE2;
import org.team100.lib.trajectory.path.PathSE2;
import org.team100.lib.trajectory.path.PathSE2Factory;
import org.team100.lib.trajectory.path.PathSE2ToVectorSeries;
import org.team100.lib.util.ChartUtil;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

public class OffsetSE2Test {
    /** Render spline and offset spline */
    @Test
    void test0() {
        WaypointSE2 w0 = new WaypointSE2(
                new Pose2d(new Translation2d(), new Rotation2d(Math.PI / 2)),
                new DirectionSE2(1, 0, -1), 1);
        WaypointSE2 w1 = new WaypointSE2(
                new Pose2d(new Translation2d(Math.PI / 2, 0), new Rotation2d(0)),
                new DirectionSE2(1, 0, -1), 1);
        SplineSE2 toolpoint = new SplineSE2(w0, w1);
        List<VectorSeries> series = new SplineSE2ToVectorSeries(0.1).convert(List.of(toolpoint));
        // this is always zero
        List<VectorSeries> series2 = new SplineSE2ToVectorSeries(0.1).curvature(List.of(toolpoint));

        double length = 1.0;
        OffsetSE2 offset = new OffsetSE2(toolpoint, length);
        List<VectorSeries> series3 = new SplineSE2ToVectorSeries(0.1).convert(List.of(offset));
        // curvature is infinite at the start since the course is changing
        // but the offset endpoint is not moving
        List<VectorSeries> series4 = new SplineSE2ToVectorSeries(0.1).curvature(List.of(offset));

        List<VectorSeries> all = new ArrayList<>();
        all.addAll(series);
        all.addAll(series2);
        all.addAll(series3);
        all.addAll(series4);
        ChartUtil.plotOverlay(all, 500);
    }

    /** Render path and offset path */
    @Test
    void test1() {
        WaypointSE2 w0 = new WaypointSE2(
                new Pose2d(new Translation2d(), new Rotation2d(Math.PI / 2)),
                new DirectionSE2(1, 0, -1), 1);
        WaypointSE2 w1 = new WaypointSE2(
                new Pose2d(new Translation2d(Math.PI / 2, 0), new Rotation2d(0)),
                new DirectionSE2(1, 0, -1), 1);
        SplineSE2 toolpoint = new SplineSE2(w0, w1);
        double length = 1.0;
        OffsetSE2 offset = new OffsetSE2(toolpoint, length);

        PathSE2Factory pathFactory = new PathSE2Factory(0.1, 0.001, 0.001);
        PathSE2 toolpointPath = pathFactory.get(List.of(toolpoint));
        PathSE2 offsetPath = pathFactory.get(List.of(offset));

        List<VectorSeries> series = new PathSE2ToVectorSeries(0.1).convert(toolpointPath);

        // this is always zero
        List<VectorSeries> series2 = new PathSE2ToVectorSeries(0.1).curvature(toolpointPath);

        List<VectorSeries> series3 = new PathSE2ToVectorSeries(0.1).convert(offsetPath);
        // curvature is infinite at the start since the course is changing
        // but the offset endpoint is not moving
        List<VectorSeries> series4 = new PathSE2ToVectorSeries(0.1).curvature(offsetPath);

        List<VectorSeries> all = new ArrayList<>();
        all.addAll(series);
        all.addAll(series2);
        all.addAll(series3);
        all.addAll(series4);
        ChartUtil.plotOverlay(all, 500);
    }

}
