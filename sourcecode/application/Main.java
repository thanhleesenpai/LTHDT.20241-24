package application;

import application.UI.Gui;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;

import java.util.Objects;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Gui gui = new Gui();
        Pane root = new Pane();
        MenuBar mb = new MenuBar();
        mb.setPrefWidth(2160);

        GridPane EdgeUI = gui.createUI();

        primaryStage.getIcons().add(new Image((Objects.requireNonNull(getClass().getResourceAsStream("/application/UI/Router.png")))));

        Menu Graph = new Menu("Graph");
        Menu Algorithm = new Menu("Algorithm"); // New menu for algorithms
        Menu HelpQuit = new Menu("Options"); // New menu for Help and Quit

        MenuItem GetPath = new MenuItem("Get Path");
        MenuItem ShowGraph = new MenuItem("Show Graph");
        //MenuItem m13 = new MenuItem("Animate Path");

        MenuItem dijkstra = new MenuItem("Dijkstra Algorithm");
        MenuItem bellmanFord = new MenuItem("Bellman-Ford Algorithm");
        MenuItem flooding = new MenuItem("Flooding Algorithm");

        MenuItem help = new MenuItem("Help");
        MenuItem quit = new MenuItem("Quit");

        // Add items to menus
        Graph.getItems().addAll(GetPath, ShowGraph);
        Algorithm.getItems().addAll(dijkstra, bellmanFord, flooding);
        HelpQuit.getItems().addAll(help, quit);

        // Add menus to MenuBar
        mb.getMenus().addAll(Graph, Algorithm, HelpQuit);

        VBox vb = new VBox(mb);

        EventHandler<ActionEvent> event = e -> {
            Pane NodeUI = new Pane();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK, ButtonType.CANCEL);
            alert.setHeaderText(null); // Loại bỏ header
            ButtonType userSelection = null;

            if (e.getSource() == GetPath) {
                NodeUI = gui.createUI();
                gui.addSearchEdgeUI((GridPane) NodeUI, "Get Path");
            }
            if (e.getSource() == ShowGraph) {
                gui.GraphGui(NodeUI);
            }
            if (e.getSource() == dijkstra) {
                alert.setTitle("Dijkstra Algorithm Selected");
                alert.setContentText("You have selected the Dijkstra Algorithm.");
                userSelection = alert.showAndWait().orElse(ButtonType.CANCEL);

                if (userSelection == ButtonType.OK) {
                    gui.animateDijkstraGui(NodeUI); // Placeholder for Dijkstra logic
                }
            }
            if (e.getSource() == bellmanFord) {
                alert.setTitle("Bellman-Ford Algorithm Selected");
                alert.setContentText("You have selected the Bellman-Ford Algorithm.");
                userSelection = alert.showAndWait().orElse(ButtonType.CANCEL);

                if (userSelection == ButtonType.OK) {
                    gui.animateBellmanFordGui(NodeUI); // Placeholder for Bellman-Ford logic
                }
            }
            if (e.getSource() == flooding) {
                alert.setTitle("Flooding Algorithm Selected");
                alert.setContentText("You have selected the Flooding Algorithm.");
                userSelection = alert.showAndWait().orElse(ButtonType.CANCEL);

                if (userSelection == ButtonType.OK) {
                    gui.animateDijkstraGui(NodeUI); // Placeholder for Flooding logic
                }
            }

            // Cập nhật giao diện
            Group newRoot = new Group();
            newRoot.getChildren().addAll(NodeUI, vb);

            primaryStage.setTitle("Routing Visualization");
            primaryStage.setScene(new Scene(newRoot, 900, 600));
            primaryStage.show();
        };

        // Help functionality
        help.setOnAction(event1 -> {
            Alert helpAlert = new Alert(Alert.AlertType.INFORMATION);
            helpAlert.setTitle("Help Menu");
            helpAlert.setHeaderText("Routing Algorithms Help");
            helpAlert.setContentText("""
                    This application demonstrates routing algorithms and graph functionalities:
                    1. Get Path - Finds the path between two nodes.
                    2. Show Graph - Displays the network graph.

                    Algorithms:
                    - Dijkstra Algorithm: Finds the shortest path.
                    - Bellman-Ford Algorithm: Supports graphs with negative weights.
                    - Flooding Algorithm: Broadcasts packets to all neighbors.""");
            helpAlert.show();
        });

        // Quit functionality
        quit.setOnAction(event1 -> {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to quit?", ButtonType.YES, ButtonType.NO);
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    primaryStage.close();
                }
            });
        });

        // Handle close request
        primaryStage.setOnCloseRequest(event1 -> {
            event1.consume();
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to quit?", ButtonType.YES, ButtonType.NO);
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    primaryStage.close();
                }
            });
        });

        GetPath.setOnAction(event);
        ShowGraph.setOnAction(event);
        dijkstra.setOnAction(event);
        bellmanFord.setOnAction(event);
        flooding.setOnAction(event);

        root.getChildren().addAll(EdgeUI, vb);

        primaryStage.setTitle("Routing Visualization");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}