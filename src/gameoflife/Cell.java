
package gameoflife;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 *
 * @author Justin Beringer
 */
public class Cell extends Rectangle{
    
    private boolean isAlive = false;

    public Cell(double width, double height) {
        super(width, height);
    }
    
    public Cell(double x, double y, double width, double height) {
        super(x, y, width, height);
        super.setStrokeWidth(0.25);
        //super.setStrokeType(StrokeType.INSIDE);
        super.setStroke(Color.rgb(130, 130, 130));
        super.setSmooth(true);
    }
    
    public void setIsAlive(boolean isAlive){
        this.isAlive = isAlive;
        if(isAlive){
            super.setFill(Color.rgb(250, 250, 250));
        }else{
            super.setFill(Color.BLACK);
        }
        
    }
    
    public boolean getIsAlive(){
        return isAlive;
    }

    void onMouseClicked(EventHandler<MouseEvent> eventHandler) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
