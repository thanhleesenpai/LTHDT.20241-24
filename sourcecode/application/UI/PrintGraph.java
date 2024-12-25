package application.UI;

import application.model.Edge;
import application.model.Graph;
import application.model.Node;
import javafx.animation.PathTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Stack;

public class PrintGraph {
    private final Graph graph;
    private final ToggleButton addNode = new ToggleButton("Add Node");
    private final ToggleButton deleteNode = new ToggleButton("Delete Node");
    private final ToggleButton addEdge = new ToggleButton("Add Edge");
    private final ToggleButton deleteEdge = new ToggleButton("Delete Edge");
    private DoubleProperty endX;
    private DoubleProperty endY;
    private Line newEdge;

    private final HBox buttonBox = new HBox();

    PrintGraph(Graph graph) {
        this.graph = graph;
        ToggleGroup toggleButtons = new ToggleGroup();
        toggleButtons.getToggles().addAll(addNode, deleteNode, addEdge, deleteEdge);
        buttonBox.getChildren().addAll(addNode, deleteNode, addEdge, deleteEdge);
    }

    void showGraph(Pane pane) {
        buttonBox.setSpacing(10);
        buttonBox.setLayoutX(300);
        buttonBox.setLayoutY(30);

        Pane tempPane = new Pane();
        tempPane.setPrefWidth(2160);
        tempPane.setPrefHeight(1080);
        getGraphPane(tempPane, true);

        TextInputDialog td = new TextInputDialog("");
        td.setHeaderText("Enter Vertex name");

        tempPane.setOnMouseClicked(mouseEvent -> {
            if (addNode.isSelected()) {
                td.getEditor().clear();
                td.showAndWait().ifPresent(input -> {
                    if (!input.trim().isEmpty() && graph.getNode(input.toLowerCase()) == null) {
                        tempPane.getChildren().clear();
                        graph.addNode(mouseEvent.getX(), mouseEvent.getY(), input);
                        getGraphPane(tempPane, true);
                    } else {
                        showError();
                    }
                });
            }
        });

        pane.getChildren().addAll(tempPane, buttonBox);
    }

    private void enableDrag(Pane tempPane, final Circle circle, Node node) {
        final Delta dragDelta = new Delta();
        final String[] source = new String[1];
        circle.setOnMousePressed(mouseEvent -> {
            if (!addNode.isSelected() && !addEdge.isSelected() && !deleteNode.isSelected() && !deleteEdge.isSelected()) {
                dragDelta.x = circle.getCenterX() - mouseEvent.getX();
                dragDelta.y = circle.getCenterY() - mouseEvent.getY();
                circle.getScene().setCursor(Cursor.MOVE);
            } else if (addEdge.isSelected()) {
                source[0] = node.getName();
                newEdge = new Line(mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getX(), mouseEvent.getY());
                endX = new SimpleDoubleProperty(mouseEvent.getX());
                endY = new SimpleDoubleProperty(mouseEvent.getY());
                newEdge.endXProperty().bind(endX);
                newEdge.endYProperty().bind(endY);
                tempPane.getChildren().add(newEdge);
            } else if (deleteNode.isSelected()) {
                graph.DeleteNode(node.getName());
                tempPane.getChildren().clear();
                getGraphPane(tempPane, true);
            }
        });

        circle.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                TextInputDialog dialog = new TextInputDialog(node.getName());
                dialog.setTitle("Update Node Name");
                dialog.setHeaderText("Enter new name for the node:");
                dialog.setContentText("New Name:");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(newName -> {
                    if (!newName.isEmpty() && graph.getNode(newName.toLowerCase()) == null) {
                        graph.renameNode(node.getName(), newName);
                        node.getText().setText(newName);
                        tempPane.getChildren().clear();
                        getGraphPane(tempPane, true);
                    } else {
                        showError();
                    }
                });
            }
        });

        circle.setOnMouseReleased(mouseEvent -> {
            if (addEdge.isSelected()) {
                endX.unbind();
                endY.unbind();

                TextInputDialog td = new TextInputDialog("");
                td.setHeaderText("Enter Weight");

                boolean validInput = false;
                double weight = 0;

                while (!validInput) {
                    Optional<String> result = td.showAndWait();
                    if (result.isPresent()) {
                        try {
                            weight = Double.parseDouble(result.get().trim());
                            validInput = true;
                        } catch (NumberFormatException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Invalid input");
                            alert.setHeaderText(null);
                            alert.setContentText("Please enter a valid real number.");
                            alert.showAndWait();
                        }
                    } else {
                        return;
                    }
                }

                for (Node node1 : graph.getNodes()) {
                    if (isInside(mouseEvent.getX(), mouseEvent.getY(), node1)) {
                        graph.addEdge(source[0], node1.getName(), weight);
                    }
                }

                tempPane.getChildren().clear();
                getGraphPane(tempPane, true);
            } else if (!deleteNode.isSelected()) {
                circle.getScene().setCursor(Cursor.HAND);
            }
        });

        circle.setOnMouseDragged(mouseEvent -> {
            if (!addNode.isSelected() && !addEdge.isSelected() && !deleteNode.isSelected() && !deleteEdge.isSelected()) {
                circle.setCenterX(mouseEvent.getX() + dragDelta.x);
                circle.setCenterY(mouseEvent.getY() + dragDelta.y);
                node.x = mouseEvent.getX() + dragDelta.x;
                node.y = mouseEvent.getY() + dragDelta.y;
            } else if (addEdge.isSelected()) {
                endX.set(mouseEvent.getX());
                endY.set(mouseEvent.getY());
            }
        });

        circle.setOnMouseEntered(mouseEvent -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                circle.getScene().setCursor(Cursor.HAND);
            }
        });

        circle.setOnMouseExited(mouseEvent -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                circle.getScene().setCursor(Cursor.DEFAULT);
            }
        });
    }

    private void enableEdgeDelete(Pane pane, final Line line, Edge edge) {
        line.setOnMouseReleased(mouseEvent -> {
            line.getScene().setCursor(Cursor.HAND);
            if (deleteEdge.isSelected()) {
                graph.DeleteEdge(edge.getSource().getName(), edge.getDestination().getName());
                pane.getChildren().clear();
                getGraphPane(pane, true);
            }
        });

        line.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                boolean validInput = false;
                while (!validInput) {
                    TextInputDialog dialog = new TextInputDialog(String.valueOf(edge.getWeight()));
                    dialog.setTitle("Update Edge Weight");
                    dialog.setHeaderText("Enter new weight for the edge:");
                    dialog.setContentText("New Weight:");

                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        try {
                            double weight = Double.parseDouble(result.get());
                            edge.setWeight(weight);
                            edge.getText().setText(String.valueOf(weight));
                            pane.getChildren().clear();
                            getGraphPane(pane, true);
                            validInput = true; // Đầu vào hợp lệ
                        } catch (NumberFormatException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Invalid input");
                            alert.setHeaderText(null);
                            alert.setContentText("Please enter a valid real number.");
                            alert.showAndWait();
                        }
                    } else {
                        validInput = true;
                    }
                }
            }
        });

        line.setOnMouseEntered(mouseEvent -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                line.getScene().setCursor(Cursor.HAND);
            }
        });

        line.setOnMouseExited(mouseEvent -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                line.getScene().setCursor(Cursor.DEFAULT);
            }
        });
    }

    private boolean isInside(double x1, double y1, Node node) {
        double distance = Math.sqrt((x1 - node.getNodeX()) * (x1 - node.getNodeX()) + (y1 - node.getNodeY()) * (y1 - node.getNodeY()));
        return distance < 10;
    }

    private static class Delta {
        double x, y;
    }

    private void showError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Invalid or duplicate name. Please try again.");
        alert.showAndWait();
    }

    void getGraphPane(Pane pane, boolean enableTouch) {
        ArrayList<Edge> edgeArrayList = new ArrayList<>();
        graph.getGa().copyEdge(edgeArrayList);

        for (Edge e : edgeArrayList) {
            pane.getChildren().addAll(e.getLine(), e.getText());
            if (enableTouch) {
                enableEdgeDelete(pane, e.getLine(), e);
            }
        }

        for (Node node : graph.getNodes()) {
            pane.getChildren().add(node.getCircle(1.0));
            pane.getChildren().add(node.getText());
            if (enableTouch) {
                enableDrag(pane, node.getCircle(1.0), node);
            }
        }
    }
    void animatePathDijkstra(Pane pane,String from,String to,String shape){
        Node fromNode = graph.getNode(from);
        Path path = new Path();
        path.getElements().add(new MoveTo(fromNode.getNodeX(),fromNode.getNodeY()));
        Stack<Node> nodeStack = graph.getNodePathDijkstra(from, to);
        if(nodeStack.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setContentText("No Path Exist");
            alert.initOwner(pane.getScene().getWindow());
            alert.show();
        }
        else {
            while (!nodeStack.isEmpty()) {
                Node node = nodeStack.pop();
                path.getElements().add(new LineTo(node.getNodeX(), node.getNodeY()));
            }
            graph.getAdj().resetNodesVisited();
            Shape shape1, shape2, area;
            PathTransition pathTransition = new PathTransition();
            switch (shape) {
                case "Circle":
                    shape1 = new Circle(fromNode.getNodeX(), fromNode.getNodeY(), 6, Color.TOMATO);
                    pane.getChildren().add(shape1);
                    pathTransition.setNode(shape1);
                    break;
                case "Square":
                    shape1 = new Rectangle(0, 0, 10, 10);
                    shape1.setFill(Color.DARKVIOLET);
                    pane.getChildren().add(shape1);
                    pathTransition.setNode(shape1);
                    break;
                case "Plus":
                    shape1 = new Line(5, 10, 15, 10);
                    shape2 = new Line(10, 5, 10, 15);
                    shape1.setStrokeWidth(5);
                    shape2.setStrokeWidth(5);
                    area = Shape.union(shape1, shape2);
                    area.setFill(Color.ROYALBLUE);
                    pane.getChildren().add(area);
                    pathTransition.setNode(area);
                    break;
                case "Cross":
                    shape1 = new Line(5, 5, 15, 15);
                    shape2 = new Line(5, 15, 15, 5);
                    shape1.setStrokeWidth(5);
                    shape2.setStrokeWidth(5);
                    area = Shape.union(shape1, shape2);
                    area.setFill(Color.DARKCYAN);
                    pane.getChildren().add(area);
                    pathTransition.setNode(area);
                    break;
                case "Triangle":
                    shape1 = new Polygon();
                    ((Polygon) shape1).getPoints().addAll(0.0, 10.0, 20.0, 10.0, 10.0, 20.0);
                    shape1.setFill(Color.GOLD);
                    pane.getChildren().add(shape1);
                    pathTransition.setNode(shape1);
                    break;
            }
            pathTransition.setDuration(Duration.seconds(2));
            pathTransition.setPath(path);
            pathTransition.setCycleCount(PathTransition.INDEFINITE);
            pathTransition.play();
        }
    }
    void animatePathBellmanFord(Pane pane,String from,String to,String shape){
        Node fromNode = graph.getNode(from);
        Path path = new Path();
        path.getElements().add(new MoveTo(fromNode.getNodeX(),fromNode.getNodeY()));
        Stack<Node> nodeStack1 = graph.getNodePathbellmanford(from, to);
//        while (!graph.getNodePathbellmanford(from, to).isEmpty()) {
//            Node node = graph.getNodePathbellmanford(from, to).pop();
//            System.out.print(node.getName() + " "); // In ra tên của node
//        }
        if(nodeStack1.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setContentText("No Path Exist");
            alert.initOwner(pane.getScene().getWindow());
            alert.show();
        }
        else {
            while (!nodeStack1.isEmpty()) {
                Node node = nodeStack1.pop();
                path.getElements().add(new LineTo(node.getNodeX(), node.getNodeY()));
            }
            graph.getBf().resetNodesVisited();
            Shape shape1, shape2, area;
            PathTransition pathTransition = new PathTransition();
            switch (shape) {
                case "Circle":
                    shape1 = new Circle(fromNode.getNodeX(), fromNode.getNodeY(), 6, Color.TOMATO);
                    pane.getChildren().add(shape1);
                    pathTransition.setNode(shape1);
                    break;
                case "Square":
                    shape1 = new Rectangle(0, 0, 10, 10);
                    shape1.setFill(Color.DARKVIOLET);
                    pane.getChildren().add(shape1);
                    pathTransition.setNode(shape1);
                    break;
                case "Plus":
                    shape1 = new Line(5, 10, 15, 10);
                    shape2 = new Line(10, 5, 10, 15);
                    shape1.setStrokeWidth(5);
                    shape2.setStrokeWidth(5);
                    area = Shape.union(shape1, shape2);
                    area.setFill(Color.ROYALBLUE);
                    pane.getChildren().add(area);
                    pathTransition.setNode(area);
                    break;
                case "Cross":
                    shape1 = new Line(5, 5, 15, 15);
                    shape2 = new Line(5, 15, 15, 5);
                    shape1.setStrokeWidth(5);
                    shape2.setStrokeWidth(5);
                    area = Shape.union(shape1, shape2);
                    area.setFill(Color.DARKCYAN);
                    pane.getChildren().add(area);
                    pathTransition.setNode(area);
                    break;
                case "Triangle":
                    shape1 = new Polygon();
                    ((Polygon) shape1).getPoints().addAll(0.0, 10.0, 20.0, 10.0, 10.0, 20.0);
                    shape1.setFill(Color.GOLD);
                    pane.getChildren().add(shape1);
                    pathTransition.setNode(shape1);
                    break;
            }
            pathTransition.setDuration(Duration.seconds(2));
            pathTransition.setPath(path);
            pathTransition.setCycleCount(PathTransition.INDEFINITE);
            pathTransition.play();
        }
    }
    void animatePathFlooding(Pane pane,String from,String to,String shape){
        Node fromNode = graph.getNode(from);
        Path path = new Path();
        path.getElements().add(new MoveTo(fromNode.getNodeX(),fromNode.getNodeY()));
        Stack<Node> nodeStack = graph.getNodePathflooding(from, to);
        if(nodeStack.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setContentText("No Path Exist");
            alert.initOwner(pane.getScene().getWindow());
            alert.show();
        }
        else {
            while (!nodeStack.isEmpty()) {
                Node node = nodeStack.pop();
                path.getElements().add(new LineTo(node.getNodeX(), node.getNodeY()));
            }
            graph.getFl().resetNodesVisited();
            Shape shape1, shape2, area;
            PathTransition pathTransition = new PathTransition();
            switch (shape) {
                case "Circle":
                    shape1 = new Circle(fromNode.getNodeX(), fromNode.getNodeY(), 6, Color.TOMATO);
                    pane.getChildren().add(shape1);
                    pathTransition.setNode(shape1);
                    break;
                case "Square":
                    shape1 = new Rectangle(0, 0, 10, 10);
                    shape1.setFill(Color.DARKVIOLET);
                    pane.getChildren().add(shape1);
                    pathTransition.setNode(shape1);
                    break;
                case "Plus":
                    shape1 = new Line(5, 10, 15, 10);
                    shape2 = new Line(10, 5, 10, 15);
                    shape1.setStrokeWidth(5);
                    shape2.setStrokeWidth(5);
                    area = Shape.union(shape1, shape2);
                    area.setFill(Color.ROYALBLUE);
                    pane.getChildren().add(area);
                    pathTransition.setNode(area);
                    break;
                case "Cross":
                    shape1 = new Line(5, 5, 15, 15);
                    shape2 = new Line(5, 15, 15, 5);
                    shape1.setStrokeWidth(5);
                    shape2.setStrokeWidth(5);
                    area = Shape.union(shape1, shape2);
                    area.setFill(Color.DARKCYAN);
                    pane.getChildren().add(area);
                    pathTransition.setNode(area);
                    break;
                case "Triangle":
                    shape1 = new Polygon();
                    ((Polygon) shape1).getPoints().addAll(0.0, 10.0, 20.0, 10.0, 10.0, 20.0);
                    shape1.setFill(Color.GOLD);
                    pane.getChildren().add(shape1);
                    pathTransition.setNode(shape1);
                    break;
            }
            pathTransition.setDuration(Duration.seconds(2));
            pathTransition.setPath(path);
            pathTransition.setCycleCount(PathTransition.INDEFINITE);
            pathTransition.play();
        }
    }
}