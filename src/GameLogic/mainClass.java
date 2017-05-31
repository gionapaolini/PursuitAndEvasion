


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import AI.PathFinding.Vertex;
import GameLogic.Gui.StartScreen;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.control.BillboardControl;
import com.jme3.system.AppSettings;
import com.jme3.util.BufferUtils;

import de.lessvoid.nifty.Nifty;
import java.util.ArrayList;


/**
 *q
 * @author giogio
 */

public class mainClass extends SimpleApplication {

    Match match;
    StartScreen startScreen;
    Node[] bb;
    BitmapText[] times;
    BillboardControl[] control;
    Node Dtriangles;
    public static void main(String[] args) {
        mainClass app = new mainClass();
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1200, 520);
        app.setShowSettings(false); // splashscreen
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        System.out.println("Starting..");  
        setDisplayFps(false);
        setDisplayStatView(false);
        flyCam.setDragToRotate(true);

      
       
        flyCam.setMoveSpeed(20f);
        cam.setLocation(new Vector3f(0,100,100));
        cam.lookAt(new Vector3f(0,0,0), Vector3f.UNIT_Y);
        
        startScreen = new StartScreen(this);
        stateManager.attach(startScreen);

        /**
         * Åctivate the Nifty-JME integration: 
         */
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        nifty.fromXml("Interface/screens.xml", "start", startScreen);

    

    }
    
    public void goToMapEditor(Settings setting){
        rootNode.detachAllChildren();
        Planet planet = new Planet(assetManager, rootNode, setting);
        rootNode.attachChild(planet.getPlanet());
        rootNode.attachChild(planet.getNavMesh());
        
    }
    
    public void endGame(){
        rootNode.detachAllChildren();
        match=null;
    }
    public void pause(){
        match.pause();
    }
    public Match getMatch(){
        return match;
    }
    
    public void startGame(Settings setting){
       rootNode.detachAllChildren();
       Dtriangles =  new Node();
       rootNode.attachChild(Dtriangles);
       match = new Match(assetManager, rootNode, setting);
        for (Agent agent : match.agents) {
            rootNode.attachChild(agent.getNodeAgent());
            if(agent instanceof Pursuer){
                Pursuer pur = (Pursuer) agent;
                pur.setVisibleField(true);
            }
               
           
        }
        
         bb = new Node[match.getGraph().getVertices().size()];
        control = new BillboardControl[match.getGraph().getVertices().size()];
     
         
        BitmapFont newFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        times = new BitmapText[match.getGraph().getVertices().size()];
        for(int i=0;i<times.length;i++){
            
            times[i] = new BitmapText(newFont, false);
            times[i].setSize(1.5f);
            times[i].setText("0");
            times[i].setLocalTranslation(new Vector3f(0,0,0));
            control[i]=new BillboardControl();
            bb[i] = new Node("node"+i);
            bb[i].setLocalTranslation(match.getGraph().getVertices().get(i).getTriangle().getCenter());
            bb[i].addControl(control[i]);
            bb[i].attachChild(times[i]);
            rootNode.attachChild(bb[i]);
        }
        
        rootNode.attachChild(match.getPlanet().getPlanet());
        rootNode.attachChild(match.getPlanet().getNavMesh());
         initKeys();
         
    }
    @Override
    public void simpleUpdate(float tpf) {
        if(match==null)
            return;
        if (!(match.isEnd() || match.isPause())) {
            match.moveAgents(tpf);


           match.checkViews();
         match.calculateSafeTriangles();
         showDangerousTriangles(match);
            
        }
        
       
        /*
        for(Agent agent: match.getAgents()){
            if(agent instanceof Pursuer)
                agent.rotate(tpf);
        }
*/
        
        for(int i=0;i<times.length;i++){
          
            times[i].setText(""+match.getGraph().getVertices().get(i).getTime());
           // times[i].setText(""+graph.getVertices().get(i).getTriangle().getCenter());
          
        }
        

    }
    
    private void initKeys() {
        // You can map one or several inputs to one named action
        inputManager.addMapping("Start", new KeyTrigger(KeyInput.KEY_SPACE));
        // Add the names to the action listener.
        inputManager.addListener(analogListener, "Start");

    }

    private AnalogListener analogListener = new AnalogListener() {
        
        public void onAnalog(String name, float value, float tpf) {
            System.out.println("Here");
            if (name.equals("Start")) {
                match.start();
                inputManager.deleteMapping("Start");
            }
            

        }
    };

    private void showDangerousTriangles(Match match) {
        Dtriangles.detachAllChildren();
        ArrayList<Vertex> vertices = match.getGraph().getVertices();
        for(Vertex v: vertices){
            if(v.isSafe())
                continue;
            Mesh m = new Mesh();
            Vector3f[] points = { v.getTriangle().get1(), v.getTriangle().get2(),v.getTriangle().get3()};
            Vector2f[] textCoord = {new Vector2f(1,1),new Vector2f(1,1),new Vector2f(1,1)}; 
            int[] indices = {2,0,1};
            
            m.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(points));
            m.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(textCoord));
            m.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(indices));
            m.updateBound();
            Geometry geo = new Geometry("triangle", m); // using our custom mesh object
            Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", ColorRGBA.Yellow);
            geo.setMaterial(mat);
            Dtriangles.attachChild(geo);
        }
    }

   
}
