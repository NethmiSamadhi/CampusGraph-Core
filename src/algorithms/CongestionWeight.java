package algorithms;

import graph.Edge;

import java.time.LocalTime;
import java.util.List;
import java.util.function.ToDoubleFunction;

public class CongestionWeight {

    public record CongestionWindow(LocalTime start, LocalTime end, double multiplier) {}

    public static final List<CongestionWindow> DEFAULT_WINDOWS = List.of(
            new CongestionWindow(LocalTime.of(8, 50), LocalTime.of(9, 5), 1.8),
            new CongestionWindow(LocalTime.of(11, 50), LocalTime.of(12, 5), 1.8),
            new CongestionWindow(LocalTime.of(12, 50), LocalTime.of(13, 5), 1.5),
            new CongestionWindow(LocalTime.of(16, 50), LocalTime.of(17, 5), 1.6)
    );

    public static double getCongestionMultiplier(LocalTime currentTime, List<CongestionWindow> windows) {
        for (CongestionWindow w : windows) {
            if (!currentTime.isBefore(w.start()) && !currentTime.isAfter(w.end())) {
                return w.multiplier();
            }
        }
        return 1.0;
    }

    public static double getCongestionMultiplier(LocalTime currentTime) {
        return getCongestionMultiplier(currentTime, DEFAULT_WINDOWS);
    }

    public static ToDoubleFunction<Edge> makeCongestionWeightFn(LocalTime currentTime, List<CongestionWindow> windows) {
        double multiplier = getCongestionMultiplier(currentTime, windows);
        return edge -> edge.getBaseWeight() * multiplier;
    }

    public static ToDoubleFunction<Edge> makeCongestionWeightFn(LocalTime currentTime) {
        return makeCongestionWeightFn(currentTime, DEFAULT_WINDOWS);
    }

    public static ToDoubleFunction<Edge> makeCongestionWeightFn() {
        return makeCongestionWeightFn(LocalTime.now(), DEFAULT_WINDOWS);
    }
}
