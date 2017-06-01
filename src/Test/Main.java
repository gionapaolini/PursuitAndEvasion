package Test;

import AI.PathFinding.GraphA;
import AI.PathFinding.Vertex;
import GameLogic.Match;
import GameLogic.Pursuer;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;

import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    Geometry agent, planet, mesh;
    Material red,blue;
    Node agentNode, pyramidNode, Dtriangles;
    Vector3f lastNormal;
    GraphA graph;
    Vector3f[] pointsPyramid;
    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(true);
        settings.setResolution(800,400);
        app.setShowSettings(false); // splashscreen
        app.setSettings(settings);
            
        app.start();
    }

    @Override
    public void simpleInitApp() {
         flyCam.setMoveSpeed(20f);
         flyCam.setDragToRotate(true);
       cam.setLocation(new Vector3f(0,100,100));
        cam.lookAt(new Vector3f(0,0,0), Vector3f.UNIT_Y);
        Sphere world = new Sphere(100,100, 20);
        Sphere sphere = new Sphere(100,100,3);
        
        agentNode = new Node();
      
        planet = new Geometry("World", world);
        mesh = makeSphere(assetManager,"mesh",0,0,0);
        agent = new Geometry("Agent", sphere);
        agent.setLocalTranslation(0, 0, 0);
        red = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        red.setColor("Color", ColorRGBA.Red);
        blue = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blue.setColor("Color", ColorRGBA.Blue);
        planet.setMaterial(red);
        agent.setMaterial(blue);
        
        
        
        rootNode.attachChild(planet);
        agentNode.attachChild(agent);
        agentNode.setLocalTranslation(0, 53, 0);
        rootNode.attachChild(agentNode);
        pyramidNode = new Node();
        rootNode.attachChild(pyramidNode);
        initKeys();
        lastNormal = new Vector3f(0,1,0);
        rootNode.attachChild(mesh);
        graph = new GraphA(mesh.getMesh());
        Dtriangles = new Node();
        rootNode.attachChild(Dtriangles);
        
        pointsPyramid = new Vector3f[5];
        
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        
        
        
            rootNode.detachChildNamed("line1");
            rootNode.detachChildNamed("line2");
            rootNode.detachChildNamed("line3");
            rootNode.detachChildNamed("line4");
            rootNode.detachChildNamed("line5");
            rootNode.detachChildNamed("line6");
            rootNode.detachChildNamed("line7");
            rootNode.detachChildNamed("line8");
            rootNode.detachChildNamed("line9");
            rootNode.detachChildNamed("line10");
            rootNode.detachChildNamed("line11");
            rootNode.detachChildNamed("line12");
            
            buildPyramid(agentNode.getLocalTranslation(),agentNode.getLocalRotation().getRotationColumn(2), lastNormal);
            selectDangerousTriangles();
            showDangerousTriangles();
            
   /*
        
       
            agentNode.detachChildNamed("l1");
            agentNode.detachChildNamed("l2");

            Ray ray1 = new Ray(agent.getLocalTranslation(), agent.getLocalRotation().getRotationColumn(2));

            Quaternion rotation = new Quaternion();
            rotation.fromAngleAxis((float) Math.toRadians((int)(90/2)),Vector3f.UNIT_Y);
            Vector3f firstLimit = rotation.mult(ray1.getDirection());
            rotation.fromAngleAxis((float) Math.toRadians((int)(-90/2)),Vector3f.UNIT_Y);
            Vector3f secondLimit = rotation.mult(ray1.getDirection());

            Geometry l1 = new Geometry("l1", new Line(ray1.getOrigin(), firstLimit.mult(10)));
            Geometry l2 = new Geometry("l2", new Line(ray1.getOrigin(), secondLimit.mult(10)));

            l1.setMaterial(blue);
            l2.setMaterial(blue);

            agentNode.attachChild(l1);
            agentNode.attachChild(l2);
            */
        
        
    }
    
    private void reposition(Vector3f contactPoint, Vector3f normalPoint){
         
        
        agentNode.setLocalTranslation(contactPoint);
        //System.out.println(results.getFarthestCollision().getContactNormal());
        Vector3f rotation = getProjectionOntoPlane(normalPoint,agentNode.getLocalRotation().getRotationColumn(2));
        Quaternion rotationQuat = new Quaternion();
        rotationQuat.lookAt(rotation,normalPoint);
        agentNode.setLocalRotation(rotationQuat);
        
    }
    
    private void reposition(){
       Ray ray = new Ray(planet.getLocalTranslation(),agentNode.getLocalTranslation());
        CollisionResults results = new CollisionResults();
        planet.collideWith(ray, results);
        Vector3f contactPoint = results.getFarthestCollision().getContactPoint();
        Vector3f normalPoint = results.getFarthestCollision().getContactNormal();
        lastNormal = normalPoint;
        float length = contactPoint.length();
        Vector3f newLocation = contactPoint.normalize().mult(length+2);
        agentNode.setLocalTranslation(newLocation);
       
        //System.out.println(results.getFarthestCollision().getContactNormal());
        Vector3f rotation = getProjectionOntoPlane(normalPoint,agentNode.getLocalRotation().getRotationColumn(2));
        Quaternion rotationQuat = new Quaternion();
        rotationQuat.lookAt(rotation,normalPoint);
        agentNode.setLocalRotation(rotationQuat);
        
    }
    
    
    
    public void getPlaneCoord(){
        Vector3f cp = new Vector3f(1,1,1);
        Vector3f n = new Vector3f(0,1,0);
        
        float sums = cp.dot(n);
       // System.out.println(n.x+"*X"+n.y+"*Y"+n.z+"*Z = "+sums);
        
    }
    
    
    public double getAngle(Vector3f normal, Vector3f direction){
       
       
        double upper = normal.dot(direction);
      
        double lenghtNormal = normal.length();
        double lenghtDirection = normal.length();
        double cos = upper/(lenghtNormal*lenghtDirection);
        double angle = Math.acos(cos);
     //   System.out.println(Math.toDegrees(angle));
        return Math.toDegrees(angle);
        
    }
    
    public Vector3f getProjectionOntoPlane(Vector3f n, Vector3f v){
        
    
        Vector3f projection = n.cross(v.cross(n));
      
  //      System.out.println(projection);
        
        return projection;
    }
    public void moveTo(Vector3f point){
        Ray ray = new Ray(planet.getLocalTranslation(),agentNode.getLocalTranslation());
        CollisionResults results = new CollisionResults();
        planet.collideWith(ray, results);
        Vector3f contactPoint = results.getFarthestCollision().getContactPoint();
        Vector3f normalPoint = results.getFarthestCollision().getContactNormal();
        
        Vector3f direction = getDirectionToPoint(normalPoint,point);
        
        Quaternion rotationQuat = new Quaternion();
        rotationQuat.lookAt(direction,normalPoint);
        agentNode.setLocalRotation(rotationQuat);
        
        reposition(contactPoint,normalPoint);
    }
    public Vector3f getDirectionToPoint(Vector3f normal, Vector3f point){
        Vector3f pointNewOrigin = point.add(agentNode.getLocalTranslation().mult(-1));
        //projection of the point
        Vector3f projection = getProjectionOntoPlane(normal,pointNewOrigin);
        return projection;
        
    }
    
     private void initKeys() {
    // You can map one or several inputs to one named action
    inputManager.addMapping("Left",   new KeyTrigger(KeyInput.KEY_J));
    inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE));   
    inputManager.addListener(analogListener,"Left", "Right", "Rotate");
    
    inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // trigger 2: left-button click
    inputManager.addListener(actionListener, "Shoot");

  }

  private AnalogListener analogListener = new AnalogListener() {
    public void onAnalog(String name, float value, float tpf) {
     
        if (name.equals("Rotate")) {
          agentNode.rotate(0, value*speed, 0);
        }
        if (name.equals("Left")) {
          Vector3f forward = agentNode.getLocalRotation().mult(Vector3f.UNIT_Z).mult(tpf * 20);
          agentNode.move(forward);
          reposition();
             
        }
      
    }
  };
  
  private ActionListener actionListener = new ActionListener() {

    public void onAction(String name, boolean keyPressed, float tpf) {
      if (name.equals("Shoot") && !keyPressed) {
          
          
          
Vector2f mouseCoords = new Vector2f(inputManager.getCursorPosition());

    //	System.out.println(mouseCoords);

    Ray ray = new Ray(cam.getWorldCoordinates(mouseCoords, 0),

    cam.getWorldCoordinates(mouseCoords, 1).subtractLocal(

    cam.getWorldCoordinates(mouseCoords, 0)).normalizeLocal());
          
          
          
          
          
        // 1. Reset results list.
        CollisionResults results = new CollisionResults();
        // 2. Aim the ray from cam loc to cam direction.
      
        // 3. Collect intersections between Ray and Shootables in results list.
        // DO NOT check collision with the root node, or else ALL collisions will hit the skybox! Always make a separate node for objects you want to collide with.
        planet.collideWith(ray, results);
       
        // 5. Use the results (we mark the hit object)
        if (results.size() > 0) {
          
          CollisionResult closest = results.getClosestCollision();
          // Let's interact - we mark the hit with a red dot.
          moveTo(closest.getContactPoint());
          
        } 
      }
    }
  };
  
 

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    
    public void buildPyramid(Vector3f eyePosition,Vector3f direction,Vector3f upVector){
         
         direction.normalize();
         
         Vector3f rightVector = direction.cross(upVector);
         float nearDistance = 5;
         float farDistance = 40;
         float fov=(float) Math.toRadians(90);
         float aspectRatio = 16/12;
         
         float Hnear = (float) (2 * Math.tan(fov/2) * nearDistance);
         float Wnear = Hnear*aspectRatio;
         
         float Hfar = (float) (2 * Math.tan(fov/2) * farDistance);
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
         
         
         pointsPyramid[0] = eyePosition;
         pointsPyramid[1] = FTL;
         pointsPyramid[2] = FTR;
         pointsPyramid[3] = FBL;
         pointsPyramid[4] = FBR;
         
         Geometry line1 = new Geometry("line1", new Line(eyePosition, FTL));
         Geometry line2 = new Geometry("line2", new Line(eyePosition, FTR));
         Geometry line3 = new Geometry("line3", new Line(eyePosition, FBL));
         Geometry line4 = new Geometry("line4", new Line(eyePosition, FBR));
         
         Geometry line5 = new Geometry("line5", new Line(FTL, FTR));
         Geometry line6 = new Geometry("line6", new Line(FTL, FBL));
         Geometry line7 = new Geometry("line7", new Line(FBL, FBR));
         Geometry line8 = new Geometry("line8", new Line(FBR, FTR));
         
         Geometry line9 = new Geometry("line9", new Line(NTL, NTR));
         Geometry line10 = new Geometry("line10", new Line(NTL, NBL));
         Geometry line11 = new Geometry("line11", new Line(NBL, NBR));
         Geometry line12 = new Geometry("line12", new Line(NBR, NTR));
         
         
         
            line1.setMaterial(blue);
            line2.setMaterial(blue);
            line3.setMaterial(blue);
            line4.setMaterial(blue);
            line5.setMaterial(blue);
            line6.setMaterial(blue);
            line7.setMaterial(blue);
            line8.setMaterial(blue);
            line9.setMaterial(blue);
            line10.setMaterial(blue);
            line11.setMaterial(blue);
            line12.setMaterial(blue);
            
            
        rootNode.attachChild(line1);
        rootNode.attachChild(line2);
        rootNode.attachChild(line3);
        rootNode.attachChild(line4);
        rootNode.attachChild(line5);
        rootNode.attachChild(line6);
        rootNode.attachChild(line7);
        rootNode.attachChild(line8);
        rootNode.attachChild(line9);
        rootNode.attachChild(line10);
        rootNode.attachChild(line11);
        rootNode.attachChild(line12);

         
     }
    
    
    
     protected Geometry makeSphere(AssetManager assetManager,String name, float x, float y, float z) {
        Sphere sphere = new Sphere(10, 10, 21.5f);
        Geometry ball = new Geometry(name, sphere);
       // ball.scale(0.05f);
        ball.setLocalTranslation(x, y, z);
         Material matWireframe = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matWireframe.setColor("Color", ColorRGBA.Green);
        matWireframe.getAdditionalRenderState().setWireframe(true);
        ball.setMaterial(matWireframe);
      
        if(ball==null)
            System.out.println("WHAT");
      //  ball.lookAt(box.getLocalTranslation(), Vector3f.UNIT_Y);
        return ball;
    }
     
     
     
    private void showDangerousTriangles() {
        Dtriangles.detachAllChildren();
        ArrayList<Vertex> vertices = graph.getVertices();
        for(Vertex v: vertices){
            if(v.isSafe())
                continue;
            Mesh m = new Mesh();
            Vector3f[] points = { v.getTriangle().get1(), v.getTriangle().get2(),v.getTriangle().get3()};
            Vector2f[] textCoord = {new Vector2f(1,1),new Vector2f(1,1),new Vector2f(1,1)}; 
            int[] indices = {2,0,1};
            
            m.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(points));
            m.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(textCoord));
            m.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indices));
            m.updateBound();
            Geometry geo = new Geometry("triangle", m); // using our custom mesh object
            Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", ColorRGBA.Yellow);
            geo.setMaterial(mat);
            Dtriangles.attachChild(geo);
        }
    }
    
     public boolean insidePyramid(Vector3f object){
       
       int cnt = 0;
       Ray ray = new Ray(object, Vector3f.UNIT_Y);
     
     
        if ( ray.intersectWherePlanar(pointsPyramid[0], pointsPyramid[1], pointsPyramid[3], null) ) cnt++;
        if ( ray.intersectWherePlanar(pointsPyramid[0], pointsPyramid[1], pointsPyramid[2], null) ) cnt++;
        if (cnt>1) return false;
        if ( ray.intersectWherePlanar(pointsPyramid[0], pointsPyramid[2], pointsPyramid[4], null) ) cnt++;
        if (cnt>1) return false;
        if ( ray.intersectWherePlanar(pointsPyramid[0], pointsPyramid[3], pointsPyramid[4], null) ) cnt++;
        if (cnt>1) return false;
   
        
        if ( ray.intersectWherePlanar(pointsPyramid[1], pointsPyramid[2], pointsPyramid[3], null) ) cnt++;
        if (cnt>1) return false;
        if ( ray.intersectWherePlanar(pointsPyramid[2], pointsPyramid[3], pointsPyramid[4], null) ) cnt++;
        if (cnt>1) return false;
        if (cnt==1)
            return true;
        else //->(cnt==0 || cnt>1)
            return false;
        
    }
     


    private void selectDangerousTriangles() {
        
        

        
        for(Vertex v: graph.getVertices()){
            v.setSafe(true);
            if(insidePyramid(v.getTriangle().get1())){
                 Ray ray = new Ray(agentNode.getWorldTranslation(),v.getTriangle().get1().subtract(agentNode.getWorldTranslation()).normalizeLocal());                                
                 CollisionResults results = new CollisionResults();                 
                 planet.collideWith(ray, results);
                 if(results.size()<=0){
                    v.setUnsafe();
                    continue;
                 }
            }
            if(insidePyramid(v.getTriangle().get2())){
               Ray ray = new Ray(agentNode.getWorldTranslation(),v.getTriangle().get2().subtract(agentNode.getWorldTranslation()).normalizeLocal());                                
                 CollisionResults results = new CollisionResults();                 
                 planet.collideWith(ray, results);
                 if(results.size()<=0){
                    v.setUnsafe();
                    continue;
                 }
            }
            if(insidePyramid(v.getTriangle().get3())){
               Ray ray = new Ray(agentNode.getWorldTranslation(),v.getTriangle().get3().subtract(agentNode.getWorldTranslation()).normalizeLocal());                                
                 CollisionResults results = new CollisionResults();                 
                 planet.collideWith(ray, results);
                 if(results.size()<=0){
                    v.setUnsafe();
                    continue;
                 }
            }
        }

        
    }
}
