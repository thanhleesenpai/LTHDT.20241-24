package application.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class Edge {
    Node source;
    Node destination;
    double weight;
    private final Line line ;
    private final Text text;
    public Edge(Node s, Node d, double w) {
        line = new Line();
        text = new Text();
        source = s;
        destination = d;
        weight = w;

        DoubleProperty startX = new SimpleDoubleProperty(source.x);
        DoubleProperty startY = new SimpleDoubleProperty(source.y);
        DoubleProperty endX = new SimpleDoubleProperty(destination.x);
        DoubleProperty endY = new SimpleDoubleProperty(destination.y);
        line.startXProperty().bind(startX);
        line.startYProperty().bind(startY);
        line.endXProperty().bind(endX);
        line.endYProperty().bind(endY);
        startX.bind(source.getCircle().centerXProperty());
        startY.bind(source.getCircle().centerYProperty());
        endX.bind(destination.getCircle().centerXProperty());
        endY.bind(destination.getCircle().centerYProperty());
        text.setText(weight+"");
        text.xProperty().bind((startX.add(endX)).divide(2));
        text.yProperty().bind((startY.add(endY)).divide(2));

    }

    public Line getLine() {
        return line;
    }

    public Text getText() {
        return text;
    }

    public Node getDestination() {
        return destination;
    }

    public Node getSource() {
        return source;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    public String toString() {
        return String.format("(%s -> %s,%f)", source.name, destination.name, weight);
    }
}
