package graph;

import java.util.*;

public class Graph {
    private final Map<String, Node> nodes = new HashMap<>();
    private final Map<String, List<Edge>> adjacency = new HashMap<>();

    public void addNode(Node node) {
        if (!nodes.containsKey(node.getId())) {
            nodes.put(node.getId(), node);
            adjacency.put(node.getId(), new ArrayList<>());
        }
    }

    public void addEdge(String fromId, String toId, double weight, boolean hasStairs, boolean bidirectional) {
        if (!nodes.containsKey(fromId) || !nodes.containsKey(toId)) {
            throw new IllegalArgumentException("Cannot add edge: unknown node (" + fromId + " or " + toId + ")");
        }
        adjacency.get(fromId).add(new Edge(toId, weight, hasStairs));
        if (bidirectional) {
            adjacency.get(toId).add(new Edge(fromId, weight, hasStairs));
        }
    }

    public void addEdge(String fromId, String toId, double weight, boolean hasStairs) {
        addEdge(fromId, toId, weight, hasStairs, true);
    }

    public List<Edge> getNeighbors(String nodeId) {
        return adjacency.getOrDefault(nodeId, Collections.emptyList());
    }

    public Node getNode(String nodeId) { return nodes.get(nodeId); }
    public Map<String, Node> getAllNodes() { return nodes; }
    public boolean containsNode(String nodeId) { return nodes.containsKey(nodeId); }

    public void setBlocked(String fromId, String toId, boolean blocked) {
        for (Edge e : adjacency.getOrDefault(fromId, Collections.emptyList())) {
            if (e.getToNode().equals(toId)) e.setBlocked(blocked);
        }
        for (Edge e : adjacency.getOrDefault(toId, Collections.emptyList())) {
            if (e.getToNode().equals(fromId)) e.setBlocked(blocked);
        }
    }

    public Set<String> allNodeIds() { return nodes.keySet(); }

    @Override
    public String toString() {
        int edgeCount = adjacency.values().stream().mapToInt(List::size).sum() / 2;
        return "Graph(nodes=" + nodes.size() + ", edges=" + edgeCount + ")";
    }
}
