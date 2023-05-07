import javafx.scene.paint.Color;
import javafx.scene.shape.*;
public class ERectangle extends Rectangle {
    void moveCenter(double newX, double newY){
        setX(newX - getWidth()/2);
        setY(newY - getHeight()/2);
    }

    private void resizeRect(double newW, double newH){
        setWidth(newW);
        setHeight(newH);
    }
}
