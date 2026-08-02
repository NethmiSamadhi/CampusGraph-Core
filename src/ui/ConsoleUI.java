package ui;

import core.CampusMapData;
import core.RouteFormatter;
import core.RoutePlanner;
import core.RouteResult;
import algorithms.MultiStopOptimizer;
import graph.Graph;
import graph.Node;

import java.util.*;

public class ConsoleUI {

    private static void printLocations(Graph graph) {
        System.out.println("\nAvailable locations:");
        List<String> ids = new ArrayList<>(graph.allNodeIds());
        Collections.sort(ids);
        for (String id : ids) {
            Node node = graph.getNode(id);
            String stairsNote = node.hasRamp() ? "" : "  (stairs-only access)";
            System.out.printf("  %-6s - %s%s%n", id, node.getName(), stairsNote);
        }
    }

    public static void run() {
        Graph graph = CampusMapData.buildSampleCampus();
        Scanner scanner = new Scanner(System.in);

        System.out.println("==================================================");
        System.out.println("  SMART CAMPUS NAVIGATION & SHORTEST PATH FINDER");
        System.out.println("==================================================");

        while (true) {
            printLocations(graph);
            System.out.println("\nOptions:");
            System.out.println("  1. Find shortest route (A -> B)");
            System.out.println("  2. Find multi-stop optimized route");
            System.out.println("  3. Find accessible (no-stairs) route");
            System.out.println("  4. Exit");
            System.out.print("\nChoose an option (1-4): ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("Start location ID: ");
                        String start = scanner.nextLine().trim().toUpperCase();
                        System.out.print("Destination location ID: ");
                        String end = scanner.nextLine().trim().toUpperCase();
                        RouteResult result = RoutePlanner.planSingleRoute(graph, start, end, true, false);
                        System.out.println("\n" + RouteFormatter.formatSummary(graph, result.getPath(), result.getCost(), result.getMessage()));
                        if (result.isSuccess()) System.out.println(RouteFormatter.formatDirections(graph, result.getPath()));
                    }
                    case "2" -> {
                        System.out.print("Start location ID: ");
                        String start = scanner.nextLine().trim().toUpperCase();
                        System.out.print("Stop IDs (comma-separated): ");
                        String stopsRaw = scanner.nextLine().trim().toUpperCase();
                        List<String> stops = Arrays.stream(stopsRaw.split(","))
                                .map(String::trim).filter(s -> !s.isEmpty()).toList();
                        MultiStopOptimizer.MultiStopResult result = RoutePlanner.planMultiStopRoute(graph, start, stops, true, false);
                        System.out.println("\n" + RouteFormatter.formatSummary(graph, result.path(), result.cost(), result.message()));
                        if (result.path() != null) System.out.println(RouteFormatter.formatDirections(graph, result.path()));
                    }
                    case "3" -> {
                        System.out.print("Start location ID: ");
                        String start = scanner.nextLine().trim().toUpperCase();
                        System.out.print("Destination location ID: ");
                        String end = scanner.nextLine().trim().toUpperCase();
                        RouteResult result = RoutePlanner.planSingleRoute(graph, start, end, true, true);
                        System.out.println("\n" + RouteFormatter.formatSummary(graph, result.getPath(), result.getCost(), result.getMessage()));
                        if (result.isSuccess()) System.out.println(RouteFormatter.formatDirections(graph, result.getPath()));
                    }
                    case "4" -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid option, try again.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
