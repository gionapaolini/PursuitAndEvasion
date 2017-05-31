/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;

/**
 *
 * @author giogio
 */
public class Escaper extends Agent{
    
    private Material colorAlive, colorDead;
    private boolean died;
    
    public Escaper(AssetManager assetManager){
        super();
        colorAlive = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        colorAlive.setColor("Color", ColorRGBA.White);
        colorDead = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        colorDead.setColor("Color", ColorRGBA.Red);
        body.setMaterial(colorAlive);
  
    }
    
    @Override
    public void move(float tpf,Planet planet){
        if(!died)
            super.move(tpf,planet);
    }
    public void die(){
        System.out.println("I'm dead!");
        died = true;
        body.setMaterial(colorDead);
    }

    public boolean isDied() {
        return died;
    }
    
    
    
}
