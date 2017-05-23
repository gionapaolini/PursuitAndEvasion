/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import AI.PathFinding.GraphA;
import AI.PathFinding.Vertex;
import GameLogic.mainClass;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import java.util.ArrayList;

/**
 *
 * @author giogio
 */
public class TestPlanetMesh extends SimpleApplication{
    
    Geometry sphereMesh;
    
    
     public static void main(String[] args) {
        TestPlanetMesh app = new TestPlanetMesh();
        AppSettings settings = new AppSettings(true);
        settings.setResolution(500, 500);
        app.setShowSettings(false); // splashscreen
        app.setSettings(settings);
        app.start();
    }
    
    
     @Override
    public void simpleInitApp() {
         cam.setLocation(new Vector3f(20,20,20));
        cam.lookAt(new Vector3f(0,0,0), Vector3f.UNIT_Y);
        sphereMesh = makeSphere(assetManager,"sphereMesh",0,0,0);
        int l = sphereMesh.getTriangleCount();
        Triangle[] triangles = new Triangle[l];
        for(int i=0;i<l;i++){
            triangles[i] = new Triangle();
            sphereMesh.getMesh().getTriangle(i, triangles[i]);
            //System.out.println(triangles[i].get1()+", "+triangles[i].get2()+", "+triangles[i].get3());
        }
        GraphA graph = new GraphA(triangles);
        rootNode.attachChild(sphereMesh);
        System.out.println(graph.getVertices().size());
        System.out.println(sphereMesh.getTriangleCount());
        
        colorNeighbours(graph.getVertices().get(10));
        colorNeighbours(graph.getVertices().get(50));
        colorNeighbours(graph.getVertices().get(102));
        colorNeighbours(graph.getVertices().get(222));
        
        
        
    }
    
    
    
    
    
    
    
     protected Geometry makeSphere(AssetManager assetManager,String name, float x, float y, float z) {
        Sphere sphere = new Sphere(12, 12, 10);
        Geometry ball = new Geometry(name, sphere);
       // ball.scale(0.05f);
        ball.setLocalTranslation(x, y, z);
         Material matWireframe = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matWireframe.setColor("Color", ColorRGBA.Green);
        matWireframe.getAdditionalRenderState().setWireframe(true);
        ball.setMaterial(matWireframe);
      
        if(ball==null)
            System.out.println("WHAT");
      //  ball.lookAt(box.getLocalTranslation(), Vector3f.UNIT_Y);
        return ball;
    }

    private void colorNeighbours(Vertex get) {
        
        Geometry realVertexSphere = makeRedSphere("realV",get.getTriangle().getCenter().x,get.getTriangle().getCenter().y,get.getTriangle().getCenter().z);
        for(Vertex v: get.getNeighbors()){
            colorGreen(v);
        }
        rootNode.attachChild(realVertexSphere);
        
    }
    private void colorGreen(Vertex get){
         Geometry realVertexSphere = makeGreenSphere("realV",get.getTriangle().getCenter().x,get.getTriangle().getCenter().y,get.getTriangle().getCenter().z);
      
        rootNode.attachChild(realVertexSphere);
    }
    
    protected Geometry makeRedSphere(String name, float x, float y, float z) {
        Sphere sphere = new Sphere(30, 30, 1);
        Geometry ball = new Geometry(name, sphere);
        ball.scale(0.05f);
        ball.setLocalTranslation(x, y, z);
         Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Red);
        ball.setMaterial(mat1);
      

        //ball.lookAt(box.getLocalTranslation(), Vector3f.UNIT_Y);
        return ball;
    }
    
    protected Geometry makeGreenSphere(String name, float x, float y, float z) {
        Sphere sphere = new Sphere(30, 30, 1);
        Geometry ball = new Geometry(name, sphere);
        ball.scale(0.05f);
        ball.setLocalTranslation(x, y, z);
         Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Green);
        ball.setMaterial(mat1);
      

        //ball.lookAt(box.getLocalTranslation(), Vector3f.UNIT_Y);
        return ball;
    }

   
    
}
