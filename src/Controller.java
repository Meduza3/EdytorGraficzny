import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;

import static java.lang.Math.*;

public class Controller {
    @FXML
    private Button circleButton;
    @FXML
    private Button rectangleButton;
    @FXML
    private Button polygonButton;
    @FXML
    private Button infoButton;
    @FXML
    private Button loadButton;
    @FXML
    private Button saveButton;
    @FXML
    private Text tooltip;
    @FXML
    private Text coords;
    @FXML
    private Canvas canvas;
    @FXML
    private Pane pane;

    @FXML
    private ColorPicker strokeColorPicker;
    @FXML
    private ColorPicker fillColorPicker;
    @FXML
    private Button switchColorButton;
    @FXML
    private int activeTool = 99;
    private boolean isDrawing = false;
    private boolean isPolygonDrawing = false;
    private double primaryPointX;
    private double primaryPointY;

    @FXML
    private void selectCircle(){
        activeTool = 0;
        updateTooltip();
    }

    @FXML
    private void selectRectangle(){
        activeTool = 1;
        updateTooltip();
    }

    @FXML
    private void selectPolygon(){
        activeTool = 2;
        updateTooltip();
    }

    @FXML
    private void selectEdit(){
        activeTool = 10;
        updateTooltip();
    }
    @FXML
    private void showInfo(){

    }

    @FXML
    private void save(){

    }

    @FXML
    private void load(){

    }

    @FXML
    private void updateTooltip(){
        if(activeTool == 0){
            tooltip.setText("Circle");
        } else if(activeTool == 1){
            tooltip.setText("Rectangle");
        } else if(activeTool == 2){
            tooltip.setText("Polygon");
        } else if(activeTool == 10){
            tooltip.setText("Edit tool");
        }else if(activeTool == 99){
            tooltip.setText("Select tool");
        }else {
            tooltip.setText("Error");
        }
    }

    @FXML
    private void updateCoords(MouseEvent e){
        coords.setText("(x,y) = (" + (int) e.getX() + "," + (int) e.getY() + ")");
        updateTooltip();
    }

    @FXML
    private void clipContents(){
        pane.setClip(new Rectangle(pane.getWidth(), pane.getHeight()));
    }
    @FXML
    private void draw2(MouseEvent e){
        if(isDrawing){
            switch(activeTool){
                case 0:
                    System.out.println("createCircle");
                    createCircle(e);
                    break;
                case 1:
                    System.out.println("createRectangle");
                    createRectangle(e);
                    break;
                case 2:
                    if(isPolygonDrawing == false) {
                        System.out.println("createPolygon");
                        createPolygon(e);
                    }
                    break;
                default:
            }
            isDrawing = false;
        } else {
            if(activeTool != 10){
                primaryPointX = e.getX();
                primaryPointY = e.getY();
                isDrawing = true;
            } else {
                Shape current = getLastClickedShape(pane, e.getX(), e.getY());
                if(e.getButton() == MouseButton.SECONDARY){
                    pane.getChildren().remove(current);
                } else if(e.getButton() == MouseButton.PRIMARY){
                    if(current instanceof ERectangle){
                        ERectangle currentRect = (ERectangle) current;
                        currentRect.moveCenter(e.getX(), e.getY());
                        currentRect.setStroke(strokeColorPicker.getValue());
                        currentRect.setFill(fillColorPicker.getValue());
                    } else if(current instanceof ECircle){
                        ECircle currentCircle = (ECircle) current;
                        currentCircle.moveCenter(e.getX(), e.getY());
                        currentCircle.setStroke(strokeColorPicker.getValue());
                        currentCircle.setFill(fillColorPicker.getValue());

                    }
                }
            }
        }
    }

    private void createPolygon(MouseEvent e) {

    }
    private Shape getLastClickedShape(Pane pane, double mouseX, double mouseY){
        Shape lastClicked = null;
        for (Node node : pane.getChildren()){
            if(node.contains(mouseX,mouseY)) {
                if (node instanceof Shape) lastClicked = (Shape) node;
            }
        }
        return lastClicked;
    }

    private void createRectangle(MouseEvent e){
        ERectangle rectangle = new ERectangle();
        double realPointX = e.getX();
        double realPointY = e.getY();

        if(e.getX() > primaryPointX && e.getY() < primaryPointY){
            realPointX = primaryPointX;
            realPointY = e.getY();
        } else if(e.getX() > primaryPointX && e.getY() > primaryPointY){
            realPointX = primaryPointX;
            realPointY = primaryPointY;
        } else if(e.getX() < primaryPointX && e.getY() < primaryPointY){
            realPointX = e.getX();
            realPointY = e.getY();
        } else if(e.getX() < primaryPointX && e.getY() > primaryPointY){
            realPointX = e.getX();
            realPointY = primaryPointY;
        }

        rectangle.setStroke(strokeColorPicker.getValue());
        rectangle.setFill(fillColorPicker.getValue());

        double width = abs(primaryPointX - e.getX());
        double height = abs(primaryPointY - e.getY());

        rectangle.setX(realPointX);
        rectangle.setY(realPointY);
        rectangle.setWidth(width);
        rectangle.setHeight(height);

        pane.getChildren().add(rectangle);
    }
    private void createCircle(MouseEvent e){
        ECircle circle = new ECircle();
        circle.setCenterX(primaryPointX);
        circle.setCenterY(primaryPointY);

        double radius = (sqrt(((primaryPointY - e.getY()) * (primaryPointY - e.getY()) + (primaryPointX - e.getX()) * (primaryPointX - e.getX()))));

        circle.setFill(fillColorPicker.getValue());
        circle.setStroke(strokeColorPicker.getValue());

        circle.setRadius(radius);
        pane.getChildren().add(circle);
    }

    @FXML
    private void switchColor(){
        Color temp = fillColorPicker.getValue();
        fillColorPicker.setValue(strokeColorPicker.getValue());
        strokeColorPicker.setValue(temp);
    }
}
