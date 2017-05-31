/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;

import com.jme3.font.BitmapText;

public class TestBillboard extends SimpleApplication {
BitmapText helloText;
Geometry g2;
 Node bb;
    public void simpleInitApp() {
        flyCam.setMoveSpeed(10);

      
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);

        Box b = new Box(.25f, .5f, .25f);
        g2 = new Geometry("Box", b);
        g2.setLocalTranslation(0, 0, 3);
        g2.setMaterial(mat);

        bb = new Node("billboard");
        BitmapFont newFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
         helloText = new BitmapText(guiFont, false);
        helloText.setSize(1.5f);
        helloText.setText("Hello World");
        helloText.setLocalTranslation(new Vector3f(0,0,0));
        
        
        
        BillboardControl control=new BillboardControl();
        
        bb.addControl(control);
        bb.attachChild(helloText);


        rootNode.attachChild(g2);
        rootNode.attachChild(bb);
  
       
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);
        g2.rotate(0, tpf, 0);
        g2.move(g2.getLocalRotation().getRotationColumn(2));
         bb.setLocalTranslation(g2.getWorldTranslation());
         helloText.setText(g2.getLocalTranslation().toString());
    }



    public static void main(String[] args) {
        TestBillboard app = new TestBillboard();
        app.start();

    }
}