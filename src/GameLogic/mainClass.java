/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import GameLogic.Gui.StartScreen;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;

import de.lessvoid.nifty.Nifty;


/**
 *
 * @author giogio
 */
public class mainClass extends SimpleApplication {

    Match match;
    StartScreen startScreen;

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

       match = new Match(assetManager, rootNode, setting);
        for (Agent agent : match.agents) {
            rootNode.attachChild(agent.getNodeAgent());
            if(agent instanceof Pursuer){
                Pursuer pur = (Pursuer) agent;
                pur.setVisibleField(true);
            }
               
           
        }
        rootNode.attachChild(match.getPlanet().getPlanet());
         initKeys();
         
    }
    @Override
    public void simpleUpdate(float tpf) {
        if(match==null)
            return;
        if (!(match.isEnd() || match.isPause())) {
            match.moveAgents(tpf);
            match.checkViews();
           
            
        }
        /*
        for(Agent agent: match.getAgents()){
            if(agent instanceof Pursuer)
                agent.rotate(tpf);
        }
*/
        

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

   
}
