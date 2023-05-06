import javafx.scene.shape.Circle;

public class ECircle extends Circle {
    private void moveCenter(double newX, double newY){
        this.setCenterX(newX);
        this.setCenterY(newY);
    }

    private void resize(double newR){
        this.setRadius(newR);
    }
}
