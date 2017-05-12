package Test;

import com.jme3.app.SimpleApplication;
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
import com.jme3.scene.Node;

import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    Geometry agent, planet;
    Material red,blue;
    Node agentNode;

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
        Sphere world = new Sphere(100,100, 50);
        Sphere sphere = new Sphere(100,100,3);
        
        agentNode = new Node();
      
        planet = new Geometry("World", world);
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
        initKeys();

    }

    @Override
    public void simpleUpdate(float tpf) {
   
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

        agentNode.setLocalTranslation(contactPoint);
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
        System.out.println(n.x+"*X"+n.y+"*Y"+n.z+"*Z = "+sums);
        
    }
    
    
    public double getAngle(Vector3f normal, Vector3f direction){
       
       
        double upper = normal.dot(direction);
      
        double lenghtNormal = normal.length();
        double lenghtDirection = normal.length();
        double cos = upper/(lenghtNormal*lenghtDirection);
        double angle = Math.acos(cos);
        System.out.println(Math.toDegrees(angle));
        return Math.toDegrees(angle);
        
    }
    
    public Vector3f getProjectionOntoPlane(Vector3f n, Vector3f v){
        
    
        Vector3f projection = n.cross(v.cross(n));
      
        System.out.println(projection);
        
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
}
