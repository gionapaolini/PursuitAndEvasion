/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import AI.PathFinding.GraphA;
import AI.PathFinding.Vertex;
import Test.TestGuardPlanetMesh;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giogio
 */
public class Match {
   
    Planet planet;
    List<Agent> agents;
    List<Power> powers;
    int n_pursuers,       //number of pursuers
        n_escapers,       //number of escapers
        n_pPowerups,      //number of pursuer's powerups
        n_pMalus,         //number of pursuer's malus
        n_ePowersups,     //number of escaper's powerups
        n_eMalus;         //number of escaper's malus
    
    double gameVelocity;
    boolean pause;
    boolean end;
    GraphA graph;
    Thread timeIncreaser;
   
    
    public Match(AssetManager assetManager, Node rootNode, Settings setting){
        planet = new Planet(assetManager, rootNode, setting);
        graph = new GraphA(planet.getNavMesh().getMesh());
        this.n_pursuers = setting.getN_pursuers();
        this.n_escapers = setting.getN_escapers();
        agents = new ArrayList<>();
        ArrayList<Vertex> copy = graph.getCopyVertexList();
        for(int i=0; i<n_pursuers;i++){
            Pursuer p= new Pursuer(assetManager);
            agents.add(p);
            
            p.setCurrentVertex(copy.remove((int)(Math.random()*copy.size())));
            p.setPosition(p.getCurrentVertex().getTriangle().getCenter());
        }
        for(int i=0; i<n_escapers;i++){
            agents.add(new Escaper(assetManager));
        }
        for(Agent agent: agents){
            for(Agent agent1: agents){
                if(agent instanceof Pursuer && agent1 instanceof Escaper){
                    agent.addAgent(agent1);
                    agent1.addAgent(agent);
                }
            }
        }
        gameVelocity = 1;
        pause = true;
        end = false;
        timeIncreaser = new Thread() {
        public void run() {
             while(!pause && !end){
                 
                 try {
                     Thread.sleep(1000);
                 } catch (InterruptedException ex) {
                     Logger.getLogger(TestGuardPlanetMesh.class.getName()).log(Level.SEVERE, null, ex);
                 }
              
                graph.incrementTime();
             }
             
            }  
        };
        
        
    }
    public Match(AssetManager assetManager){
         planet = new Planet(assetManager);
        graph = new GraphA(planet.getNavMesh().getMesh());
         ArrayList<Vertex> copy = graph.getCopyVertexList();
        for(int i=0; i<n_pursuers;i++){
             Pursuer p= new Pursuer(assetManager);
            agents.add(p);
            p.setCurrentVertex(copy.remove((int)(Math.random()*copy.size())));
            p.setPosition(p.getCurrentVertex().getTriangle().getCenter());
        }
        for(int i=0; i<n_escapers;i++){
            agents.add(new Escaper(assetManager));
        }
        for(Agent agent: agents){
            for(Agent agent1: agents){
                if(agent instanceof Pursuer && agent1 instanceof Escaper){
                    agent.addAgent(agent1);
                    agent1.addAgent(agent);
                }
            }
        }
        gameVelocity = 1;
        pause = true;
        end = false;
        timeIncreaser = new Thread() {
        public void run() {
             while(!pause && !end){
                 
                 try {
                     Thread.sleep(1000);
                 } catch (InterruptedException ex) {
                     Logger.getLogger(TestGuardPlanetMesh.class.getName()).log(Level.SEVERE, null, ex);
                 }
              
                graph.incrementTime();
             }
             
            }  
        };
       
       
    }

    public GraphA getGraph() {
        return graph;
    }

    public void setGraph(GraphA graph) {
        this.graph = graph;
    }
    
    
    
    public void start(){
        pause = false;
        timeIncreaser.start();
    }
    
    public void pause(){
        pause=!pause;
        if(!pause){
            timeIncreaser.start();
        }
    }
    
    public void moveAgents(float tpf){
        for(Agent agent: agents){
            if(agent instanceof Escaper && ((Escaper)agent).isDied())
                continue;
            if(agent instanceof Pursuer)
                agent.moveOnGrid(tpf, planet);
            else
                agent.moveEvading(planet);
            
        }
        /*
        
        System.out.println("called");
        
 
        
        for(Agent agent: agents){
            if(agent instanceof Escaper){
                Escaper escaper = (Escaper) agent;
                if(escaper.isDied())
                    continue;
            }
           
            // current position ray, 
            // normal, 
            // planet normal (ray actually)
            // derivative,
            // multiplier
            // direction on surface
            // scale by multiplier
            // move
            // new ray
            // place on surface
            // check if difference is too much
            
            
            
            CollisionResults results = new CollisionResults();
            CollisionResults newresults = new CollisionResults();
         
//            Vector2f intent = agent.move(tpf);
//            agent.body.move(new Vector3f((float)Math.random(), (float)Math.random(), (float)Math.random()).mult(0.2f));
            
             Ray r = new Ray(new Vector3f(0,0,0), agent.body.getWorldTranslation());    
            
//            try{
                
                planet.planet.collideWith(r, results);
                Vector3f contactPoint = results.getFarthestCollision().getContactPoint();
                Vector3f contactNormal = results.getFarthestCollision().getContactNormal();  
                
                float multiplier = agent.body.getWorldTranslation().normalize().dot(contactNormal);
//                multiplier = 2.9f;
                
                if(multiplier > 0.0f)
//                    agent.getNodeAgent().move(new Vector3f((float)Math.random(), (float)Math.random(), (float)Math.random()).mult(multiplier));
                    agent.getNodeAgent().move(new Vector3f(-(float)Math.random(), -(float)Math.random(),0).mult(multiplier*multiplier*multiplier));

                    
                    
                Ray newr = new Ray(new Vector3f(0,0,0), agent.body.getWorldTranslation());   
                planet.planet.collideWith(newr, newresults);
                Vector3f newcontactPoint = newresults.getFarthestCollision().getContactPoint();

//                Ray ray = new Ray(contactPoint, contactNormal);
//                Geometry line = new Geometry("line1", new Line(ray.getOrigin(),ray.getDirection().mult(100.0f)));
//                line.setMaterial(material);
//                agent.setLine(line);
//                
        
//                agent.body.setLocalTranslation(contactPoint);
                agent.getNodeAgent().setLocalTranslation(newcontactPoint);
//            } catch(Exception e){}
            
            
        }
*/
    }
    
    public void checkEndGame(){
        Escaper escaper;
        for(Agent agent: agents){
            if(agent instanceof Escaper){
                escaper = (Escaper) agent;
                if(!escaper.isDied()){
                    return;
                }
                
            }
                
        }
        end = true;
    }
    
    public int getEvadersLeft(){
        int i=0;
        Escaper escaper;
        for(Agent agent: agents){
            if(agent instanceof Escaper){
                escaper = (Escaper) agent;
                if(!escaper.isDied()){
                    i++;
                }
                
            }
                
        }
        return i;
    }
    
    
    public void checkViews(){
        for(Agent agent: agents){
            if(agent instanceof Pursuer){
                Pursuer pursuer = (Pursuer) agent;
                pursuer.watchOut(planet.getSolids(), planet);
                pursuer.updateFieldBounds();
            }
           
        }
        checkEndGame();
        
    }

    public Planet getPlanet() {
        return planet;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public List<Power> getPowers() {
        return powers;
    }

    public int getN_pursuers() {
        return n_pursuers;
    }

    public int getN_escapers() {
        return n_escapers;
    }

    public int getN_pPowerups() {
        return n_pPowerups;
    }

    public int getN_pMalus() {
        return n_pMalus;
    }

    public int getN_ePowersups() {
        return n_ePowersups;
    }

    public int getN_eMalus() {
        return n_eMalus;
    }

    public double getGameVelocity() {
        return gameVelocity;
    }

    public boolean isPause() {
        return pause;
    }

    public boolean isEnd() {
        return end;
    }
    
    
    
    public void calculateSafeTriangles(){
        for(Agent agent: agents){
            if(!(agent instanceof Pursuer))
                continue;
            
            Pursuer current = (Pursuer) agent;
            for(Vertex v: graph.getVertices()){ 
                    v.setSafe(true);
                    Ray ray1 = new Ray(current.getNodeAgent().getWorldTranslation(),v.getTriangle().get1().subtract(current.getNodeAgent().getWorldTranslation()).normalizeLocal());
                    Ray ray2 = new Ray(current.getNodeAgent().getWorldTranslation(),v.getTriangle().get2().subtract(current.getNodeAgent().getWorldTranslation()).normalizeLocal());
                    Ray ray3 = new Ray(current.getNodeAgent().getWorldTranslation(),v.getTriangle().get3().subtract(current.getNodeAgent().getWorldTranslation()).normalizeLocal());
                                        
                    CollisionResults results1 = new CollisionResults();                 
                    planet.getPlanet().collideWith(ray1, results1);
                    
                    CollisionResults results2= new CollisionResults();                                    
                    planet.getPlanet().collideWith(ray2, results2);
                    
                    CollisionResults results3 = new CollisionResults();
                    planet.getPlanet().collideWith(ray3, results3);
                    
                    if(results1.size()==0){
                        if(checkInSight(current, v.getTriangle().get1())){
                            v.setUnsafe();
                            return;
                        }
                        
                    }
                    if(results2.size()==0){
                        if(checkInSight(current, v.getTriangle().get2())){
                            v.setUnsafe();
                            return;
                        }
                    }
                    if(results3.size()==0){
                        if(checkInSight(current, v.getTriangle().get3())){
                            v.setUnsafe();
                            return;
                        }
                    }
            
            }
        }
    }
    
    
     public void calculateSafeTriangles2(){
        for(Agent agent: agents){
            if(!(agent instanceof Pursuer))
                continue;
            
            Pursuer current = (Pursuer) agent;
            for(Vertex v: graph.getVertices()){ 
                v.setSafe(true);
                if(insidePyramid(v.getTriangle().get1(), current)){
                    Ray ray = new Ray(current.getNodeAgent().getWorldTranslation(),v.getTriangle().get1().subtract(current.getNodeAgent().getWorldTranslation()).normalizeLocal());
                  //  ray.setLimit(v.getTriangle().get1().subtract(current.getNodeAgent().getWorldTranslation()).length());
                    CollisionResults results = new CollisionResults();                 
                    planet.getPlanet().collideWith(ray, results);
                    if(results.size()<=0){
                        v.setUnsafe();
                        return;
                    }
                        
                }
                if(insidePyramid(v.getTriangle().get2(), current)){

                    Ray ray = new Ray(current.getNodeAgent().getWorldTranslation(),v.getTriangle().get2().subtract(current.getNodeAgent().getWorldTranslation()).normalizeLocal());
                   // ray.setLimit(v.getTriangle().get2().subtract(current.getNodeAgent().getWorldTranslation()).length());
                    CollisionResults results = new CollisionResults();                 
                    planet.getPlanet().collideWith(ray, results);
                    if(results.size()<=0){
                        v.setUnsafe();
                        return;
                    }
                        
                }
                if(insidePyramid(v.getTriangle().get3(), current)){

                    Ray ray = new Ray(current.getNodeAgent().getWorldTranslation(),v.getTriangle().get3().subtract(current.getNodeAgent().getWorldTranslation()).normalizeLocal());
                    //ray.setLimit(v.getTriangle().get3().subtract(current.getNodeAgent().getWorldTranslation()).length());
                    CollisionResults results = new CollisionResults();                 
                    planet.getPlanet().collideWith(ray, results);
                    if(results.size()<=0){
                        v.setUnsafe();
                        return;
                    }
                        
                }
            
            }
        }
    }
    
     protected boolean checkInSight(Pursuer pursuer, Vector3f point){
            Vector3f cube2ToOrigin = point.add(pursuer.getNodeAgent().getWorldTranslation().mult(-1));
            Vector3f direction = pursuer.getNodeAgent().getWorldRotation().getRotationColumn(2);
            
            double angle = getAngle(direction, cube2ToOrigin);
            
            if (angle > Math.toRadians(pursuer.getField_of_sight()/2)) {
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
     
    public boolean insidePyramid(Vector3f object, Pursuer pursuer){
        
       Vector3f eyePosition = pursuer.getNodeAgent().getWorldTranslation();
       Vector3f direction = pursuer.getNodeAgent().getLocalRotation().getRotationColumn(2);
       
       Ray rayPlan = new Ray(planet.getPlanet().getLocalTranslation(),eyePosition.subtract(planet.getPlanet().getLocalTranslation()).normalizeLocal());
       CollisionResults results = new CollisionResults();
       planet.getPlanet().collideWith(rayPlan, results);
       
       Vector3f normalPoint = results.getFarthestCollision().getContactNormal();
        
       Vector3f[] points = buildPyramid(eyePosition,direction,normalPoint); 
        
       int cnt = 0;
       Ray ray = new Ray(object, Vector3f.UNIT_Y);
     
        if ( ray.intersectWherePlanar(object, points[4], points[6], null) ) cnt++;
        if ( ray.intersectWherePlanar(object, points[4], points[5], null) ) cnt++;
        if (cnt>1) return false;
        if ( ray.intersectWherePlanar(object, points[5], points[7], null) ) cnt++;
        if (cnt>1) return false;
        if ( ray.intersectWherePlanar(object, points[7], points[6], null) ) cnt++;
        if (cnt>1) return false;
   
        
        if ( ray.intersectWherePlanar(points[4], points[5], points[6], null) ) cnt++;
        if (cnt>1) return false;
        if ( ray.intersectWherePlanar(points[5], points[7], points[6], null) ) cnt++;
        if (cnt>1) return false;
        if (cnt==1)
            return true;
        else //->(cnt==0 || cnt>1)
            return false;
        
    }
     
    public Vector3f[] buildPyramid(Vector3f eyePosition,Vector3f direction,Vector3f upVector){
         
         direction.normalize();
         
         Vector3f rightVector = direction.cross(upVector);
         float nearDistance = 10;
         float farDistance = 1000;
         float fov = 110;
         float aspectRatio = 16/12;
         
         float Hnear = (float) (2 * Math.tan(fov/2) * nearDistance);
         float Wnear = Hnear*aspectRatio;
         
         float Hfar = (float) (2 * Math.tan(fov / 2) * farDistance);
         float Wfar = Hfar * aspectRatio;
         
         Vector3f Cnear = eyePosition.add(direction.mult(nearDistance));
         Vector3f Cfar = eyePosition.add(direction.mult(farDistance));
         
         Vector3f NTL,NTR,NBL,NBR,FTL,FTR,FBL,FBR;
         
         NTL = Cnear.add(upVector.mult(Hnear/2)).subtract(rightVector.mult(Wnear/2));
         NTR = Cnear.add(upVector.mult(Hnear/2)).add(rightVector.mult(Wnear/2));
         NBL = Cnear.subtract(upVector.mult(Hnear/2)).subtract(rightVector.mult(Wnear/2));
         NBR = Cnear.subtract(upVector.mult(Hnear/2)).add(rightVector.mult(Wnear/2));
         
         FTL = Cfar.add(upVector.mult(Hfar/2)).subtract(rightVector.mult(Wfar/2));
         FTR = Cfar.add(upVector.mult(Hfar/2)).add(rightVector.mult(Wfar/2));
         FBL = Cfar.subtract(upVector.mult(Hfar/2)).subtract(rightVector.mult(Wfar/2));
         FBR = Cfar.subtract(upVector.mult(Hfar/2)).add(rightVector.mult(Wfar/2));
         
         Vector3f [] v = {NTL,NTR,NBL,NBR,
                          FTL,FTR,FBL,FBR};
         return v;
        
         
     }
    
}
