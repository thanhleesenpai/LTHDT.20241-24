package application.algorithm;

import application.model.Edge;

import application.model.Node;

import java.util.*;

public class Flooding extends GraphAlgorithm {

    public Flooding() {
        super(); // Gọi constructor của lớp cha
    }

    public  Flooding (boolean directed) {
        this.directed = directed;
        nodes = new HashSet<>();
    }

    public Stack<Node> animatePathfl(Node start, Node end) {
        Stack<Node> path = new Stack<>();
        HashMap<Node, Node> predecessors = new HashMap<>();
        Set<Node> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();

        // Khởi tạo
        queue.add(start);
        visited.add(start);
        predecessors.put(start, null);

        // Bắt đầu lan tỏa
        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // Kiểm tra nếu đã đến đỉnh đích
            if (current.equals(end)) {
                Node temp = end;
                while (temp != null) {
                    path.push(temp); // Lưu đường đi vào Stack
                    temp = predecessors.get(temp);
                }
                return path; // Trả về đường đi
            }

            // Lan tỏa đến các đỉnh kề chưa thăm
            for (Edge edge : current.getEdge()) {
                Node neighbor = edge.getDestination();
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    predecessors.put(neighbor, current);
                }
            }
        }

        // Nếu không tìm được đường đi, trả về null
        return null;
    }
}
