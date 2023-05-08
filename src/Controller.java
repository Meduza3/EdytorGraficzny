import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
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
    private ArrayList<Double> mouseClicks = new ArrayList<>();

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
    private void selectPaint(){
        activeTool = 10;
        updateTooltip();
    }

    @FXML
    private void selectMove(){
        activeTool = 11;
        updateTooltip();
    }

    @FXML
    private void selectRotate(){
       activeTool = 12;
       updateTooltip();
    }

    @FXML
    private void selectEyedropper(){
        activeTool = 20;
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
            tooltip.setText("Paint tool");
        } else if(activeTool == 11) {
            tooltip.setText("Move tool");
        } else if(activeTool == 12) {
            tooltip.setText("Rotate tool");
        } else if(activeTool == 20) {
            tooltip.setText("Eyedropper");
        } else if(activeTool == 99){
            tooltip.setText("Select tool");
        } else {
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
    private void draw(MouseEvent e){
        if(isDrawing){
            switch(activeTool){
                case 0:
                    System.out.println("createCircle");
                    createCircle(e);
                    isDrawing = false;
                    break;
                case 1:
                    System.out.println("createRectangle");
                    createRectangle(e);
                    isDrawing = false;
                    break;
                case 2:
                    if(isPolygonDrawing == false) {
                        System.out.println("createPolygon");
                        createPolygon(e);
                    }
                    break;
                default:
            }
        } else {
            if(!(activeTool == 10 || activeTool == 11 || activeTool == 12) || activeTool == 20){
                primaryPointX = e.getX();
                primaryPointY = e.getY();
                isDrawing = true;
            } else {
                Shape lastClicked = getLastClickedShape(pane, e.getX(), e.getY());
                switch(activeTool){
                    case 10:
                        System.out.println("paint");
                        lastClicked.setFill(fillColorPicker.getValue());
                        lastClicked.setStroke(strokeColorPicker.getValue());
                        break;
                    case 11:
                        System.out.println("move");
                        if(lastClicked instanceof ECircle) {
                            ECircle currentCircle = (ECircle) lastClicked;
                            currentCircle.moveCenter(e.getX(), e.getY());
                        } else if(lastClicked instanceof ERectangle){
                            ERectangle currentRectanlge = (ERectangle) lastClicked;
                            currentRectanlge.moveCenter(e.getX(), e.getY());
                        } else if(lastClicked instanceof Polygon){
                            //Polygon
                        }
                        break;
                    case 12:
                        System.out.println("rotate");
                        break;
                    case 20:
                        System.out.println("Eyedropper");
                        eyedropper(lastClicked);
                }
            }
        }
    }

    private void createPolygon(MouseEvent e) {
        if(e.getButton() == MouseButton.PRIMARY){
            mouseClicks.add(e.getX());
            mouseClicks.add(e.getY());
        } else if (e.getButton() == MouseButton.SECONDARY){
            System.out.println(mouseClicks.toString());
            ArrayList<Double> points = new ArrayList<Double>();
            points.add(primaryPointX);
            points.add(primaryPointY);
            points.addAll(mouseClicks);
            Polygon polygon = new Polygon(points.stream().mapToDouble(Double::doubleValue).toArray());
            polygon.setStroke(strokeColorPicker.getValue());
            polygon.setFill(fillColorPicker.getValue());
            addMouseScrolling(polygon);
            pane.getChildren().add(polygon);
            mouseClicks.clear();
            isDrawing = false;
        }
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
        addMouseScrolling(rectangle);
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
        addMouseScrolling(circle);
        pane.getChildren().add(circle);
    }
    @FXML
    private void switchColor(){
        Color temp = fillColorPicker.getValue();
        fillColorPicker.setValue(strokeColorPicker.getValue());
        strokeColorPicker.setValue(temp);
    }

    @FXML
    private void eyedropper(Shape shape){
        fillColorPicker.setValue((Color) shape.getFill());
        strokeColorPicker.setValue((Color) shape.getStroke());
    }

    public void addMouseScrolling(Node node) {
        node.setOnScroll((ScrollEvent event) -> {
            double zoomFactor = 1 + event.getDeltaY() / 100;
            node.setScaleX(node.getScaleX() * zoomFactor);
            node.setScaleY(node.getScaleY() * zoomFactor);
        });
    }
}