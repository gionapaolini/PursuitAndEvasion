/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.PathFinding;

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
    
    public AstarAlg(Vertex start, Vertex goal){
        openSet.add(start);
        this.start = start;
        this.goal = goal;
        start.setG(0);
        start.setF(heuristicEstimate(start, goal));
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
                        openSet.add(neighbor);
                    }
                    else{
                        if(tGscore <= neighbor.getG()){
                            neighbor.setCameFrom(current);
                            neighbor.setG(tGscore);
                            double newF = neighbor.calcF(goal);
                            neighbor.setF(newF);
                        }
                    }
                }
            }
        }
        return null;
    }
    
    //add heuristic here? height/distance?
    public double heuristicEstimate(Vertex x, Vertex y){
        double heuristic = 0;
        return heuristic;
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
