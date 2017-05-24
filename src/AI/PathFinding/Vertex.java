/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.PathFinding;

import com.jme3.math.Triangle;
import java.util.ArrayList;

/**
 *
 * @author carolley
 */
public class Vertex {
    
    private double f;
    private double h;
    private double g;
    private double time;
    private Vertex cameFrom;
    private Triangle triangle;
    private double height;
    private double angle;
    private double distance;
    private ArrayList<Vertex> neighbors;

    
    public Vertex(Triangle triangle){
        this.g = Integer.MAX_VALUE;
        this.f = Integer.MAX_VALUE;
        this.time = 0.0;
        this.neighbors = new ArrayList();
        this.triangle = triangle;
    }
    
    public void incrementTimeNeighbour(double time){
        for(Vertex v: neighbors){
            v.setTime(time);
        }
    }

    public double getHeight() {
        return height;
    }
    public void incrementTime(){
        time+=1;
    }
    public void resetTime(){
        time = 0;
    }
    public void setHeight(double height) {
        this.height = height;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
    
    public Triangle getTriangle(){
        return triangle;
    }
    
    public double getF(){
        return this.f;
    }
    
    public double getG(){
        return g;
    }
    
    public void setG(double g){
        this.g = g;
    }
    
    public void setF(double f){
        this.f = f;
    }
    
    
    public double calcF() {
        this.f = g + h;
        return f;
    } 
    
    public double getH(){
        return h;
    }
    
    public void setCameFrom(Vertex c){
        this.cameFrom = c;
    }
    
    public Vertex getCameFrom(){
        return cameFrom;
    }
    
    public void setTime(double time){
        this.time = time;
    }
    
    public double getTime(){
        return this.time;
    }
    
    public ArrayList<Vertex> getNeighbors(){
        return neighbors;
    }
    
    public void addNeighbor(Vertex c){
        neighbors.add(c);
        
    }
    
    
    public void setH(Vertex previous){
        if (previous==null)
            return;
            
        if(previous.getHeight()>height)
            h = distance - angle;
        else
            h = distance + angle;
        
        
    }

    public void setH(int x){
       h = x;
        
    }
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    
    
}









/*

 <group>
            <file>file:/C:/Users/carolley/Documents/GitHub/Pursuit/src/PathfindingAlg/Graph.java</file>
            <file>file:/C:/Users/carolley/Documents/GitHub/Pursuit/src/PathfindingAlg/Node.java</file>
            <file>file:/C:/Users/carolley/Documents/GitHub/Pursuit/src/PathfindingAlg/Astar.java</file>
        </group>

*/
