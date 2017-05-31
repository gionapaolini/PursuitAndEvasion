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
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
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
    
    Node[] bb;
    BitmapText[] times;
    BillboardControl[] control;
    
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
        pursuer.setCurrentVertex(graph.getVertices().get(247));
       // graph.getVertices().get(40).incrementTimeNeighbour(1.0f);
        pursuer.setPosition(graph.getVertices().get(247).getTriangle().getCenter());
        rootNode.attachChild(pursuer.getNodeAgent());
        
        pursuer2 = new Pursuer(assetManager);
        pursuer2.setCurrentVertex(graph.getVertices().get(100));
        graph.getVertices().get(100).incrementTimeNeighbour(1.0f);
        pursuer2.setPosition(graph.getVertices().get(100).getTriangle().getCenter());
        rootNode.attachChild(pursuer2.getNodeAgent());
        
        pursuer3 = new Pursuer(assetManager);
        pursuer3.setCurrentVertex(graph.getVertices().get(150));
        graph.getVertices().get(150).incrementTimeNeighbour(1.0f);
        pursuer3.setPosition(graph.getVertices().get(150).getTriangle().getCenter());
        rootNode.attachChild(pursuer3.getNodeAgent());
        
        pursuer4 = new Pursuer(assetManager);
        pursuer4.setCurrentVertex(graph.getVertices().get(30));
        graph.getVertices().get(30).incrementTimeNeighbour(1.0f);
        pursuer4.setPosition(graph.getVertices().get(30).getTriangle().getCenter());
        rootNode.attachChild(pursuer4.getNodeAgent());
        
        bb = new Node[graph.getVertices().size()];
        control = new BillboardControl[graph.getVertices().size()];
     
         
        BitmapFont newFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        times = new BitmapText[graph.getVertices().size()];
        for(int i=0;i<times.length;i++){
            
            times[i] = new BitmapText(newFont, false);
            times[i].setSize(1.5f);
            times[i].setText("0");
            times[i].setLocalTranslation(new Vector3f(0,0,0));
            control[i]=new BillboardControl();
            bb[i] = new Node("node"+i);
            bb[i].setLocalTranslation(graph.getVertices().get(i).getTriangle().getCenter());
            bb[i].addControl(control[i]);
            bb[i].attachChild(times[i]);
            rootNode.attachChild(bb[i]);
        }
 
     
        
        
 
       
        
        Thread one = new Thread() {
         public void run() {
             while(1==1){
                 
                 try {
                     Thread.sleep(1000);
                 } catch (InterruptedException ex) {
                     Logger.getLogger(TestGuardPlanetMesh.class.getName()).log(Level.SEVERE, null, ex);
                 }
              
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
         for(int i=0;i<times.length;i++){
          
            times[i].setText(""+graph.getVertices().get(i).getTime());
           // times[i].setText(""+graph.getVertices().get(i).getTriangle().getCenter());
          
        }
    }
    
}
