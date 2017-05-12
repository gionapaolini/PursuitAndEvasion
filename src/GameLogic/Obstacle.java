/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

/**
 *
 * @author carolley
 */
public class Obstacle {
    
    BoundingBox obstacle;
    
    public Obstacle(Vector3f c, int x, int y, int z){
       this.obstacle = new BoundingBox(c,x,y,z);
    }
    
    public boolean contains(Vector3f c){
        return obstacle.contains(c);
    }
}
