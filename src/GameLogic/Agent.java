/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author giogio
 */
public abstract class Agent {
    
    protected Geometry body;
    protected List<Agent> agents;
    protected static int nAgent=0;
    protected Node nodeAgent; //attach to it whatever needs to rotate/translate with the agent
    
    protected float velocity,
                   size;
    
    protected List<Power> currentPowers;
    
    protected boolean invisible;
    
    public Agent(){
        agents = new ArrayList<>();
        currentPowers  = new ArrayList<>();
        body = makeSphere("agent"+nAgent, 0,0,0);
        nodeAgent = new Node();
        nodeAgent.attachChild(body);
        float randomX = (float) (Math.random()*100);
        if(Math.random()<0.5)
            randomX-=200;
        else
            randomX+=200;
        float randomY = (float) (Math.random()*100);
        if(Math.random()<0.5)
            randomY-=200;
        else
            randomY+=200;
        float randomZ = (float) (Math.random()*100);
        if(Math.random()<0.5)
            randomZ-=200;
        else
            randomZ+=200;
        
        nodeAgent.setLocalTranslation(randomX,randomY,randomZ);
    }
    
    public void setDefault(){
        velocity = 10;
        size =10;
        invisible = false;
    }
    
    public void addAgent(Agent agent){
        agents.add(agent);
    }
    
    public void addPower(Power power){
        currentPowers.add(power);
    }
    public void removePower(Power power){
        currentPowers.remove(power);
    }
    public void resetPowers(){
        currentPowers.clear();
    }
    public void setVelocity(float vel){
        velocity = vel;
    }
    public void setSize(float s){
        float scale =  s/size;
        size = s;
        nodeAgent.scale(scale);
        
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    public List<Power> getCurrentPowers() {
        return currentPowers;
    }

    public void setCurrentPowers(List<Power> currentPowers) {
        this.currentPowers = currentPowers;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getSize() {
        return size;
    }
    
    protected Geometry makeSphere(String name, float x, float y, float z) {
        Sphere sphere = new Sphere(30, 30, 1);
        Geometry ball = new Geometry(name, sphere);
        ball.setLocalTranslation(x, y, z);
        return ball;
    }   
    
    
    public void move(float tpf, Planet planet){
            
       
       Vector3f forward = nodeAgent.getLocalRotation().mult(Vector3f.UNIT_Z).mult(tpf * 10);
       nodeAgent.move(forward);
       reposition(planet);
       
       if(Math.random()<0.3f){
           tpf*=-1;
       }
       if(Math.random()>0.1f){
          if(Math.random()>0.1f){
              rotate(tpf);
          }else{
              rotate(-tpf);
          }
       }
        
    }
    
    public void rotate(float tpf){
        nodeAgent.rotate(0,tpf,0);
    }

    public Geometry getBody() {
        return body;
    }

    public Node getNodeAgent() {
        return nodeAgent;
    }
    
    private void reposition(Vector3f contactPoint, Vector3f normalPoint){
         
        
        nodeAgent.setLocalTranslation(contactPoint);
        //System.out.println(results.getFarthestCollision().getContactNormal());
        Vector3f rotation = getProjectionOntoPlane(normalPoint,nodeAgent.getLocalRotation().getRotationColumn(2));
        Quaternion rotationQuat = new Quaternion();
        rotationQuat.lookAt(rotation,normalPoint);
        nodeAgent.setLocalRotation(rotationQuat);
        
    }
    
    private void reposition(Planet planetObj){
        Geometry planet = planetObj.getPlanet();
       Ray ray = new Ray(planet.getLocalTranslation(),nodeAgent.getLocalTranslation());
        CollisionResults results = new CollisionResults();
        planet.collideWith(ray, results);
        Vector3f contactPoint = results.getFarthestCollision().getContactPoint();
        Vector3f normalPoint = results.getFarthestCollision().getContactNormal();

        nodeAgent.setLocalTranslation(contactPoint);
        //System.out.println(results.getFarthestCollision().getContactNormal());
        Vector3f rotation = getProjectionOntoPlane(normalPoint,nodeAgent.getLocalRotation().getRotationColumn(2));
        Quaternion rotationQuat = new Quaternion();
        rotationQuat.lookAt(rotation,normalPoint);
        nodeAgent.setLocalRotation(rotationQuat);
        
    }
    
    
    private Vector3f getProjectionOntoPlane(Vector3f n, Vector3f v){
        
    
        Vector3f projection = n.cross(v.cross(n));
      
        System.out.println(projection);
        
        return projection;
    }
    
    
}
