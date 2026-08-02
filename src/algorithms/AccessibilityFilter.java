package algorithms;

import graph.Edge;
import graph.Graph;
import core.RouteResult;

import java.util.function.ToDoubleFunction;

public class AccessibilityFilter {

    public static RouteResult findAccessiblePath(Graph graph, String startId, String endId, ToDoubleFunction<Edge> weightFn) {
        RouteResult result = Dijkstra.findShortestPath(graph, startId, endId, weightFn, true);

        if (!result.isSuccess()) {
            RouteResult fallback = Dijkstra.findShortestPath(graph, startId, endId, weightFn, false);
            if (fallback.isSuccess()) {
                return new RouteResult(null, Double.POSITIVE_INFINITY,
                        "No wheelchair/stair-free route exists between these locations. A route exists but requires stairs.");
            }
            return new RouteResult(null, Double.POSITIVE_INFINITY, "No route exists between these locations at all.");
        }

        return new RouteResult(result.getPath(), result.getCost(), "Accessible route found (no stairs required).");
    }
}
