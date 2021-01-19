
public class sinkNode extends node {
    int num; // Node's number
    int positionX; // Node's x position on the scene
    int positionY; // Node's y position on the scene

    public sinkNode(int num, int x, int y) {
        super(num, x, y);
        this.num = num;
        this.positionX = x;
        this.positionY = y;
    }

}
