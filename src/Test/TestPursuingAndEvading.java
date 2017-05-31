/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import GameLogic.Pursuer;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;

/**
 *
 * @author giogio
 */
public class TestPursuingAndEvading extends SimpleApplication {
    
  
     Pursuer agent1, agent2, agent3, agent4, target;
    Geometry geen;
    Vector3f random1, random2;
    
    double mill;
    public static void main(String[] args) {
        TestPursuingAndEvading app = new TestPursuingAndEvading();
        AppSettings settings = new AppSettings(true);
        settings.setResolution(800,400);
        app.setShowSettings(false); // splashscreen
        app.setSettings(settings);
            
        app.start();
    }

    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);
        flyCam.setDragToRotate(true);
        cam.setLocation(new Vector3f(0,20,40));
      
       
        flyCam.setMoveSpeed(40f);
        Box cube = new Box(40,0.1f,40);
        Geometry floor = new Geometry("floor",cube);
        Material red = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        red.setColor("Color", ColorRGBA.Red);
        Material blue = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blue.setColor("Color", ColorRGBA.Blue);
        Material green = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        green.setColor("Color", ColorRGBA.Green);
        
        floor.setLocalTranslation(Vector3f.ZERO);
        floor.setMaterial(red);
        rootNode.attachChild(floor);
        
        
           
       Sphere agentGreen = new Sphere(10,10,1.1f);
       geen = new Geometry("ff",agentGreen);
       geen.setMaterial(green);
       
       agent1 = new Pursuer(assetManager);
       agent1.setPosition(new Vector3f(40,2,40));
       agent1.setCurrentVel(new Vector3f(0,0,0));
       
       
       agent2 = new Pursuer(assetManager);
       agent2.setPosition(new Vector3f(10,2,10));
       agent2.setCurrentVel(new Vector3f(0,0,0));
       
       
       agent3 = new Pursuer(assetManager);
       agent3.setPosition(new Vector3f(-40,2,-50));
       agent3.setCurrentVel(new Vector3f(0,0,0));
       
        
       agent4 = new Pursuer(assetManager);
       agent4.setPosition(new Vector3f(0,2,0));
       agent4.setCurrentVel(new Vector3f(0,0,0));
       target = agent2;
       
       rootNode.attachChild(agent1.getNodeAgent());
       rootNode.attachChild(agent2.getNodeAgent());
      rootNode.attachChild(agent3.getNodeAgent());
       rootNode.attachChild(agent4.getNodeAgent());
      rootNode.attachChild(geen);
        
    }
    
    @Override
    public void simpleUpdate(float ftp){
       
        agent1.setCurrentVel(agent1.getCurrentVel().add(agent1.pursue(agent2)));
        agent1.setCurrentVel(agent1.getCurrentVel().add(agent1.evade(agent3)));
        agent1.setCurrentVel(agent1.getCurrentVel().add(agent1.evade(agent4)));

        agent1.setPosition(agent1.getNodeAgent().getWorldTranslation().add(agent1.getCurrentVel()));
        
        
        
        agent2.setCurrentVel(agent2.getCurrentVel().add(agent2.pursue(agent3)));
        agent2.setCurrentVel(agent2.getCurrentVel().add(agent2.evade(agent1)));
        agent2.setCurrentVel(agent2.getCurrentVel().add(agent2.evade(agent4)));
        agent2.setPosition(agent2.getNodeAgent().getWorldTranslation().add(agent2.getCurrentVel()));


        agent3.setCurrentVel(agent3.getCurrentVel().add(agent3.pursue(agent4)));
        agent3.setCurrentVel(agent3.getCurrentVel().add(agent3.evade(agent1)));
        agent3.setCurrentVel(agent3.getCurrentVel().add(agent3.evade(agent2)));
        agent3.setPosition(agent3.getNodeAgent().getWorldTranslation().add(agent3.getCurrentVel()));
       
        agent4.setCurrentVel(agent4.getCurrentVel().add(agent4.pursue(agent1)));
        agent4.setCurrentVel(agent4.getCurrentVel().add(agent4.evade(agent2)));
        agent4.setCurrentVel(agent4.getCurrentVel().add(agent4.evade(agent3)));

        agent4.setPosition(agent4.getNodeAgent().getWorldTranslation().add(agent4.getCurrentVel()));
    
        
        geen.setLocalTranslation(agent4.getNodeAgent().getLocalTranslation());
    }
 
    
}
