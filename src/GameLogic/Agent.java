/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import AI.PathFinding.Vertex;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Triangle;
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
    protected Vector3f currentVel;
    protected List<Agent> agents;
    protected static int nAgent=0;
    protected Node nodeAgent; //attach to it whatever needs to rotate/translate with the agent
    
    protected float maxVel,
                   size;
    
    protected List<Power> currentPowers;
    
    protected boolean invisible, moving;
    protected Vertex currentVertex;
    protected double lastMovementTime;
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
        
        currentVel = new Vector3f(0,0,0);
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }
    
    public void setDefault(){
        maxVel = 0.4f;
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
        maxVel = vel;
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

    public Vertex getCurrentVertex() {
        return currentVertex;
    }

    public void setCurrentVertex(Vertex currentVertex) {
        this.currentVertex = currentVertex;
    }
    
    

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public double getVelocity() {
        return maxVel;
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
            
         float multiplier = 10;
           Vector3f currentPosition = nodeAgent.getLocalTranslation();
           Vector3f center = planet.getPlanet().getLocalTranslation(); 
           double distance = getDistance(center,currentPosition);
           double difference  =  planet.getSettings().getRadius() - distance;
           multiplier += (difference);
       System.out.println(difference);
       Vector3f forward = nodeAgent.getLocalRotation().mult(Vector3f.UNIT_Z).mult(tpf * multiplier);
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
    
    public void moveOnGrid(float tpf, Planet planet){
       
        if(!moving &&  System.currentTimeMillis()-lastMovementTime>3000){
            System.out.println("NOT MOVING");
            double bestTime = 0;
            Vertex bestVertex = null;
            for(Vertex v: currentVertex.getNeighbors()){
                for(Vertex v1: v.getNeighbors()){
                    System.out.println(v1.getTime());
                    if(v1.getTime()>bestTime){
                        bestVertex = v1;
                        bestTime = v1.getTime();
                    }else if(v1.getTime()==bestTime && Math.random()>=0.75){
                        bestVertex = v1;
                        bestTime = v1.getTime();
                    }
                }
            }
            bestVertex.incrementTimeNeighbour(0);
            bestVertex.setTime(0);
            
            currentVertex = bestVertex;
   
            System.out.println(currentVertex.getTriangle().getCenter()); 
            
           
            
        //   moveTo(currentVertex.getTriangle().getCenter(), planet);
            
            moving = true;
            lastMovementTime = System.currentTimeMillis();
        }else if(moving){
                       
            
         

           //////////ADD DIFFERENT SPEED
          
           Vector3f currentPosition = nodeAgent.getWorldTranslation();
           Vector3f center = planet.getNavMesh().getWorldTranslation(); 
           double distance = getDistance(center,currentPosition);
           double difference  =  planet.getSettings().getRadius() +2 - distance;
           System.out.println(difference/10);
           difference = Math.max(difference/5, -0.45f);
           maxVel = 0.5f;
           maxVel +=difference;
           Vector3f forward = nodeAgent.getLocalRotation().mult(Vector3f.UNIT_Z).normalize().mult(maxVel);
           nodeAgent.move(forward);
           if(onTriangle(currentVertex.getTriangle(),planet)){
               moving = false;
           }
           moveTo(currentVertex.getTriangle().getCenter(), planet);            
           reposition(planet);
           
          
           
        }
    }
    
    public boolean onTriangle(Triangle tri, Planet planet){
        Ray ray = new Ray(planet.getPlanet().getLocalTranslation(),nodeAgent.getLocalTranslation());
        CollisionResults results = new CollisionResults();
        tri.collideWith(ray, results);
        return results.size()>0;
    }
    
    public void moveTo(Vector3f point,Planet planet){
        Ray ray = new Ray(planet.getPlanet().getLocalTranslation(),nodeAgent.getLocalTranslation());
        CollisionResults results = new CollisionResults();
        planet.getNavMesh().collideWith(ray, results);
        Vector3f contactPoint = results.getFarthestCollision().getContactPoint();
        Vector3f normalPoint = results.getFarthestCollision().getContactNormal();
        
        Vector3f direction = getDirectionToPoint(normalPoint,point);
        
        Quaternion rotationQuat = new Quaternion();
        rotationQuat.lookAt(direction,normalPoint);
        nodeAgent.setLocalRotation(rotationQuat);
        
        reposition(contactPoint,normalPoint);
    }
    
    public Vector3f getDirectionToPoint(Vector3f normal, Vector3f point){
        Vector3f pointNewOrigin = point.add(nodeAgent.getLocalTranslation().mult(-1));
        //projection of the point
        Vector3f projection = getProjectionOntoPlane(normal,pointNewOrigin);
        return projection;
        
    }
    
    public void setPosition(Vector3f pos){
        nodeAgent.setLocalTranslation(pos.x,pos.y,pos.z);
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
         
        float length = contactPoint.length();
        Vector3f newLocation = contactPoint.normalize().mult(length+2);
        nodeAgent.setLocalTranslation(newLocation);
        //System.out.println(results.getFarthestCollision().getContactNormal());
        Vector3f rotation = getProjectionOntoPlane(normalPoint,nodeAgent.getLocalRotation().getRotationColumn(2));
        Quaternion rotationQuat = new Quaternion();
        rotationQuat.lookAt(rotation,normalPoint);
        nodeAgent.setLocalRotation(rotationQuat);
        
    }
    
    private void reposition(Planet planetObj){
        Geometry planet = planetObj.getPlanet();
       Ray ray = new Ray(planet.getLocalTranslation(),nodeAgent.getLocalTranslation().subtract(planet.getLocalTranslation()).normalizeLocal());
        CollisionResults results = new CollisionResults();
        planet.collideWith(ray, results);
        if(results.size()<=0)
            return;
        Vector3f contactPoint = results.getFarthestCollision().getContactPoint();
        Vector3f normalPoint = results.getFarthestCollision().getContactNormal();
        float length = contactPoint.length();
        Vector3f newLocation = contactPoint.normalize().mult(length+2);
        nodeAgent.setLocalTranslation(newLocation);
        //System.out.println(results.getFarthestCollision().getContactNormal());
        Vector3f rotation = getProjectionOntoPlane(normalPoint,nodeAgent.getLocalRotation().getRotationColumn(2));
        Quaternion rotationQuat = new Quaternion();
        rotationQuat.lookAt(rotation,normalPoint);
        nodeAgent.setLocalRotation(rotationQuat);
        setCurrentVel(nodeAgent.getLocalRotation().getRotationColumn(2).normalize());
        
    }
    
     private void repositionOnNavMesh(Planet planetObj){
        Geometry planet = planetObj.getNavMesh();
        Ray ray = new Ray(planet.getLocalTranslation(),nodeAgent.getLocalTranslation());
     
        CollisionResults results = new CollisionResults();
        planet.collideWith(ray, results);
     
        Vector3f contactPoint = results.getFarthestCollision().getContactPoint();
        Vector3f normalPoint = results.getFarthestCollision().getContactNormal();
     
        float length = contactPoint.length();
        Vector3f newLocation = contactPoint.normalize().mult(length+2);
        nodeAgent.setLocalTranslation(newLocation);

        //System.out.println(results.getFarthestCollision().getContactNormal());
        Vector3f rotation = getProjectionOntoPlane(normalPoint,nodeAgent.getLocalRotation().getRotationColumn(2));
        Quaternion rotationQuat = new Quaternion();
        rotationQuat.lookAt(rotation,normalPoint);
        nodeAgent.setLocalRotation(rotationQuat);
        setCurrentVel(nodeAgent.getLocalRotation().getRotationColumn(2).normalize());

    }
    
    
    private Vector3f getProjectionOntoPlane(Vector3f n, Vector3f v){
        
    
        Vector3f projection = n.cross(v.cross(n));
      
       // System.out.println(projection);
        
        return projection;
    }
    
    private double getDistance(Vector3f start, Vector3f end){
        return Math.sqrt(Math.pow(start.x - end.x,2)+Math.pow(start.y - end.y,2)+Math.pow(start.z - end.z,2));
    }

    public Vector3f getCurrentVel() {
        return currentVel;
    }

    public void setCurrentVel(Vector3f currentVel) {
        this.currentVel = currentVel;
    }
    
    
    
    public Vector3f seek(Agent target){
        Vector3f targetPos = target.getNodeAgent().getWorldTranslation();

        Vector3f desideredVel = targetPos.add(nodeAgent.getWorldTranslation().mult(-1)).normalize().mult(maxVel);
        return desideredVel.add(currentVel.mult(-1));
        
    }
    public Vector3f flee(Agent target){
        Vector3f targetPos = target.getNodeAgent().getWorldTranslation();

        Vector3f desideredVel = nodeAgent.getWorldTranslation().add(targetPos.mult(-1)).normalize().mult(maxVel);
        return desideredVel.add(currentVel.mult(-1));
        
    }
    
    public Vector3f seek(Vector3f target){
        Vector3f targetPos = target;

        Vector3f desideredVel = targetPos.add(nodeAgent.getWorldTranslation().mult(-1)).normalize().mult(maxVel);
        return desideredVel.add(currentVel.mult(-1));
        
    }
    public Vector3f flee(Vector3f target){
        Vector3f targetPos = target;

        Vector3f desideredVel = nodeAgent.getWorldTranslation().add(targetPos.mult(-1)).normalize().mult(maxVel);
        return desideredVel.add(currentVel.mult(-1));
        
    }
     
    public Vector3f pursue(Agent target){
        float t = 3;
        
        Vector3f dist = target.getNodeAgent().getWorldTranslation().add(nodeAgent.getWorldTranslation().mult(-1));
        t = dist.length() / maxVel;
        
        Vector3f targetPos = target.getNodeAgent().getWorldTranslation();
        Vector3f targetVel = target.getCurrentVel();
        
        Vector3f futurePos = targetPos.add(targetVel.mult(t));
        return seek(futurePos);
    }
    
    public Vector3f evade(Agent target){
        float t = 3;
        
        Vector3f dist = target.getNodeAgent().getWorldTranslation().add(nodeAgent.getWorldTranslation().mult(-1));
        t = dist.length() / maxVel;
        
        Vector3f targetPos = target.getNodeAgent().getWorldTranslation();
        Vector3f targetVel = target.getCurrentVel();
        
        Vector3f futurePos = targetPos.add(targetVel.mult(t));
        return flee(futurePos);
    }
    
    public void moveEvading(Planet planetObj){
        Vector3f currentPosition = nodeAgent.getLocalTranslation();
        Vector3f center = planetObj.getPlanet().getLocalTranslation(); 
        double distance = getDistance(center,currentPosition);
        double difference  =  planetObj.getSettings().getRadius()+2 - distance;
       
        difference = Math.max(difference/5, -0.45f);
        maxVel = 0.5f;
        maxVel +=difference;

      

        
        
        
        float minDistant = 100000;
        Agent closestAgent = null;
        for(Agent agent: agents){
             float dist = agent.getNodeAgent().getWorldTranslation().add(nodeAgent.getWorldTranslation().mult(-1)).length();
             if(dist<minDistant){
                 minDistant = dist;
                 closestAgent = agent;
             }

        }
        setCurrentVel(currentVel.add(evade(closestAgent)));
        Geometry planet = planetObj.getPlanet();
        setPosition(nodeAgent.getWorldTranslation().add(currentVel));
        repositionWithVel(planet); 
    }
    
     
    private void repositionWithVel(Geometry planet){
        
        Ray ray = new Ray(planet.getLocalTranslation(),nodeAgent.getLocalTranslation().subtract(planet.getLocalTranslation()).normalizeLocal());
        CollisionResults results = new CollisionResults();
        planet.collideWith(ray, results);
        if(results.size()<=0)
            return;
        Vector3f contactPoint = results.getFarthestCollision().getContactPoint();
        Vector3f normalPoint = results.getFarthestCollision().getContactNormal();
        float length = contactPoint.length();
        
        Vector3f newLocation = contactPoint.normalize().mult(length+2);
        nodeAgent.setLocalTranslation(newLocation);        
        
        Vector3f rotation = getProjectionOntoPlane(normalPoint,currentVel);
        
        float magnitude = currentVel.length();
        Quaternion rotationQuat = new Quaternion();
        rotationQuat.lookAt(rotation,normalPoint);
        nodeAgent.setLocalRotation(rotationQuat);
        setCurrentVel(nodeAgent.getLocalRotation().getRotationColumn(2).normalize().mult(magnitude));
        
    }
     
}
