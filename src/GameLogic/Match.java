/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;

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
    
    
    
    public Match(AssetManager assetManager, Node rootNode, Settings setting){
        this.n_pursuers = setting.getN_pursuers();
        this.n_escapers = setting.getN_escapers();
        agents = new ArrayList<>();
        for(int i=0; i<n_pursuers;i++){
            agents.add(new Pursuer(assetManager));
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
        planet = new Planet(assetManager, rootNode, setting);
        
        
    }
    public Match(AssetManager assetManager){
        for(int i=0; i<n_pursuers;i++){
            agents.add(new Pursuer(assetManager));
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
        planet = new Planet(assetManager);
       
    }
    
    public void start(){
        pause = false;
    }
    
    public void pause(){
        pause=!pause;
    }
    
    public void moveAgents(float tpf){
        for(Agent agent: agents){
            if(agent instanceof Escaper && ((Escaper)agent).isDied())
                continue;
            agent.move(tpf, planet);
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
        System.out.println("Checking views");
        for(Agent agent: agents){
            if(agent instanceof Pursuer){
                Pursuer pursuer = (Pursuer) agent;
                pursuer.watchOut(planet.getSolids());
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
    
    
    
    
    
}
