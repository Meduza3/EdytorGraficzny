import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.Arrays;

public class EPolygon extends Polygon {

    EPolygon(double[] array){
        super();
    }
void moveCenter(double newX, double newY){
        double[] points = getPoints().stream().mapToDouble(Double::doubleValue).toArray();
        double[] newPoints = new double[points.length];
        for(int i = 0; i < points.length; i++){
            if(i % 2 == 0){
                newPoints[i] = points[i] + newX;
            } else {
                newPoints[i] = points[i] + newY;
            }
        }
        ArrayList<Double> newPointsArrayList = new ArrayList<>();
        //Add all elements of newPoints to newPointsArrayList
        Arrays.stream(newPoints).forEach(newPointsArrayList::add);
        getPoints().addAll(newPointsArrayList);
    }

}
