package tests;

import algorithms.CongestionWeight;
import graph.Edge;
import testutil.TestRunner;

import java.time.LocalTime;
import java.util.function.ToDoubleFunction;

public class TestCongestion {
    public static void run() {
        System.out.println("\n-- TestCongestion --");
        LocalTime peakTime = LocalTime.of(8, 55);
        double peakMultiplier = CongestionWeight.getCongestionMultiplier(peakTime);
        TestRunner.assertEqual(1.8, peakMultiplier, "peak time returns correct multiplier");

        LocalTime offPeakTime = LocalTime.of(14, 30);
        double offPeakMultiplier = CongestionWeight.getCongestionMultiplier(offPeakTime);
        TestRunner.assertEqual(1.0, offPeakMultiplier, "off-peak time returns multiplier of 1.0");

        LocalTime classChangeTime = LocalTime.of(12, 0);
        ToDoubleFunction<Edge> weightFn = CongestionWeight.makeCongestionWeightFn(classChangeTime);
        Edge edge = new Edge("B", 10, false);
        TestRunner.assertEqual(18.0, weightFn.applyAsDouble(edge), "weight function scales edge weight correctly");
    }
}
