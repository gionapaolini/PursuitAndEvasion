/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import java.awt.Color;

/**
 *
 * @author giogio
 */
public class Power{
    
    private double change_in_size,
                   change_in_velocity,
                   duration;
           
    private boolean invisibility;
    
    private Color color;
    
    public void affect(Agent agent){
        agent.addPower(this);
    }
    
    
}
