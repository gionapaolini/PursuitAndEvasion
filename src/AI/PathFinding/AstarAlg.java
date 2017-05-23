/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.PathFinding;

import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author carolley
 */
public class AstarAlg {
    GraphA graph;
    ArrayList<Vertex> totalPath;
    ArrayList<Vertex> closedSet;
    ArrayList<Vertex> openSet; 
    Vertex goal;
    Vertex start;
    
    public AstarAlg(Vertex start, Vertex goal, GraphA g){
        openSet = new ArrayList<>();
        totalPath = new ArrayList<>();
        closedSet = new ArrayList<>();
        graph = g;
        openSet.add(start);
        this.start = start;
        this.goal = goal;
        start.setG(0);
        heuristicEstimate(null,start);
        this.totalPath = new ArrayList<Vertex>();
        
    }
    
    public ArrayList<Vertex> pathFinding(){
        while(openSet.size()!=0){
            Vertex current = lowestF(openSet);
            if(current.equals(goal)){
                return reconstructPath(current);
            }
            
            openSet.remove(current);
            closedSet.add(current);
         
               
            ArrayList<Vertex> neighbors = graph.getNeighbors(current);
            double tGscore;
            for(int i=0; i<neighbors.size(); i++){
                Vertex neighbor = neighbors.get(i);
                
                if(!closedSet.contains(neighbor)){
                    //we don't have edges so how we do distance??
                    tGscore = current.getG() + 1;
                    if(!openSet.contains(neighbor)){
                        heuristicEstimate(current,neighbor);
                        openSet.add(neighbor);
                        neighbor.setCameFrom(current);
                        
                    }
                    else{
                        if(tGscore <= neighbor.getG()){
                            neighbor.setCameFrom(current);
                            neighbor.setG(tGscore);
                            neighbor.calcF();
                            heuristicEstimate(current,neighbor);
                           
                        }
                    }
                }
            }
        }
        return null;
    }
    
    //add heuristic here? height/distance?
    public void heuristicEstimate(Vertex previous, Vertex x){
        Vector3f start =x.getTriangle().getCenter();
        Vector3f end =goal.getTriangle().getCenter();

        x.setDistance(Math.sqrt(Math.pow(start.x - end.x,2)+Math.pow(start.y - end.y,2)+Math.pow(start.z - end.z,2)));

        x.setH(previous);
        if(x.getAngle()>=90 )
            x.setH(1000000000);
        x.calcF();
       
    }
    
    public Vertex lowestF (ArrayList<Vertex> openSet){
        Vertex current = openSet.get(0);
        for(int i=1; i<openSet.size(); i++){
            if(current.getF() > openSet.get(i).getF()){
                current = openSet.get(i);
            }
        }
        return current;
    }
    
    public ArrayList<Vertex> reconstructPath(Vertex current){
        totalPath.add(current);
        System.out.println(start+"AND"+current);
        while(!current.equals(start)){
            
            current = current.getCameFrom();
            
            totalPath.add(current);            
        }
        ArrayList<Vertex> path = new ArrayList();
        for(int i=totalPath.size()-1; i>=0; i--){
            path.add(totalPath.get(i));
        }
        return path;
    }
    
}
