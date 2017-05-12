/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.PathFinding;

import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author carolley
 */
public class GraphA {
   private ArrayList<Vertex> vertices;
   
   
   public GraphA(){
       this.vertices = new ArrayList();
       
   }
   
   public void insertVertex(Vertex a){
       vertices.add(a);
   }
   
   public void removeVertex(Vertex a){
       vertices.remove(a);
   }
   
   public void insertEdge(Vertex a, Vertex b){
       a.addNeighbor(b);
       b.addNeighbor(a);
   }
   
   public Vertex getVertexFromTriangle(Triangle current){
       ArrayList<Vertex> vertices = getVertices();
        for(Vertex v: vertices){
            if(v.getTriangle().equals(current))
                return v;
        }
        return null;
   }

   
   /*
    
    If true then the triangle already exist in the graph 
    
    */
    
   
    public boolean notExist(Triangle current){
     
        //System.out.println(current.get1()+","+current.get2()+","+current.get3());
        ArrayList<Vertex> vertices = getVertices();
        for(Vertex v: vertices){
            if(v.getTriangle().equals(current))
                return false;
        }
        
        return true;
    }   
   public void removeEdge(Vertex a, Vertex b){
       a.getNeighbors().remove(b);
       b.getNeighbors().remove(a);
   }
   
   public boolean isAdjacent(Vertex a, Vertex b){
       if(a.getNeighbors().size() < b.getNeighbors().size()){
           if(a.getNeighbors().contains(b)){
               return true;
           }
       }
       else{
           if(b.getNeighbors().contains(a)){
               return true;
           }
       }
       return false;
   }
   
   public ArrayList<Vertex> getNeighbors(Vertex a){
       return a.getNeighbors();
   }
   
   public ArrayList<Vertex> getVertices(){
       return vertices;
   }
   
   public int numVertices(){
       return vertices.size();
   }

     public void setValue(Vertex v){
        
           // System.out.println(triangleList[j].get1()+", "+triangleList[j].get2()+", "+triangleList[j].get3());
            //System.out.println("Angle Heuristic:"+getAngle(new Vector3f(0,1,0),triangleList[j].getNormal()));
            Vector3f start = v.getTriangle().getCenter();
            Vector3f end = new Vector3f(0,1,0);
            double distance = Math.sqrt(Math.pow(start.x - end.x,2)+Math.pow(start.y - end.y,2)+Math.pow(start.z - end.z,2));
            double angle =0;
//double angle = getAngle(new Vector3f(0,1,0),v.getTriangle().getNormal());
          //  System.out.println("Distance Heuristic: "+Math.sqrt(Math.pow(start.x - end.x,2)+Math.pow(start.y - end.y,2)+Math.pow(start.z - end.z,2)));
            
          //  System.out.println();
            v.setH(distance+angle);
     
        
    }
    
    public double getAngle(Vector3f normal, Vector3f direction){
       
       
        double upper = normal.dot(direction);
      
        double lenghtNormal = normal.length();
        double lenghtDirection = normal.length();
        double cos = upper/(lenghtNormal*lenghtDirection);
        double angle = Math.acos(cos);
        //System.out.println(Math.toDegrees(angle));
        return Math.toDegrees(angle);
        
    }
   
    
    public void getInfoOnVertex(int i){
        Vertex v = vertices.get(i);
       System.out.println(v.getTriangle().get1()+","+v.getTriangle().get2()+","+v.getTriangle().get3());

        System.out.println("H value: "+v.getH());
        for (Vertex neighbor : v.getNeighbors()) {
            System.out.println(neighbor.getTriangle().get1()+","+neighbor.getTriangle().get2()+","+neighbor.getTriangle().get3());
        }
        
    }
    
}
