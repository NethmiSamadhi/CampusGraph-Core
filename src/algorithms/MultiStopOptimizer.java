package algorithms;

import graph.Edge;
import graph.Graph;
import core.RouteResult;

import java.util.*;
import java.util.function.ToDoubleFunction;

public class MultiStopOptimizer {

    private static final int EXACT_SOLUTION_LIMIT = 6;

    public record MultiStopResult(List<String> path, double cost, List<String> visitOrder, String message) {}

    private static RouteResult pathCost(Graph graph, String start, List<String> stopsOrder,
                                         ToDoubleFunction<Edge> weightFn, boolean avoidStairs) {
        List<String> fullPath = new ArrayList<>();
        double totalCost = 0;
        String current = start;

        for (String stop : stopsOrder) {
            RouteResult segment = Dijkstra.findShortestPath(graph, current, stop, weightFn, avoidStairs);
            if (!segment.isSuccess()) {
                return new RouteResult(null, Double.POSITIVE_INFINITY, "Unreachable stop: " + stop);
            }
            if (fullPath.isEmpty()) {
                fullPath.addAll(segment.getPath());
            } else {
                fullPath.addAll(segment.getPath().subList(1, segment.getPath().size()));
            }
            totalCost += segment.getCost();
            current = stop;
        }

        return new RouteResult(fullPath, totalCost, "ok");
    }

    private static void permute(List<String> stops, int k, List<List<String>> results) {
        if (k == stops.size()) {
            results.add(new ArrayList<>(stops));
            return;
        }
        for (int i = k; i < stops.size(); i++) {
            Collections.swap(stops, k, i);
            permute(stops, k + 1, results);
            Collections.swap(stops, k, i);
        }
    }

    public static MultiStopResult optimizeExact(Graph graph, String start, List<String> stops,
                                                  ToDoubleFunction<Edge> weightFn, boolean avoidStairs) {
        List<List<String>> allOrders = new ArrayList<>();
        permute(new ArrayList<>(stops), 0, allOrders);

        List<String> bestPath = null;
        double bestCost = Double.POSITIVE_INFINITY;
        List<String> bestOrder = null;

        for (List<String> order : allOrders) {
            RouteResult result = pathCost(graph, start, order, weightFn, avoidStairs);
            if (result.getCost() < bestCost) {
                bestPath = result.getPath();
                bestCost = result.getCost();
                bestOrder = order;
            }
        }

        if (bestPath == null) {
            return new MultiStopResult(null, Double.POSITIVE_INFINITY, null, "One or more stops are unreachable.");
        }
        return new MultiStopResult(bestPath, bestCost, bestOrder, "Optimized multi-stop route found (exact).");
    }

    public static MultiStopResult optimizeGreedy(Graph graph, String start, List<String> stops,
                                                  ToDoubleFunction<Edge> weightFn, boolean avoidStairs) {
        Set<String> remaining = new HashSet<>(stops);
        String current = start;
        List<String> order = new ArrayList<>();
        List<String> fullPath = new ArrayList<>();
        double totalCost = 0;

        while (!remaining.isEmpty()) {
            String bestNext = null;
            List<String> bestSegment = null;
            double bestCost = Double.POSITIVE_INFINITY;

            for (String stop : remaining) {
                RouteResult result = Dijkstra.findShortestPath(graph, current, stop, weightFn, avoidStairs);
                if (result.getCost() < bestCost) {
                    bestNext = stop;
                    bestSegment = result.getPath();
                    bestCost = result.getCost();
                }
            }

            if (bestNext == null) {
                return new MultiStopResult(null, Double.POSITIVE_INFINITY, null, "One or more stops are unreachable.");
            }

            if (fullPath.isEmpty()) {
                fullPath.addAll(bestSegment);
            } else {
                fullPath.addAll(bestSegment.subList(1, bestSegment.size()));
            }
            totalCost += bestCost;
            order.add(bestNext);
            current = bestNext;
            remaining.remove(bestNext);
        }

        return new MultiStopResult(fullPath, totalCost, order, "Optimized multi-stop route found (greedy heuristic).");
    }

    public static MultiStopResult optimizeRoute(Graph graph, String start, List<String> stops,
                                                 ToDoubleFunction<Edge> weightFn, boolean avoidStairs) {
        if (stops == null || stops.isEmpty()) {
            return new MultiStopResult(List.of(start), 0, List.of(), "No stops provided.");
        }
        if (stops.size() <= EXACT_SOLUTION_LIMIT) {
            return optimizeExact(graph, start, stops, weightFn, avoidStairs);
        } else {
            return optimizeGreedy(graph, start, stops, weightFn, avoidStairs);
        }
    }
}
