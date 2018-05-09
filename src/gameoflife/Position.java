/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflife;

/**
 *
 * @author jbber
 */
public class Position {

    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Position other = (Position) obj;
        if (this.x != other.x) {
            return false;
        }
        return this.y == other.y;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + this.x;
        hash = 11 * hash + this.y;
        return hash;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void clear() {
        setPosition(0, 0);
    }

}
