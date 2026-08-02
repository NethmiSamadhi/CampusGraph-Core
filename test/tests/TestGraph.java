package tests;

import graph.Edge;
import graph.Graph;
import graph.Node;
import testutil.TestRunner;

public class TestGraph {
    public static void run() {
        System.out.println("\n-- TestGraph --");
        Graph graph = new Graph();
        graph.addNode(new Node("A", "Location A", true));
        graph.addNode(new Node("B", "Location B", true));
        graph.addNode(new Node("C", "Location C", true));

        TestRunner.assertTrue(graph.containsNode("A"), "addNode: node A exists");
        TestRunner.assertEqual(3, graph.allNodeIds().size(), "addNode: correct node count");

        graph.addEdge("A", "B", 5, false);
        boolean aHasB = graph.getNeighbors("A").stream().anyMatch(e -> e.getToNode().equals("B"));
        boolean bHasA = graph.getNeighbors("B").stream().anyMatch(e -> e.getToNode().equals("A"));
        TestRunner.assertTrue(aHasB, "addEdge: bidirectional A->B exists");
        TestRunner.assertTrue(bHasA, "addEdge: bidirectional B->A exists");

        TestRunner.assertThrows(() -> graph.addEdge("A", "X", 5, false), "addEdge: invalid node raises exception");

        graph.setBlocked("A", "B", true);
        Edge blockedEdge = graph.getNeighbors("A").stream().filter(e -> e.getToNode().equals("B")).findFirst().get();
        TestRunner.assertTrue(blockedEdge.isBlocked(), "setBlocked: edge correctly marked blocked");

        TestRunner.assertTrue(graph.getNeighbors("C").isEmpty(), "disconnected node C has no neighbors");
    }
}
