import java.util.ArrayList;
import java.util.List;

public class node {
    int num; // Node's number
    int positionX; // Node's x position on the scene
    int positionY; // Node's y position on the scene
    List<String> order = new ArrayList<String>(); // Node's input list
    List<String> orderOut = new ArrayList<String>(); // Node's output list

    public node(int num, int x, int y) {
        this.num = num;
        this.positionX = x;
        this.positionY = y;
    }

    // Adds a marble to the input list
    public void setOrder(String marble) {
        order.add(marble);
    }

    // Adds a marble to the output list
    public void setOrderOut(String marble) {
        orderOut.add(marble);
    }

    // Returns the input list
    public List<String> getOrder() {
        return order;
    }

    // Returns the output list
    public List<String> getOrderOut() {
        return orderOut;
    }

    // Returns the node's number
    public int getNum() {
        return num;
    }

    // Returns the node's x position on the scene
    public int getX() {
        return positionX;
    }

    // Returns the node's y position on the scene
    public int getY() {
        return positionY;
    }
}
