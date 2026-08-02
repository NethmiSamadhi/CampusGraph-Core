package core;

import algorithms.AccessibilityFilter;
import algorithms.CongestionWeight;
import algorithms.Dijkstra;
import algorithms.MultiStopOptimizer;
import graph.Edge;
import graph.Graph;

import java.util.List;
import java.util.function.ToDoubleFunction;

public class RoutePlanner {

    public static RouteResult planSingleRoute(Graph graph, String startId, String endId,
                                               boolean useCongestion, boolean accessibleMode) {
        ToDoubleFunction<Edge> weightFn = useCongestion ? CongestionWeight.makeCongestionWeightFn() : null;

        if (accessibleMode) {
            return AccessibilityFilter.findAccessiblePath(graph, startId, endId, weightFn);
        }
        return Dijkstra.findShortestPath(graph, startId, endId, weightFn, false);
    }

    public static MultiStopOptimizer.MultiStopResult planMultiStopRoute(Graph graph, String startId, List<String> stops,
                                                                          boolean useCongestion, boolean accessibleMode) {
        ToDoubleFunction<Edge> weightFn = useCongestion ? CongestionWeight.makeCongestionWeightFn() : null;
        return MultiStopOptimizer.optimizeRoute(graph, startId, stops, weightFn, accessibleMode);
    }
}
