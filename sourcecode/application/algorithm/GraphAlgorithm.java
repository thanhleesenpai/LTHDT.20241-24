package application.algorithm;

import application.model.Node;
import application.model.Edge;

import java.util.*;

public class GraphAlgorithm {
    protected boolean directed;
    public Set<Node> nodes;

    // Constructor chung
    public GraphAlgorithm() {
        this.nodes = new HashSet<>();
    }

    public GraphAlgorithm(boolean directed) {
        this.directed = directed;
        nodes = new HashSet<>();
    }

    // Phương thức chung để thêm node
    public void addNode(Node... n) {
        // We're using a var arg method so we don't have to call
        // addNode repeatedly
        nodes.addAll(Arrays.asList(n));
    }

    public void addEdge(Node source, Node destination, double weight) {

        nodes.add(source);
        nodes.add(destination);

        // We're using addEdgeHelper to make sure we don't have duplicate edges
        addEdgeHelper(source, destination, weight);

        if (!directed && source != destination) {
            addEdgeHelper(destination, source, weight);
        }
    }
    private void addEdgeHelper(Node a, Node b, double weight) {

        for (Edge edge : a.getEdge()) {
            if (edge.getSource() == a && edge.getDestination() == b) {
                // Update the value in case it's a different one now
                edge.weight = weight;
                return;
            }
        }
        // If it hasn't been added already
        a.getEdge().add(new Edge(a, b, weight));
    }

    public void copyEdge(ArrayList<Edge> edges){
        for(Node node : nodes){
            edges.addAll(node.getEdge());
        }
    }

    public boolean DeleteEd(Node a, Node b) {
        boolean removed = false;

        // Xóa cạnh từ a đến b
        for (Iterator<Edge> iterator = a.getEdge().iterator(); iterator.hasNext(); ) {
            Edge edge = iterator.next();
            if (edge.getSource() == a && edge.getDestination() == b) {
                iterator.remove(); // Xóa cạnh
                removed = true;
                break;
            }
        }

        // Vo huong
        if (!directed) {
            for (Iterator<Edge> iterator = b.getEdge().iterator(); iterator.hasNext(); ) {
                Edge edge = iterator.next();
                if (edge.getSource() == b && edge.getDestination() == a) {
                    iterator.remove(); // Xóa cạnh
                    removed = true;
                    break;
                }
            }
        }
        return removed;
    }

    public void DeleteNo(Node from){
        for(Node node : nodes){
            node.getEdge().removeIf(edge -> edge.getSource() == from || edge.getDestination() == from);
        }
        nodes.remove(from);
        System.out.println("Node delete Successfully");
    }
    //public abstract String getShortestPath(Node start, Node end);
    // Kiểm tra xem có cạnh giữa hai node không
    public boolean hasEdge(Node source, Node destination) {
        LinkedList<Edge> edges = source.getEdge();
        for (Edge edge : edges) {
            if (edge.getDestination() == destination) {
                return true;
            }
        }
        return false;
    }
    public void resetNodesVisited() {
        for (Node node : nodes) {
            node.unvisited();
        }
    }
}
