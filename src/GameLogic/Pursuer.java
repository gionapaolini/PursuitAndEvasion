/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Line;
import java.util.List;

/**
 *
 * @author giogio
 */
public class Pursuer extends Agent{
    
    private float field_of_sight;
    private Material color, blue;
    private boolean visibleField;
    private Geometry l1,l2; //lines bound for sight
    
    
    public Pursuer(AssetManager assetManager){
        super();
        color = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        color.setColor("Color", ColorRGBA.Red);
        blue = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blue.setColor("Color", ColorRGBA.Blue);
        body.setMaterial(blue);
        visibleField = false;
        setDefault();
        updateFieldBounds();
    }
    
    @Override
    public void setDefault(){
        super.setDefault();
        field_of_sight = 50;
    }
    
    @Override
    public void move(float tpf, Planet planet){
        super.move(tpf, planet);
    }

    public void setField_of_sight(float field_of_sight) {
        this.field_of_sight = field_of_sight;
    }

    public float getField_of_sight() {
        return field_of_sight;
    }
  
    public void watchOut(Node notAgents, Planet planet){
      
        for(Agent escaper: agents){
           
            Escaper e = (Escaper)escaper;
            if(e.isDied() || e.isInvisible()){
                continue;
            }
           // Ray ray = new Ray(body.getLocalTranslation(),escaper.getBody().getLocalTranslation());
           Ray ray = new Ray(nodeAgent.getWorldTranslation(),escaper.getNodeAgent().getWorldTranslation().subtract(nodeAgent.getWorldTranslation()).normalizeLocal());
           
           
           CollisionResults results = new CollisionResults();
           planet.getPlanet().collideWith(ray, results);
            
      
            if (results.size()<=0) {
              
                if(checkInSight(escaper)){
                     e.die();
                    //match.pause();
                     createLine(e, planet.rootNode);
                     
                }

            }
        }
    }
    
    protected boolean checkInSight(Agent escaper){
            Vector3f cube2ToOrigin = escaper.getBody().getWorldTranslation().add(body.getWorldTranslation().mult(-1));
            Vector3f direction = body.getWorldRotation().getRotationColumn(2);
            
            double angle = getAngle(direction, cube2ToOrigin);
            
            if (angle > Math.toRadians(field_of_sight/2)) {
                return false;
            }
            return true;
    }
    protected double getAngle(Vector3f v1, Vector3f v2){
        double dot = v1.dot(v2);
        double lenghtprod = v1.length()*v2.length();
        double result = dot / lenghtprod;
        return Math.acos(result);      

      
    }

    public boolean isVisibleField() {
        return visibleField;
    }

    public void setVisibleField(boolean visibleField) {
        this.visibleField = visibleField;
        if(!visibleField){
            nodeAgent.detachChildNamed("l1");
            nodeAgent.detachChildNamed("l2"); 
        }
    }

    public Geometry getL1() {
        return l1;
    }

    public void setL1(Geometry l1) {
        this.l1 = l1;
    }

    public Geometry getL2() {
        return l2;
    }

    public void setL2(Geometry l2) {
        this.l2 = l2;
    }
    
    
    
    
    public void updateFieldBounds(){
        if(visibleField){
            nodeAgent.detachChildNamed("l1");
            nodeAgent.detachChildNamed("l2");

            Ray ray1 = new Ray(body.getLocalTranslation(), body.getLocalRotation().getRotationColumn(2));

            Quaternion rotation = new Quaternion();
            rotation.fromAngleAxis((float) Math.toRadians((int)(field_of_sight/2)),Vector3f.UNIT_Y);
            Vector3f firstLimit = rotation.mult(ray1.getDirection());
            rotation.fromAngleAxis((float) Math.toRadians((int)(-field_of_sight/2)),Vector3f.UNIT_Y);
            Vector3f secondLimit = rotation.mult(ray1.getDirection());

            l1 = new Geometry("l1", new Line(ray1.getOrigin(), firstLimit.mult(10)));
            l2 = new Geometry("l2", new Line(ray1.getOrigin(), secondLimit.mult(10)));

            l1.setMaterial(blue);
            l2.setMaterial(blue);

            nodeAgent.attachChild(l1);
            nodeAgent.attachChild(l2);
        }
    
    }

    private void createLine(Escaper e, Node rootNode) {
        
        Geometry line = new Geometry("lineee", new Line(nodeAgent.getWorldTranslation(),e.getNodeAgent().getWorldTranslation()));
        line.setMaterial(blue);
        rootNode.attachChild(line);
        
        
        
        
    }
    
    
    
}
