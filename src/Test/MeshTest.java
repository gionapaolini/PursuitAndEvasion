/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package Test;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Triangle;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.mesh.IndexBuffer;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;
import java.util.List;
import AI.PathFinding.GraphA;
import AI.PathFinding.Vertex;

/**
 * How to create custom meshes by specifying vertices
 * We render the mesh in three different ways, once with a solid blue color,
 * once with vertex colors, and once with a wireframe material.
 * @author KayTrance
 */
public class MeshTest extends SimpleApplication {

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
        Vector3f [] vertices = new Vector3f[16];
        vertices[0] = new Vector3f(0,0,0);
        vertices[1] = new Vector3f(3,0,0);
        vertices[2] = new Vector3f(6,0,0);
        vertices[3] = new Vector3f(9,0,0);
        
        vertices[4] = new Vector3f(0,0,3);
        vertices[5] = new Vector3f(3,0,3);
        vertices[6] = new Vector3f(6,0,3);
        vertices[7] = new Vector3f(9,0,3);
        
        vertices[8] = new Vector3f(0,0,6);
        vertices[9] = new Vector3f(3,0,6);
        vertices[10] = new Vector3f(6,0,6);
        vertices[11] = new Vector3f(9,0,6);
        
        vertices[12] = new Vector3f(0,0,9);
        vertices[13] = new Vector3f(3,0,9);
        vertices[14] = new Vector3f(6,0,9);
        vertices[15] = new Vector3f(9,0,9);
        
        
        
        

        // Texture coordinates
        Vector2f [] texCoord = new Vector2f[16];
        texCoord[0] = new Vector2f(0,0);
        texCoord[1] = new Vector2f(.33f,0);
        texCoord[2] = new Vector2f(0.66f,0);
        texCoord[3] = new Vector2f(1,0);
        
        texCoord[4] = new Vector2f(0,0.33f);
        texCoord[5] = new Vector2f(0.33f,0.33f);
        texCoord[6] = new Vector2f(0.66f,0.33f);
        texCoord[7] = new Vector2f(1,0.33f);
        
        texCoord[8] = new Vector2f(0,0.66f);
        texCoord[9] = new Vector2f(0.33f,0.66f);
        texCoord[10] = new Vector2f(0.66f,0.66f);
        texCoord[11] = new Vector2f(1,0.66f);
        
        texCoord[8] = new Vector2f(0,1);
        texCoord[9] = new Vector2f(0.33f,1);
        texCoord[10] = new Vector2f(0.66f,1);
        texCoord[11] = new Vector2f(1,1);
        

        // Indexes. We define the order in which mesh should be constructed
     
        short[] indexes = {1,0,4,4,5,1,
                           2,1,5,5,6,2,
                           3,2,6,6,7,3,
                           5,4,8,8,9,5,
                           6,5,9,9,10,6,
                           7,6,10,10,11,7,
                           9,8,12,12,13,9,
                           10,9,13,13,14,10,
                           11,10,14,14,15,11
                           
                    
        };

        

/*
 // Vertex positions in space
        Vector3f [] vertices = new Vector3f[4];
        vertices[0] = new Vector3f(0,0,0);
        vertices[1] = new Vector3f(3,0,0);
        vertices[2] = new Vector3f(0,3,0);
        vertices[3] = new Vector3f(3,3,0);

        // Texture coordinates
        Vector2f [] texCoord = new Vector2f[4];
        texCoord[0] = new Vector2f(0,0);
        texCoord[1] = new Vector2f(1,0);
        texCoord[2] = new Vector2f(0,1);
        texCoord[3] = new Vector2f(1,1);

        // Indexes. We define the order in which mesh should be constructed
        short[] indexes = {2, 0, 1, 1, 3, 2};
*/
        // Setting buffers
        m.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        m.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        m.setBuffer(Type.Index, 1, BufferUtils.createShortBuffer(indexes));
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
        cMesh.setBuffer(Type.Color, 4, colorArray);
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
        graph.getInfoOnVertex(6);
        
        
    
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
    
    
    
    
    private void setValue(Triangle[] triangleList){
        for(int j=0;j<triangleList.length;j++){
            System.out.println(triangleList[j].get1()+", "+triangleList[j].get2()+", "+triangleList[j].get3());
            System.out.println("Angle Heuristic:"+getAngle(new Vector3f(0,1,0),triangleList[j].getNormal()));
            Vector3f start = triangleList[j].getCenter();
            Vector3f end = new Vector3f(0,1,0);
            System.out.println("Distance Heuristic: "+Math.sqrt(Math.pow(start.x - end.x,2)+Math.pow(start.y - end.y,2)+Math.pow(start.z - end.z,2)));
            
            System.out.println();
            
     
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
}