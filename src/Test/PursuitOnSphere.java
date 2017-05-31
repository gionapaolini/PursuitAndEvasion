/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import GameLogic.Agent;
import GameLogic.Planet;
import GameLogic.Pursuer;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;

/**
 *
 * @author giogio
 */
public class PursuitOnSphere extends SimpleApplication {
    Pursuer agent1, agent2, agent3, agent4, target;
    Geometry planet, geen;
    Vector3f random1, random2;
    public static void main(String[] args) {
        PursuitOnSphere app = new PursuitOnSphere();
        AppSettings settings = new AppSettings(true);
        settings.setResolution(800,400);
        app.setShowSettings(false); // splashscreen
        app.setSettings(settings);
            
        app.start();
    }

     @Override
    public void simpleInitApp() {
       flyCam.setMoveSpeed(20f);
       flyCam.setDragToRotate(true);
       cam.setLocation(new Vector3f(0,100,100));
       cam.lookAt(new Vector3f(0,0,0), Vector3f.UNIT_Y);
        Material red = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        red.setColor("Color", ColorRGBA.Red);
        Material blue = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blue.setColor("Color", ColorRGBA.Blue);
        Material green = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        green.setColor("Color", ColorRGBA.Green);
       Sphere world = new Sphere(50,50,50);
       planet = new Geometry("planet",world);
       planet.setMaterial(red);
       
       
       Sphere agentGreen = new Sphere(10,10,1.1f);
       geen = new Geometry("ff",agentGreen);
       geen.setMaterial(green);
       
       agent1 = new Pursuer(assetManager);
       agent1.setPosition(new Vector3f(0,55,0));
       agent1.setCurrentVel(new Vector3f(0,0,0));
       
       
       agent2 = new Pursuer(assetManager);
       agent2.setPosition(new Vector3f(10,200,466));
       agent2.setCurrentVel(new Vector3f(0,0,0));
       
       
       agent3 = new Pursuer(assetManager);
       agent3.setPosition(new Vector3f(132,0,-55));
       agent3.setCurrentVel(new Vector3f(0.1f,0,0));
       
        
       agent4 = new Pursuer(assetManager);
       agent4.setPosition(new Vector3f(132,-555,-55));
       agent4.setCurrentVel(new Vector3f(0.1f,0,0));
       target = agent2;
       
       rootNode.attachChild(agent1.getNodeAgent());
       rootNode.attachChild(agent2.getNodeAgent());
      rootNode.attachChild(agent3.getNodeAgent());
       rootNode.attachChild(agent4.getNodeAgent());
       rootNode.attachChild(planet);
      rootNode.attachChild(geen);
       
    }
    
    @Override
    public void simpleUpdate(float tpf){
        
        agent1.setCurrentVel(agent1.getCurrentVel().add(agent1.pursue(agent4)));
        agent1.setPosition(agent1.getNodeAgent().getWorldTranslation().add(agent1.getCurrentVel()));
        reposition(agent1,planet); 
       
        
        agent2.setCurrentVel(agent2.getCurrentVel().add(agent2.pursue(agent4)));
        agent2.setPosition(agent2.getNodeAgent().getWorldTranslation().add(agent2.getCurrentVel()));
        reposition(agent2,planet); 
      

        agent3.setCurrentVel(agent3.getCurrentVel().add(agent3.pursue(agent4)));
        agent3.setPosition(agent3.getNodeAgent().getWorldTranslation().add(agent3.getCurrentVel()));
        reposition(agent3,planet); 
        
        agent4.setCurrentVel(agent4.getCurrentVel().add(agent4.evade(agent1)));
        agent4.setCurrentVel(agent4.getCurrentVel().add(agent4.evade(agent2)));
        agent4.setCurrentVel(agent4.getCurrentVel().add(agent4.evade(agent3)));

        agent4.setPosition(agent4.getNodeAgent().getWorldTranslation().add(agent4.getCurrentVel()));
        reposition(agent4,planet); 
        
        geen.setLocalTranslation(agent4.getNodeAgent().getLocalTranslation());
    }
    
     private void reposition(Agent agent,  Geometry planet){
        
       Ray ray = new Ray(planet.getLocalTranslation(),agent.getNodeAgent().getLocalTranslation());
        CollisionResults results = new CollisionResults();
        planet.collideWith(ray, results);
        Vector3f contactPoint = results.getFarthestCollision().getContactPoint();
        Vector3f normalPoint = results.getFarthestCollision().getContactNormal();
        float length = contactPoint.length();
        
        Vector3f newLocation = contactPoint.normalize().mult(length+1);
       agent.getNodeAgent().setLocalTranslation(newLocation);        
        
        Vector3f rotation = getProjectionOntoPlane(normalPoint,agent.getCurrentVel());
        
        float magnitude = agent.getCurrentVel().length();
        Quaternion rotationQuat = new Quaternion();
        rotationQuat.lookAt(rotation,normalPoint);
        agent.getNodeAgent().setLocalRotation(rotationQuat);
        agent.setCurrentVel(agent.getNodeAgent().getLocalRotation().getRotationColumn(2).normalize().mult(magnitude));
        
    }
     
    private Vector3f getProjectionOntoPlane(Vector3f n, Vector3f v){
        
    
        Vector3f projection = n.cross(v.cross(n));
      
        System.out.println(projection);
        
        return projection;
    }
}
