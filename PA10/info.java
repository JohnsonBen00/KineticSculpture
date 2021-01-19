import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//This class reads the lines in the file and returns the appropriate data back to the main program
public class info {
    // Scene's dimensions
    public static int Xmax = 0;
    public static int Ymax = 0;
    public static String[] marbles; // An array of colors in String form
    // A map of node type to a list of their dimensions in the scene
    public static HashMap<String, List<String>> nodes = new HashMap<String, List<String>>();
    // A map of node to a list of nodes it's connected to
    public static HashMap<Integer, List<Integer>> commands = new HashMap<Integer, List<Integer>>();

    public info(String line) {
        if (line.startsWith("input: ")) {
            marbles = line.substring(7, line.length()).split(",");
        } else if (line.contains("->")) {
            command(line);
        } else if (line.contains("(")) {
            position(line);
        }
    }

    // Gets the connection of which node to which node and
    // adds it to the map
    public void command(String line) {
        int num = Integer.parseInt(line.substring(0, 1));
        int num2 = Integer.parseInt(line.substring(5));
        if (!commands.containsKey(num)) {
            List<Integer> node = new ArrayList<Integer>();
            node.add(num2);
            commands.put(num, node);
        } else {
            List<Integer> node = commands.get(num);
            node.add(num2);
            commands.put(num, node);
        }
    }

    // Gets the type of node and its dimension and adds it to the map, it type
    // already exists, appends the dimension to the list of dimensions
    public void position(String line) {
        String info = line.substring(3, line.indexOf(","));
        String data = line.substring(line.indexOf("("), line.length());
        int x = Integer.parseInt(data.substring(1, data.indexOf(",")).trim());
        int y = Integer.parseInt(data
                .substring(data.indexOf(",") + 1, data.indexOf(")")).trim());
        if (!nodes.containsKey(info)) {
            List<String> node = new ArrayList<String>();
            node.add(data);
            nodes.put(info, node);
        } else {
            List<String> node = nodes.get(info);
            node.add(data);
            nodes.put(info, node);
        }
        if (Xmax < x) {
            Xmax = x;
        }
        if (Ymax < y) {
            Ymax = y;
        }
    }

    // Returns the colors of every marble in an array
    public String[] getColors() {
        return marbles;
    }

    // Returns a map of node connections
    public HashMap<Integer, List<Integer>> getCommands() {
        return commands;
    }

    // Returns a map of node type to its dimensions
    public HashMap<String, List<String>> getNodes() {
        return nodes;
    }

    // Returns the scene's x dimension
    public int getX() {
        return Xmax;
    }

    // Returns the scene's y dimension
    public int getY() {
        return Ymax;
    }
}
