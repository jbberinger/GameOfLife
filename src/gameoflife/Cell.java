
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
        super.setStrokeWidth(0.1);
        super.setStrokeType(StrokeType.CENTERED);
        super.setStroke(Color.WHITE);
    }
    
    public void setIsAlive(boolean isAlive){
        this.isAlive = isAlive;
        if(isAlive){
            super.setFill(Color.WHITE);
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
