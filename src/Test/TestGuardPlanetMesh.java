/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import AI.PathFinding.GraphA;
import GameLogic.Planet;
import GameLogic.Pursuer;
import GameLogic.Settings;
import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giogio
 */
public class TestGuardPlanetMesh extends SimpleApplication{
    int i;
     GraphA graph;
    Pursuer pursuer, pursuer2, pursuer3, pursuer4;
    Planet planet;
 public static void main(String[] args) {
        TestGuardPlanetMesh app = new TestGuardPlanetMesh();
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
         planet = new Planet(assetManager, rootNode, new Settings());
         rootNode.attachChild(planet.getPlanet());
         rootNode.attachChild(planet.getNavMesh());
         graph = new GraphA(planet.getNavMesh().getMesh());
        pursuer = new Pursuer(assetManager);
        pursuer.setCurrentVertex(graph.getVertices().get(1100));
        graph.getVertices().get(1100).incrementTimeNeighbour();
        pursuer.setPosition(graph.getVertices().get(1100).getTriangle().getCenter());
        rootNode.attachChild(pursuer.getNodeAgent());
        
        pursuer2 = new Pursuer(assetManager);
        pursuer2.setCurrentVertex(graph.getVertices().get(1110));
        graph.getVertices().get(1110).incrementTimeNeighbour();
        pursuer2.setPosition(graph.getVertices().get(1110).getTriangle().getCenter());
        rootNode.attachChild(pursuer2.getNodeAgent());
        
        pursuer3 = new Pursuer(assetManager);
        pursuer3.setCurrentVertex(graph.getVertices().get(1120));
        graph.getVertices().get(1120).incrementTimeNeighbour();
        pursuer3.setPosition(graph.getVertices().get(1120).getTriangle().getCenter());
        rootNode.attachChild(pursuer3.getNodeAgent());
        
        pursuer4 = new Pursuer(assetManager);
        pursuer4.setCurrentVertex(graph.getVertices().get(1140));
        graph.getVertices().get(1140).incrementTimeNeighbour();
        pursuer4.setPosition(graph.getVertices().get(1140).getTriangle().getCenter());
        rootNode.attachChild(pursuer4.getNodeAgent());
        Thread one = new Thread() {
         public void run() {
             while(1==1){
                 
                graph.incrementTime();
             }
             
            }  
        };

        one.start();
        
     

  
    
    }
    
    @Override
    public void simpleUpdate(float ftp){
         pursuer.moveOnGrid(ftp, planet);
         pursuer2.moveOnGrid(ftp, planet);
         pursuer3.moveOnGrid(ftp, planet);
         pursuer4.moveOnGrid(ftp, planet);
    }
    
}
