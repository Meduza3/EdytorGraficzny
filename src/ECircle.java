import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class ECircle extends Circle {
    void moveCenter(double newX, double newY){
        this.setCenterX(newX);
        this.setCenterY(newY);
    }

    void resize(double newR){
        this.setRadius(newR);
    }
}
