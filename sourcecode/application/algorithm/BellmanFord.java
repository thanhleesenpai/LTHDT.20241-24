package application.algorithm;

import java.util.*;

import application.model.*;

public class BellmanFord extends GraphAlgorithm {

    public BellmanFord() {
        super();  // Gọi constructor của lớp cha
    }
//
//    public  BellmanFord (boolean directed) {
//        this.directed = directed;
//        nodes = new HashSet<>();
//    }

    public Stack<Node> animatePathbf(Node start, Node end) {
        Stack<Node> path = new Stack<>();
        HashMap<Node, Double> distance = new HashMap<>();
        HashMap<Node, Node> predecessor = new HashMap<>();

        // Khởi tạo khoảng cách từ start đến tất cả các node là vô cùng, trừ start là 0
        for (Node node : nodes) {
            distance.put(node, Double.POSITIVE_INFINITY);
        }
        distance.put(start, 0.0);

        // Relax tất cả các cạnh |V| - 1 lần
        for (int i = 0; i < nodes.size() - 1; i++) {
            for (Node node : nodes) {
                for (Edge edge : node.getEdge()) {
                    if (distance.get(node) + edge.getWeight() < distance.get(edge.getDestination())) {
                        distance.put(edge.getDestination(), distance.get(node) + edge.getWeight());
                        predecessor.put(edge.getDestination(), node);
                    }
                }
            }
        }

        // Kiểm tra chu trình âm
        for (Node node : nodes) {
            for (Edge edge : node.getEdge()) {
                if (distance.get(node) + edge.getWeight() < distance.get(edge.getDestination())) {
                    return null; // Phát hiện chu trình âm
                }
            }
        }

        // Truy vết đường đi từ end về start
        Node current = end;
        while (current != null) {
            path.push(current); // Lưu trữ node hiện tại vào Stack
            current = predecessor.get(current); // Truy ngược đến node cha
        }

        // Kiểm tra nếu không tìm thấy đường đi
        if (path.isEmpty() || !path.peek().equals(start)) {
            return null;
        }

        return path;
    }


}
