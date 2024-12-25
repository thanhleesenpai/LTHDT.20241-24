package application;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        Gui gui = new Gui();
        Pane root = new Pane();
        MenuBar mb = new MenuBar();
        mb.setPrefWidth(2160);

        GridPane EdgeUI = gui.createUI();

        Menu Graph = new Menu("Graph");
        Menu Algorithm = new Menu("Algorithm"); // New menu for algorithms
        Menu HelpQuit = new Menu("Options"); // New menu for Help and Quit

        MenuItem m9 = new MenuItem("Get Path");
        MenuItem m12 = new MenuItem("Show Graph");
        MenuItem m13 = new MenuItem("Animate Path");

        MenuItem dijkstra = new MenuItem("Dijkstra Algorithm");
        MenuItem bellmanFord = new MenuItem("Bellman-Ford Algorithm");
        MenuItem flooding = new MenuItem("Flooding Algorithm");

        MenuItem help = new MenuItem("Help");
        MenuItem quit = new MenuItem("Quit");

        // Add items to menus
        Graph.getItems().addAll(m9, m12, m13);
        Algorithm.getItems().addAll(dijkstra, bellmanFord, flooding);
        HelpQuit.getItems().addAll(help, quit);

        // Add menus to MenuBar
        mb.getMenus().addAll(Graph, Algorithm, HelpQuit);

        VBox vb = new VBox(mb);

        EventHandler<ActionEvent> event = e -> {
            Pane NodeUI = new Pane();
            if (e.getSource() == m9) {
                NodeUI = gui.createUI();
                gui.addSearchEdgeUI((GridPane) NodeUI, "Get Path");
            }
            if (e.getSource() == m12) {
                gui.GraphGui(NodeUI);
            }
            if (e.getSource() == m13) {
                gui.animateGui(NodeUI);
            }
            if (e.getSource() == dijkstra) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dijkstra Algorithm Selected", ButtonType.OK);
                alert.show();
                gui.animateGui(NodeUI); // Placeholder for Dijkstra logic
            }
            if (e.getSource() == bellmanFord) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Bellman-Ford Algorithm Selected", ButtonType.OK);
                alert.show();
                gui.animateGui(NodeUI); // Placeholder for Bellman-Ford logic
            }
            if (e.getSource() == flooding) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Flooding Algorithm Selected", ButtonType.OK);
                alert.show();
                gui.animateGui(NodeUI); // Placeholder for Flooding logic
            }

            Group newRoot = new Group();
            newRoot.getChildren().addAll(NodeUI, vb);

            primaryStage.setTitle("GRAPH");
            primaryStage.setScene(new Scene(newRoot, 620, 500));
            primaryStage.show();
        };

        // Help functionality
        help.setOnAction(e -> {
            Alert helpAlert = new Alert(Alert.AlertType.INFORMATION);
            helpAlert.setTitle("Help Menu");
            helpAlert.setHeaderText("Routing Algorithms Help");
            helpAlert.setContentText("This application demonstrates routing algorithms and graph functionalities:\n"
                    + "1. Get Path - Finds the path between two nodes.\n"
                    + "2. Show Graph - Displays the network graph.\n"
                    + "3. Animate Path - Visualizes the selected path.\n\n"
                    + "Algorithms:\n"
                    + "- Dijkstra Algorithm: Finds the shortest path.\n"
                    + "- Bellman-Ford Algorithm: Supports graphs with negative weights.\n"
                    + "- Flooding Algorithm: Broadcasts packets to all neighbors.");
            helpAlert.show();
        });

        // Quit functionality
        quit.setOnAction(e -> {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to quit?", ButtonType.YES, ButtonType.NO);
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    primaryStage.close();
                }
            });
        });

        m9.setOnAction(event);
        m12.setOnAction(event);
        m13.setOnAction(event);
        dijkstra.setOnAction(event);
        bellmanFord.setOnAction(event);
        flooding.setOnAction(event);

        root.getChildren().addAll(EdgeUI, vb);

        primaryStage.setTitle("GRAPH");
        primaryStage.setScene(new Scene(root, 720, 480));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
