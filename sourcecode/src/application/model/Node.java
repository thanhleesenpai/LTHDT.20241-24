package application.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.*;

public class Node {

    private double x;
    private double y;
    private String name;
    private boolean visited;
    private final Circle circle;
    private final Text text;
    LinkedList<Edge> edges;

    Node(double x,double y, String name) {
        circle = new Circle(10);
        circle.setFill(Color.LIGHTSKYBLUE);
        circle.setStroke(Color.DODGERBLUE);
        circle.setPickOnBounds(false);
        text = new Text(name);
        this.setX(x);
        this.setY(y);
        this.name = name;
        visited = false;
        edges = new LinkedList<>();
    }
    public void setName(String name) {
		this.name = name;
	}

	public LinkedList<Edge> getEdge() {
        return edges;
    }

    public String getName() {
        return name;
    }

    public Circle getCircle(double scale) {
        circle.setCenterX(getX() * scale);
        circle.setCenterY(getY() * scale);
        return circle;
    }
    Circle getCircle(){
        return circle;
    }

    public Text getText() {
        text.layoutXProperty().bind(circle.centerXProperty().add(-text.getLayoutBounds().getWidth() / 2));
        text.layoutYProperty().bind(circle.centerYProperty().add(5));
        return text;
    }

    public boolean isVisited() {
        return visited;
    }

    public void visit() {
        visited = true;
    }

    public void unvisited() {
        visited = false;
    }
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
}