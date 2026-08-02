package algorithms;

import graph.Edge;
import graph.Graph;
import core.RouteResult;

import java.util.*;
import java.util.function.ToDoubleFunction;

public class Dijkstra {

    private record QueueEntry(String nodeId, double distance) {}

    public static RouteResult findShortestPath(Graph graph, String startId, String endId,
                                                 ToDoubleFunction<Edge> weightFn, boolean avoidStairs) {
        if (!graph.containsNode(startId) || !graph.containsNode(endId)) {
            throw new IllegalArgumentException("Start or end node does not exist in the graph");
        }

        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        for (String nodeId : graph.allNodeIds()) {
            distances.put(nodeId, Double.POSITIVE_INFINITY);
        }
        distances.put(startId, 0.0);

        Set<String> visited = new HashSet<>();
        PriorityQueue<QueueEntry> pq = new PriorityQueue<>(Comparator.comparingDouble(QueueEntry::distance));
        pq.add(new QueueEntry(startId, 0.0));

        while (!pq.isEmpty()) {
            QueueEntry current = pq.poll();
            if (visited.contains(current.nodeId())) continue;
            visited.add(current.nodeId());

            if (current.nodeId().equals(endId)) break;

            for (Edge edge : graph.getNeighbors(current.nodeId())) {
                if (edge.isBlocked()) continue;
                if (avoidStairs && edge.hasStairs()) continue;

                double weight = weightFn != null ? weightFn.applyAsDouble(edge) : edge.getBaseWeight();
                double newDist = current.distance() + weight;

                if (newDist < distances.get(edge.getToNode())) {
                    distances.put(edge.getToNode(), newDist);
                    previous.put(edge.getToNode(), current.nodeId());
                    pq.add(new QueueEntry(edge.getToNode(), newDist));
                }
            }
        }

        if (Double.isInfinite(distances.get(endId))) {
            return new RouteResult(null, Double.POSITIVE_INFINITY, "No route found between these locations.");
        }

        List<String> path = new ArrayList<>();
        String node = endId;
        while (node != null) {
            path.add(node);
            node = previous.get(node);
        }
        Collections.reverse(path);

        return new RouteResult(path, distances.get(endId), "Route found.");
    }

    public static RouteResult findShortestPath(Graph graph, String startId, String endId) {
        return findShortestPath(graph, startId, endId, null, false);
    }
}
