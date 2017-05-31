/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.PathFinding;

import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import java.util.ArrayList;

/**
 *
 * @author carolley
 */
public class GraphA {
   private ArrayList<Vertex> vertices;
   private Vertex end;
   
   
   public GraphA(){
       this.vertices = new ArrayList();
      
       
   }
   public GraphA(Mesh m){
      int l = m.getTriangleCount();
      Triangle[] triangleList = new Triangle[l];
      for(int i=0;i<l;i++){
          triangleList[i] = new Triangle();
          m.getTriangle(i, triangleList[i]);
          //System.out.println(triangles[i].get1()+", "+triangles[i].get2()+", "+triangles[i].get3());
      }
      System.out.println("HERE");
     this.vertices = new ArrayList();
           System.out.println(triangleList.length);

       for(int i=0;i<triangleList.length;i++){
            System.out.println("HERE"+i);

            Triangle current = triangleList[i];
            Vertex currentVertex;
            if(notExist(current)){
                
                 currentVertex = new Vertex(current);
                  insertVertex(currentVertex);
                 
                 
                 
            }else{
                currentVertex = getVertexFromTriangle(current);
            }
            setValue(currentVertex);
            for(int j=i+1;j<triangleList.length;j++){
                Triangle secondTriangle = triangleList[j];
                Vertex secondVertex;
                if(notExist(secondTriangle)){
                    secondVertex = new Vertex(secondTriangle);
                    insertVertex(secondVertex);
                 
                }else{
                    secondVertex = getVertexFromTriangle(secondTriangle);
                }
                 
                if(isNeighbour(current,secondTriangle)){
                    insertEdge(currentVertex,secondVertex);
                    Triangle tri = currentVertex.getTriangle();
                    Triangle tri1 = secondVertex.getTriangle();
                    
                    //System.out.println(tri.get1()+","+tri.get2()+","+tri.get3()+"ADDED"+tri1.get1()+","+tri1.get2()+","+tri1.get3());
                }
                
            }
            
        }
             System.out.println("HERE2");

       
   }
   public GraphA(Triangle[] triangleList){
       this.vertices = new ArrayList();
       for(int i=0;i<triangleList.length;i++){
            
            Triangle current = triangleList[i];
            Vertex currentVertex;
            if(notExist(current)){
                
                 currentVertex = new Vertex(current);
                  insertVertex(currentVertex);
                 
                 
                 
            }else{
                currentVertex = getVertexFromTriangle(current);
            }
            setValue(currentVertex);
            for(int j=i+1;j<triangleList.length;j++){
                Triangle secondTriangle = triangleList[j];
                Vertex secondVertex;
                if(notExist(secondTriangle)){
                    secondVertex = new Vertex(secondTriangle);
                    insertVertex(secondVertex);
                 
                }else{
                    secondVertex = getVertexFromTriangle(secondTriangle);
                }
                 
                if(isNeighbour(current,secondTriangle)){
                    insertEdge(currentVertex,secondVertex);
                    Triangle tri = currentVertex.getTriangle();
                    Triangle tri1 = secondVertex.getTriangle();
                    
                    //System.out.println(tri.get1()+","+tri.get2()+","+tri.get3()+"ADDED"+tri1.get1()+","+tri1.get2()+","+tri1.get3());
                }
                
            }
            
        }
   }
   
   public void incrementTime(){
       for(Vertex v: vertices){
           v.incrementTime();
       }
   }
    private boolean isNeighbour(Triangle first, Triangle second){
        if(first.get1().equals(second.get1()) || first.get1().equals(second.get2()) || first.get1().equals(second.get3())
                || first.get2().equals(second.get1()) || first.get2().equals(second.get2()) || first.get2().equals(second.get3())
                    || first.get3().equals(second.get1()) || first.get3().equals(second.get2()) || first.get3().equals(second.get3())){
            return true;
        }
        return false;         
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
        
            Vector3f start = v.getTriangle().getCenter();
            // ONLY FOR FLAT SURFACE, TO CHANGE FOR PLANET
            v.setAngle(getAngle(new Vector3f(0,1,0),v.getTriangle().getNormal()));      
            v.setHeight(start.getY()); 
            //v.setDistance(Math.sqrt(Math.pow(start.x - end.x,2)+Math.pow(start.y - end.y,2)+Math.pow(start.z - end.z,2)));
     
        
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

        System.out.println("Height value: "+v.getHeight());
        System.out.println("Distance value: "+v.getDistance());
        System.out.println("Angle value: "+v.getAngle());


        for (Vertex neighbor : v.getNeighbors()) {
            System.out.println(neighbor.getTriangle().get1()+","+neighbor.getTriangle().get2()+","+neighbor.getTriangle().get3());
        }
        
    }

    public ArrayList<Vertex> getCopyVertexList() {
       ArrayList<Vertex> copy = new  ArrayList<Vertex>();
       for(Vertex v: vertices){
           copy.add(v);
       }
       return copy;
    }

    public void setVerticesSafe() {
        
        for(Vertex v: vertices){
            v.setSafe(true);
        }
        
    }
    
    
    
}
