package application;
import java.util.*;

class BellmanFord {
    private final List<Node> nodes;
    private final List<Edge> edges;

    public BellmanFord(List<Node> nodes, List<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public String shortestPath(Node source, Node target) {
        Map<Node, Double> distance = new HashMap<>();
        Map<Node, Node> predecessor = new HashMap<>();
        for (Node node : nodes) {
            distance.put(node, Double.POSITIVE_INFINITY);
        }
        distance.put(source, 0.0);

        // Relax edges |V|-1 times
        for (int i = 0; i < nodes.size() - 1; i++) {
            for (Edge edge : edges) {
                if (distance.get(edge.source) + edge.weight < distance.get(edge.destination)) {
                    distance.put(edge.destination, distance.get(edge.source) + edge.weight);
                    predecessor.put(edge.destination, edge.source);
                }
            }
        }

        // Check for negative weight cycles
        for (Edge edge : edges) {
            if (distance.get(edge.source) + edge.weight < distance.get(edge.destination)) {
                return "Graph contains a negative weight cycle.";
            }
        }

        // Build path
        StringBuilder path = new StringBuilder(target.name);
        Node step = target;
        while (predecessor.get(step) != null) {
            step = predecessor.get(step);
            path.insert(0, step.name + " -> ");
        }
        return "Shortest Path: " + path + "\nCost: " + distance.get(target);
    }
}