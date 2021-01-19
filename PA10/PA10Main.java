import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Benhur
 *         This program displays an animation of marbles moving through a
 *         kinetic sculpture.
 */
public class PA10Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * This function creates a textfield with an eventHandler button
     * for the user to input a file.
     * After the button is clicked, a new Scene pops up with the animation
     * with appropriate print statements within the console.
     */
    public void start(Stage primaryStage) {
        BorderPane panel = new BorderPane();
        TextField file = new TextField();
        Button button = new Button("Process");
        button.setOnAction(new HandleTextInput(file));
        final int num_items = 2;
        HBox input_box = new HBox(num_items);
        input_box.getChildren().add(file);
        input_box.getChildren().add(button);
        panel.setBottom(input_box);
        button.setOnAction(new HandleTextInput(file));
        primaryStage.setTitle("PA10");
        primaryStage.setScene(new Scene(panel));
        primaryStage.show();
    }
    class HandleTextInput implements EventHandler<ActionEvent> {
        private JavaFXView view;
        private TextField file;
        public int size = 40; // Node size
        public int Xmax = 0; // Scene width
        public int Ymax = 0; // Scene height
        public int num = 0; // Node num
        public int step = 0; // Timeline switch
        public double delay = 1;
        public String[] marbles; // colors
        public List<node> nodeList = new ArrayList<node>();
        // Ex key=input, value=[(20,20)]
        public HashMap<String, List<String>> nodes = new HashMap<String, List<String>>();
        // The connection between nodes
        public HashMap<Integer, List<Integer>> commands = new HashMap<Integer, List<Integer>>();
        public HandleTextInput(TextField file) {
            this.file = file;
        }
        public void handle(ActionEvent event) {
            try {
                Scanner input = new Scanner(new File(file.getText()));
                while (input.hasNextLine()) {
                    String line = input.nextLine();
                    if (line.startsWith("delay: ")) {
                        delay = Double.parseDouble(line.substring(6));
                    } else {
                        info information = new info(line);
                        // Returns the appropriate data
                        marbles = information.getColors();
                        commands = information.getCommands();
                        nodes = information.getNodes();
                        Xmax = information.getX();
                        Ymax = information.getY();
                    }
                }
                scene(); // Creates the new scene
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        private void scene() {
            Stage p = new Stage();
            view = new JavaFXView(Xmax + 50, Ymax + 50);
            // Creates the appropriate node besed on it's key
            for (String key : nodes.keySet()) {
                if (key.equals("input")) {
                    coord(key, num, 0);
                    num++;
                } else if (key.equals("passthrough")) {
                    for (int i = 0; i < nodes.get(key).size(); i++) {
                        coord(key, num, i);
                        num++;
                    }
                }
            }
            coord("sink", num, 0);
            // Creates the lines between the appropriate nodes
            for (Integer order : commands.keySet()) {
                for (int j = 0; j < nodeList.size(); j++) {
                    if (order == nodeList.get(j).num) {
                        int startX = nodeList.get(j).positionX;
                        int startY = nodeList.get(j).positionY;
                        for (int k = 0; k < commands.get(order).size(); k++) {
                            for (int l = 0; l < nodeList.size(); l++) {
                                if (commands.get(order)
                                        .get(k) == nodeList.get(l).num) {
                                    int endX = nodeList.get(l).positionX;
                                    int endY = nodeList.get(l).positionY;
                                    view.initEdge(order, nodeList.get(l).num,
                                            startX + size, startY + size / 2,
                                            endX, endY + size / 2);
                                }
                            }
                        }
                    }
                }
            }
            p.setTitle("Sculpture");
            p.setScene(new Scene(view.root));
            p.show();
            // Adds all the marbles to the input node's input list
            for (int m = 0; m < marbles.length; m++) {
                nodeList.get(0).setOrder(marbles[m].trim());
            }
            // Plays the animation
            animation();
        }

        /**
         * This function extracts the appropriate values from a list and
         * creates a specific node with it's appropriate data and adds it to the
         * scene.
         * 
         * @param key
         *            Ex{input, passthrough, sink}
         * @param num
         *            -- node's number
         * @param i
         *            -- the index in a list
         */
        private void coord(String key, int num, int i) {
            node node;
            String pos = nodes.get(key).get(i);
            int x = Integer.parseInt(pos.substring(1, pos.indexOf(",")).trim());
            int y = Integer.parseInt(pos
                    .substring(pos.indexOf(",") + 1, pos.indexOf(")")).trim());
            view.initNode(num, x, y, size);
            if (key.equals("input")) {
                node = new inputNode(num, x, y);
            } else if (key.equals("passthroguh")) {
                node = new passthroughNode(num, x, y);
            } else {
                node = new sinkNode(num, x, y);
            }
            nodeList.add(node);
        }

        /**
         * In this function, there are two parts.
         * Part 1: Checks to see if a marble has reached the sink node
         * and prints out the appropriate data of that marble.
         * Then it clears the scene of all marbles
         * Lastly, it takes a node's input list and transfers
         * all the elements in the list to its output list.
         * 
         * Part 2: Takes a node's output list and transfers all its data
         * to the every connected node's input list.
         * And displays it on the scene.
         */
        private void animation() {
            final Timeline timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            final KeyFrame kf = new KeyFrame(Duration.seconds(delay),
                    (ActionEvent e) -> {
                        switch (step) {
                        case 0:
                            List<String> lastNode = nodeList
                                    .get(nodeList.size() - 1).getOrder();
                            if (!lastNode.isEmpty()) {
                                for (int h = lastNode.size() - 1; h >= 0; h--) {
                                    Color color = Color
                                            .valueOf(lastNode.get(h));
                                    System.out.println(
                                            "sink output = Circle[centerX=0.0, centerY=0.0, radius=10.0, fill="
                                                    + color + "]");
                                    nodeList.get(nodeList.size() - 1).getOrder()
                                            .remove(h);
                                }
                            }
                            for (int i = 0; i < nodeList.size(); i++) {
                                for (int j = 0; j < nodeList.size(); j++) {
                                    view.clearEdge(i, j);
                                }
                            }
                            for (int k = 0; k < nodeList.size(); k++) {
                                if (!nodeList.get(k).getOrder().isEmpty()) {
                                    String marble = nodeList.get(k).getOrder()
                                            .remove(0);
                                    nodeList.get(k).setOrderOut(marble);
                                }
                            }
                            step++;
                            break;
                        case 1:
                            for (int i = 0; i < nodeList.size(); i++) {
                                if (!nodeList.get(i).getOrderOut().isEmpty()) {
                                    String marble = nodeList.get(i)
                                            .getOrderOut().remove(0);
                                    for (Integer j : commands.keySet()) {
                                        if (nodeList.get(i).num == j) {
                                            for (int k = 0; k < commands.get(j)
                                                    .size(); k++) {
                                                int number = commands.get(j)
                                                        .get(k);
                                                for (int l = 0; l < nodeList.size(); l++) {
                                                    if (nodeList.get(
                                                            l).num == number) {
                                                        nodeList.get(l)
                                                                .setOrder(
                                                                        marble);
                                                        view.edgeTransition(i,
                                                                l,
                                                                marble,
                                                                delay);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            step--;
                            break;
                        }
                    });
            timeline.getKeyFrames().add(kf);
            timeline.play();
        }
    }
}