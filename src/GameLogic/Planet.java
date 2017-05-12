/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author giogio
 */
public class Planet {
    Node solids; //whatever can stop the sight of the pursuer (planet/obstacles..)
    Geometry planet;
    
    
    
    
    
    public Planet(AssetManager assetManager){
        planet = makeFloor(assetManager);
        solids = new Node();
        solids.attachChild(planet);
    }
    
    public Planet(AssetManager assetManager, Node rootNode, Settings setting){
        
        planet = new Geometry("Planet");
        
        DirectionalLight sun = new DirectionalLight();
        
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);
        PlanetMeshGen planetMeshGen = new PlanetMeshGen();
        
        
        if(setting.getTypeOfAlgorithm()==1){
            planetMeshGen.generateHeightmap(setting);
        }
        
        planet.setMesh(planetMeshGen.generateMesh(setting));
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseVertexColor", true);
        // Uncommet for wireframe
        //mat.getAdditionalRenderState().setWireframe(true);

        planet.setMaterial(mat);
        
        solids = new Node();
        solids.attachChild(planet);
    }
    
    
    protected Geometry makeFloor(AssetManager assetManager) {
        Box box = new Box(100, .001f, 100);
        Geometry floor = new Geometry("the Floor", box);
        floor.setLocalTranslation(0, 0, 0);
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Gray);
        floor.setMaterial(mat1);
        return floor;
    }

    public Node getSolids() {
        return solids;
    }

    public Geometry getPlanet() {
        return planet;
    }
    
    
}
