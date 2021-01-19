
public class inputNode extends node {
    int num; // Node's number
    int positionX; // Node's x position on the scene
    int positionY; // Node's y position on the scene

    public inputNode(int num, int x, int y) {
        super(num, x, y);
        this.num = num;
        this.positionX = x;
        this.positionY = y;
    }

}
