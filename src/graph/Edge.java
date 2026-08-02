package graph;

public class Edge {
    private final String toNode;
    private final double baseWeight;
    private final boolean hasStairs;
    private boolean blocked;

    public Edge(String toNode, double baseWeight, boolean hasStairs) {
        this.toNode = toNode;
        this.baseWeight = baseWeight;
        this.hasStairs = hasStairs;
        this.blocked = false;
    }

    public String getToNode() { return toNode; }
    public double getBaseWeight() { return baseWeight; }
    public boolean hasStairs() { return hasStairs; }
    public boolean isBlocked() { return blocked; }
    public void setBlocked(boolean blocked) { this.blocked = blocked; }

    @Override
    public String toString() {
        return "Edge(-> " + toNode + ", w=" + baseWeight + ", stairs=" + hasStairs + ")";
    }
}
