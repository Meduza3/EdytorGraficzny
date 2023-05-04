import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import static java.lang.Math.abs;

public class Controller {
    @FXML
    private Button okragButton;
    @FXML
    private Button prostokatButton;
    @FXML
    private Button wielokatButton;
    @FXML
    private Button infoButton;
    @FXML
    private Button wczytajButton;
    @FXML
    private Button zapiszButton;
    @FXML
    private Text tooltip;
    @FXML
    private Text coords;
    @FXML
    private Canvas canvas;
    @FXML
    private ColorPicker strokeColorPicker;
    @FXML
    private ColorPicker fillColorPicker;
    @FXML
    private int activeTool = 1;
    private boolean isDrawing = false;
    private boolean primaryClickExists = false;
    private double primaryPointX;
    private double primaryPointY;

    @FXML
    private void selectOkrag(){
        activeTool = 0;
        updateTooltip();
    }

    @FXML
    private void selectProstokat(){
        activeTool = 1;
        updateTooltip();
    }

    @FXML
    private void selectWielokat(){
        activeTool = 2;
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
            tooltip.setText("Okrąg");
        } else if(activeTool == 1){
            tooltip.setText("Prostokąt");
        } else if(activeTool == 2){
            tooltip.setText("Wielokąt");
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
    private void draw(MouseEvent e) {

        if (isDrawing == false){
            // System.out.println("not drawing yet!");
            isDrawing = true;
            GraphicsContext gc = canvas.getGraphicsContext2D();
            primaryPointX = e.getX();
            primaryPointY = e.getY();
        } else {
            // System.out.println("Drawing now");
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setStroke(strokeColorPicker.getValue());
            gc.setLineWidth(5);
            gc.setFill(fillColorPicker.getValue());
            gc.beginPath();
            if(activeTool == 0){
                drawOkrag(gc, e);
                System.out.println("drawOkrąg");
            } else if(activeTool == 1){
                drawProstokat(gc, e);
                System.out.println("DrawProstokąt");
            } else if(activeTool == 2){
                drawWielokat(gc, e);
                System.out.println("DrawWielokat");
            } else {
                System.out.println("zbyt duży activeTool");
            }
        }
    }
    @FXML
    private void drawOkrag(GraphicsContext gc, MouseEvent e){
        double weed = abs(primaryPointX - e.getX());
        double height = abs(primaryPointY - e.getY());

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

        gc.strokeOval(realPointX, realPointY, weed, height);
        gc.fillOval(realPointX, realPointY, weed, height);

        gc.stroke();
        gc.fill();

        isDrawing = false;
    }

    @FXML
    private void drawProstokat(GraphicsContext gc, MouseEvent e){
        double weed = abs(primaryPointX - e.getX());
        double height = abs(primaryPointY - e.getY());

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


        gc.rect(realPointX, realPointY, weed, height);

        gc.stroke();
        gc.fill();

        isDrawing = false;
    }

    @FXML
    private void drawWielokat(GraphicsContext gc, MouseEvent e){
        isDrawing = false;

    }
}
