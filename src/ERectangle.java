import javafx.scene.shape.*;
public class ERectangle extends Rectangle {
    private void moveCorner(double newX, double newY){
        setX(newX);
        setY(newY);
    }

    private void resizeRect(double newW, double newH){
        setWidth(newW);
        setHeight(newH);
    }
}
