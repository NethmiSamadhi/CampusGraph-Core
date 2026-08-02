package core;

import java.util.List;

public class RouteResult {
    private final List<String> path;
    private final double cost;
    private final String message;

    public RouteResult(List<String> path, double cost, String message) {
        this.path = path;
        this.cost = cost;
        this.message = message;
    }

    public List<String> getPath() { return path; }
    public double getCost() { return cost; }
    public String getMessage() { return message; }
    public boolean isSuccess() { return path != null && !path.isEmpty(); }
}
