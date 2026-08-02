package tests;

import algorithms.MultiStopOptimizer;
import graph.Graph;
import graph.Node;
import testutil.TestRunner;

import java.util.List;

public class TestMultiStopOptimizer {
    private static Graph buildLineGraph() {
        Graph graph = new Graph();
        for (String id : new String[]{"A", "B", "C", "D"}) {
            graph.addNode(new Node(id, "Location " + id, true));
        }
        graph.addEdge("A", "B", 1, false);
        graph.addEdge("B", "C", 1, false);
        graph.addEdge("C", "D", 1, false);
        return graph;
    }

    public static void run() {
        System.out.println("\n-- TestMultiStopOptimizer --");
        Graph graph = buildLineGraph();

        MultiStopOptimizer.MultiStopResult result = MultiStopOptimizer.optimizeRoute(graph, "A", List.of("D", "C"), null, false);
        TestRunner.assertEqual(List.of("C", "D"), result.visitOrder(), "optimizer picks C before D (not the typed order)");
        TestRunner.assertEqual(3.0, result.cost(), "optimizer total cost is minimal (no backtracking)");

        MultiStopOptimizer.MultiStopResult empty = MultiStopOptimizer.optimizeRoute(graph, "A", List.of(), null, false);
        TestRunner.assertEqual(List.of("A"), empty.path(), "empty stops returns start-only path");
        TestRunner.assertEqual(0.0, empty.cost(), "empty stops has zero cost");

        Graph graphWithIsolated = buildLineGraph();
        graphWithIsolated.addNode(new Node("Z", "Isolated", true));
        MultiStopOptimizer.MultiStopResult unreachable = MultiStopOptimizer.optimizeRoute(graphWithIsolated, "A", List.of("B", "Z"), null, false);
        TestRunner.assertNull(unreachable.path(), "unreachable stop returns null path");
    }
}
