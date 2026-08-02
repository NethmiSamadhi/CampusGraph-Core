package core;

import graph.Graph;

import java.util.List;
import java.util.stream.Collectors;

public class RouteFormatter {

    public static String formatDirections(Graph graph, List<String> path) {
        if (path == null || path.isEmpty()) {
            return "No path available.";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            String nodeId = path.get(i);
            String name = graph.getNode(nodeId).getName();
            if (i == 0) {
                sb.append("Start at ").append(name).append(".\n");
            } else if (i == path.size() - 1) {
                sb.append("Arrive at ").append(name).append(". You have reached your destination.\n");
            } else {
                sb.append("Continue to ").append(name).append(".\n");
            }
        }
        return sb.toString().trim();
    }

    public static String formatSummary(Graph graph, List<String> path, double cost, String message) {
        if (path == null || path.isEmpty()) {
            return message != null ? message : "No route could be generated.";
        }

        String names = path.stream()
                .map(id -> graph.getNode(id).getName())
                .collect(Collectors.joining(" -> "));

        return message + "\n" +
                "Route: " + names + "\n" +
                "Estimated total cost (time/distance units): " + Math.round(cost * 100.0) / 100.0;
    }
}
