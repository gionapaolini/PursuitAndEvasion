/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import GameLogic.Obstacle;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Ray;
import com.jme3.scene.shape.Line;


/**
 *
 * @author giogio
 */
public class TestMotion extends SimpleApplication{
    
    Geometry box;
    BoundingBox box1;
    Line line;
    Geometry agent;
    final int RADIUS = 1;
    
    
    public static void main(String[] args){
        TestMotion test = new TestMotion();
        test.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(20f);
        Geometry obstacle = makeBox("box", 1,1,1);
        this.box1 = new BoundingBox(new Vector3f(1,1,1),1,1,1);
        this.agent =  makeSphere("agent", 5,1,1);
        Line line = new Line(new Vector3f(10,0,1), new Vector3f(0,0,1));
        rootNode.attachChild(obstacle);
        rootNode.attachChild(agent);
        agent.setLocalTranslation(new Vector3f(-1,1,1));
        
    }
    
      public void simpleUpdate(float tpf) {
          agent.move(new Vector3f(0.0001f,0,0));
          collides();
            
        }

    
    protected Vector3f getDirection(Geometry geometry){
        return geometry.getLocalRotation().getRotationColumn(2);
    }
    
    
     protected Geometry makeBox(String name, float x, float y, float z) {
        Box sphere = new Box(1, 1, 1);
        Geometry box = new Geometry(name, sphere);
        box.setLocalTranslation(x, y, z);
        box.scale(0.5f);
        
         Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Blue);
        box.setMaterial(mat1);
        return box;
    }   
     
     
    protected Geometry makeSphere(String name, float x, float y, float z) {
        Sphere sphere = new Sphere(30, 30, 1);
        Geometry ball = new Geometry(name, sphere);
        ball.scale(0.05f);
        ball.setLocalTranslation(x, y, z);
         Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Red);
        ball.setMaterial(mat1);
      
        ball.lookAt(box.getLocalTranslation(), Vector3f.UNIT_Y);
        return ball;
    }
    
    public boolean collides(){
        //Spatial originPoint = agent.center();
        Vector3f vector = agent.getLocalTranslation().scaleAdd(1, new Vector3f(0,0,0));
       // Spatial endPoint = originPoint.move(vector);      
       if(box1.contains(vector)){
            System.out.println("AYY"+ agent.getLocalTranslation().x+" "+agent.getLocalTranslation().y+" "+agent.getLocalTranslation().z);
            return false;
        }
       //else System.out.println("OUT");
        
          return true;
        
    }
    
    public void findBoundries(){
       /* Vector3f origin = box.getLocalTranslation();
        Vector3f coordsUpLeft = new Vector3f((float)(origin.x-0.5), (float) (origin.y), (float)(origin.z-0.5));
        Vector3f coordsDownLeft = new Vector3f((float)(origin.x-0.5), (float)(origin.y), (float)(origin.z +0.5));
        Vector3f coordsUpRight = new Vector3f((float)(origin.x+0.5), (float) (origin.y), (float)(origin.z-0.5));
        Vector3f coordDownRight = new Vector3f((float)(origin.x+0.5), (float) (origin.y), (float)(origin.z+0.5));
        */
       
        
    }
    
    
    
}
