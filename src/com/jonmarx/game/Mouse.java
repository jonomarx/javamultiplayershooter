package com.jonmarx.game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Jon
 */
public class Mouse extends MouseAdapter {
    double x = 0;
    double y = 0;
    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
    boolean isClicked = false;
    @Override
    public void mousePressed(MouseEvent e) {
        isClicked = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isClicked = false;
    }
    
    public boolean isClicked() {
        return isClicked;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
