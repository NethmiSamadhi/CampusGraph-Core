package tests;

import algorithms.Dijkstra;
import core.RouteResult;
import graph.Graph;
import graph.Node;
import testutil.TestRunner;

import java.util.List;

public class TestDijkstra {
    private static Graph buildGraph() {
        Graph graph = new Graph();
        for (String id : new String[]{"A", "B", "C", "D"}) {
            graph.addNode(new Node(id, "Location " + id, true));
        }
        graph.addEdge("A", "B", 1, false);
        graph.addEdge("B", "C", 2, false);
        graph.addEdge("A", "C", 10, false);
        graph.addEdge("C", "D", 1, false);
        return graph;
    }

    public static void run() {
        System.out.println("\n-- TestDijkstra --");
        Graph graph = buildGraph();

        RouteResult result = Dijkstra.findShortestPath(graph, "A", "D");
        TestRunner.assertEqual(List.of("A", "B", "C", "D"), result.getPath(), "shortest path A->D is correct");
        TestRunner.assertEqual(4.0, result.getCost(), "shortest path A->D cost is correct");

        RouteResult same = Dijkstra.findShortestPath(graph, "A", "A");
        TestRunner.assertEqual(List.of("A"), same.getPath(), "same start and end returns single-node path");
        TestRunner.assertEqual(0.0, same.getCost(), "same start and end cost is zero");

        Graph graphWithIsolated = buildGraph();
        graphWithIsolated.addNode(new Node("Z", "Isolated", true));
        RouteResult unreachable = Dijkstra.findShortestPath(graphWithIsolated, "A", "Z");
        TestRunner.assertFalse(unreachable.isSuccess(), "unreachable node returns no path");

        Graph blockedGraph = buildGraph();
        blockedGraph.setBlocked("B", "C", true);
        RouteResult reroute = Dijkstra.findShortestPath(blockedGraph, "A", "D");
        TestRunner.assertEqual(List.of("A", "C", "D"), reroute.getPath(), "blocked edge forces reroute via A->C->D");
        TestRunner.assertEqual(11.0, reroute.getCost(), "reroute cost accounts for longer path");

        TestRunner.assertThrows(() -> Dijkstra.findShortestPath(graph, "A", "NOT_REAL"), "invalid node raises exception");
    }
}
