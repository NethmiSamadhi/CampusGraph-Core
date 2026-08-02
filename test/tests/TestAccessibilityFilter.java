package tests;

import algorithms.AccessibilityFilter;
import core.RouteResult;
import graph.Graph;
import graph.Node;
import testutil.TestRunner;

import java.util.List;

public class TestAccessibilityFilter {
    public static void run() {
        System.out.println("\n-- TestAccessibilityFilter --");
        Graph graph = new Graph();
        for (String id : new String[]{"A", "B", "C"}) {
            graph.addNode(new Node(id, "Location " + id, true));
        }
        graph.addEdge("A", "C", 2, true);
        graph.addEdge("A", "B", 3, false);
        graph.addEdge("B", "C", 3, false);

        RouteResult result = AccessibilityFilter.findAccessiblePath(graph, "A", "C", null);
        TestRunner.assertTrue(result.isSuccess(), "accessible route is found");
        TestRunner.assertEqual(List.of("A", "B", "C"), result.getPath(), "accessible route avoids the stairs edge");
        TestRunner.assertEqual(6.0, result.getCost(), "accessible route cost reflects the longer, stair-free path");

        Graph stairsOnlyGraph = new Graph();
        stairsOnlyGraph.addNode(new Node("X", "Location X", true));
        stairsOnlyGraph.addNode(new Node("Y", "Location Y", true));
        stairsOnlyGraph.addEdge("X", "Y", 5, true);

        RouteResult noAccessible = AccessibilityFilter.findAccessiblePath(stairsOnlyGraph, "X", "Y", null);
        TestRunner.assertFalse(noAccessible.isSuccess(), "no accessible route found when only stairs path exists");
        TestRunner.assertTrue(noAccessible.getMessage().contains("requires stairs"), "message clearly explains why route failed");
    }
}
