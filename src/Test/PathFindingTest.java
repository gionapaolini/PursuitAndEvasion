/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import AI.PathFinding.AstarAlg;
import AI.PathFinding.GraphA;
import AI.PathFinding.Vertex;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Triangle;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;

/**
 *
 * @author giogio
 */
public class PathFindingTest extends SimpleApplication{

    public static void main(String[] args){
        MeshTest app = new MeshTest();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        cam.setLocation(new Vector3f(3,10,3));
        cam.lookAt(new Vector3f(0,0,0), Vector3f.UNIT_Y);
       
        Mesh m = new Mesh();
        
        // Vertex positions in space
        Vector3f [] vertices = new Vector3f[20];
        vertices[0] = new Vector3f(0,0,0);
        vertices[1] = new Vector3f(3,0,0);
        //ADDING CUBE
        vertices[2] = new Vector3f(3,3,0);
        
        vertices[3] = new Vector3f(6,0,0);
        //ADDING CUBE
        vertices[4] = new Vector3f(6,3,0);
        
        vertices[5] = new Vector3f(9,0,0);
        
        vertices[6] = new Vector3f(0,0,3);
        vertices[7] = new Vector3f(3,0,3);
        //ADDING CUBE
        vertices[8] = new Vector3f(3,3,3);
        
        vertices[9] = new Vector3f(6,0,3);
         //ADDING CUBE
        vertices[10] = new Vector3f(6,3,3);
        
        vertices[11] = new Vector3f(9,0,3);
        
        vertices[12] = new Vector3f(0,0,6);
        vertices[13] = new Vector3f(3,0,6);
        vertices[14] = new Vector3f(6,0,6);
        vertices[15] = new Vector3f(9,0,6);
        
        vertices[16] = new Vector3f(0,0,9);
        vertices[17] = new Vector3f(3,0,9);
        vertices[18] = new Vector3f(6,0,9);
        vertices[19] = new Vector3f(9,0,9);
        
        
        
        

        // Texture coordinates
        Vector2f [] texCoord = new Vector2f[20];
        texCoord[0] = new Vector2f(0,0);
        texCoord[1] = new Vector2f(0.2f,0);
        texCoord[2] = new Vector2f(0.4f,0.2f);
        texCoord[3] = new Vector2f(0.8f,0);
        texCoord[4] = new Vector2f(0.6f,0.2f);
        texCoord[5] = new Vector2f(1,0);
        
        
        texCoord[6] = new Vector2f(0,0.33f);
        texCoord[7] = new Vector2f(.2f,0.6f);
        texCoord[8] = new Vector2f(0.4f,0.4f);
        texCoord[9] = new Vector2f(0.8f,0.6f);
        texCoord[10] = new Vector2f(0.6f,0.4f);
        texCoord[11] = new Vector2f(1,0.33f);
        
        texCoord[12] = new Vector2f(0,0.66f);
        texCoord[13] = new Vector2f(.3f,0.8f);
        texCoord[14] = new Vector2f(0.66f,0.8f);
        texCoord[15] = new Vector2f(1,0.66f);
        texCoord[16] = new Vector2f(0,1);
        texCoord[17] = new Vector2f(0.33f,1);
        
        
        texCoord[18] = new Vector2f(0.66f,1);
        texCoord[19] = new Vector2f(1,1);
       
        

        // Indexes. We define the order in which mesh should be constructed
     
        short[] indexes = {
            
            
                           1,0,6,6,7,1,
                           1,7,2,7,8,2,
                           //3,2,6,6,7,3,
                           2,8,4,8,10,4,
                           10,9,3,3,4,10,
                           3,9,5,5,9,11,
                           8,7,10,10,7,9,
                           6,12,7,7,12,13,
                           9,7,13,13,14,9,
                           9,14,11,11,14,15,
                           12,16,13,13,16,17,
                           13,17,14,14,17,18,
                           14,18,15,15,18,19,
                           
                           
                           
                       
                           
                    
        };
        

        // Setting buffers
        m.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        m.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        m.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createShortBuffer(indexes));
        m.updateBound();

        // *************************************************************************
        // First mesh uses one solid color
        // *************************************************************************

        // Creating a geometry, and apply a single color material to it
        Geometry geom = new Geometry("OurMesh", m);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);

        // Attaching our geometry to the root node.
       // rootNode.attachChild(geom);

        // *************************************************************************
        // Second mesh uses vertex colors to color each vertex
        // *************************************************************************
        Mesh cMesh = m.clone();
        Geometry coloredMesh = new Geometry ("ColoredMesh", cMesh);
        Material matVC = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matVC.setBoolean("VertexColor", true);

        //We have 4 vertices and 4 color values for each of them.
        //If you have more vertices, you need 'new float[yourVertexCount * 4]' here!
        float[] colorArray = new float[4*4];
        int colorIndex = 0;

        //Set custom RGBA value for each Vertex. Values range from 0.0f to 1.0f
        for(int i = 0; i < 4; i++){
           // Red value (is increased by .2 on each next vertex here)
           colorArray[colorIndex++]= 0.1f+(.2f*i);
           // Green value (is reduced by .2 on each next vertex)
           colorArray[colorIndex++]= 0.9f-(0.2f*i);
           // Blue value (remains the same in our case)
           colorArray[colorIndex++]= 0.5f;
           // Alpha value (no transparency set here)
           colorArray[colorIndex++]= 1.0f;
        }
        // Set the color buffer
        cMesh.setBuffer(VertexBuffer.Type.Color, 4, colorArray);
        coloredMesh.setMaterial(matVC);
        // move mesh a bit so that it doesn't intersect with the first one
        coloredMesh.setLocalTranslation(4, 0, 0);
        //rootNode.attachChild(coloredMesh);

//        /** Alternatively, you can show the mesh vertixes as points
//          * instead of coloring the faces. */
//        cMesh.setMode(Mesh.Mode.Points);
//        cMesh.setPointSize(10f);
//        cMesh.updateBound();
//        cMesh.setStatic();
//        Geometry points = new Geometry("Points", m);
//        points.setMaterial(mat);
//        rootNode.attachChild(points);

        // *************************************************************************
        // Third mesh will use a wireframe shader to show wireframe
        // *************************************************************************
        Mesh wfMesh = m.clone();
        Geometry wfGeom = new Geometry("wireframeGeometry", wfMesh);
        Material matWireframe = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matWireframe.setColor("Color", ColorRGBA.Green);
        matWireframe.getAdditionalRenderState().setWireframe(true);
        wfGeom.setMaterial(matWireframe);
        wfGeom.setLocalTranslation(4, 4, 0);
        rootNode.attachChild(wfGeom);
        int l = m.getTriangleCount();
        Triangle[] triangles = new Triangle[l];
        for(int i=0;i<l;i++){
            triangles[i] = new Triangle();
            m.getTriangle(i, triangles[i]);
            //System.out.println(triangles[i].get1()+", "+triangles[i].get2()+", "+triangles[i].get3());
        }
        GraphA graph = generateGraph(triangles);
        graph.getInfoOnVertex(0);
        graph.getInfoOnVertex(15);
        

        AstarAlg astar = new AstarAlg(graph.getVertices().get(0),graph.getVertices().get(9), graph);
        ArrayList<Vertex> list = astar.pathFinding();
        for(Vertex x: list){
            System.out.println(x.getTriangle().get1()+" "+x.getTriangle().get2()+" "+x.getTriangle().get3());
        }
        
        
    
    }
    
    
 
    
    private GraphA generateGraph(Triangle[] triangleList){
        
        
       GraphA graph = new GraphA();
       for(int i=0;i<triangleList.length;i++){
            
            Triangle current = triangleList[i];
            Vertex currentVertex;
            if(graph.notExist(current)){
                
                 currentVertex = new Vertex(current);
                 graph.insertVertex(currentVertex);
                 
                 
                 
            }else{
                currentVertex = graph.getVertexFromTriangle(current);
            }
            graph.setValue(currentVertex);
            for(int j=i+1;j<triangleList.length;j++){
                Triangle secondTriangle = triangleList[j];
                Vertex secondVertex;
                if(graph.notExist(secondTriangle)){
                    secondVertex = new Vertex(secondTriangle);
                    graph.insertVertex(secondVertex);
                 
                }else{
                    secondVertex = graph.getVertexFromTriangle(secondTriangle);
                }
                 
                if(isNeighbour(current,secondTriangle)){
                    graph.insertEdge(currentVertex,secondVertex);
                    Triangle tri = currentVertex.getTriangle();
                    Triangle tri1 = secondVertex.getTriangle();
                    
                    //System.out.println(tri.get1()+","+tri.get2()+","+tri.get3()+"ADDED"+tri1.get1()+","+tri1.get2()+","+tri1.get3());
                }
                
            }
            
        }
       return graph;
    }
    
    
    
}
