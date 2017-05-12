/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic.Gui;

/**
 *
 * @author giogio
 */
import GameLogic.Settings;
import GameLogic.mainClass;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.RadioButtonGroupStateChangedEvent;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.slider.builder.SliderBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 */
public class StartScreen extends AbstractAppState implements ScreenController {

  private Nifty nifty;
  private Application app;
  private Screen screen;
  private mainClass main;
  private Settings settings;
  /** custom methods */
  public StartScreen(mainClass main) {
    /** You custom constructor, can accept arguments */
    this.main=main;
    settings = new Settings();
    settings.setN_escapers(10);
    settings.setTypeOfAlgorithm(1);
    settings.setN_pursuers(10);
  }

  public void startGame() {
    nifty.gotoScreen("actualGame");  // switch to another screen
    nifty.setIgnoreKeyboardEvents(true);
    updateSetting();
    main.startGame(settings);
   
  }
  public void updateSetting(){
    String n_pursuers = nifty.getScreen("settings").findNiftyControl("n_pursuers", TextField.class).getText();
    String n_evaders = nifty.getScreen("settings").findNiftyControl("n_evaders", TextField.class).getText();
    settings.setN_escapers(Integer.parseInt(n_evaders));
    settings.setN_pursuers(Integer.parseInt(n_pursuers));
  }
  public void goToMapEditor(){
    nifty.gotoScreen("mapEditor");
    main.goToMapEditor(settings);
  }
  
  public void backToMenu(){
    nifty.gotoScreen("start");
    main.endGame();
  }
  public void goToSettingGame(){
    nifty.gotoScreen("settings");
    nifty.setIgnoreKeyboardEvents(false);
   
  }
  
  public void generatePlanet(){
     if(settings.getTypeOfAlgorithm()==1){
         
        int radius, heightmapwidth, iterations, numIslands, seed;
        float islandRadius, displacement, smoothing;
        
        radius = (int) nifty.getScreen("mapEditor").findNiftyControl("radius", Slider.class).getValue();
        heightmapwidth = (int) nifty.getScreen("mapEditor").findNiftyControl("heightmapwidth", Slider.class).getValue();
        seed = (int)nifty.getScreen("mapEditor").findNiftyControl("seed", Slider.class).getValue();

        iterations = (int) nifty.getScreen("mapEditor").findNiftyControl("iterations", Slider.class).getValue();
        numIslands = (int) nifty.getScreen("mapEditor").findNiftyControl("numIslands", Slider.class).getValue();
        islandRadius = nifty.getScreen("mapEditor").findNiftyControl("islandRadius", Slider.class).getValue();
        displacement = nifty.getScreen("mapEditor").findNiftyControl("displacement", Slider.class).getValue();
        smoothing = nifty.getScreen("mapEditor").findNiftyControl("smoothing", Slider.class).getValue();

        settings.setRadius(radius);
        settings.setHeightmapwidth(heightmapwidth);
        settings.setSeed(seed);
        
        settings.setIterations(iterations);
        settings.setNumIslands(numIslands);
        settings.setIslandRadius(islandRadius);
        settings.setDisplacement(displacement);
        settings.setSmoothing(smoothing);
       
     }else if(settings.getTypeOfAlgorithm()==2){
        int radius, heightmapwidth, seed;
        float amplitude, x_scale, y_scale, z_scale;
        radius = (int) nifty.getScreen("mapEditor").findNiftyControl("radius", Slider.class).getValue();
        heightmapwidth = (int) nifty.getScreen("mapEditor").findNiftyControl("heightmapwidth", Slider.class).getValue();
        seed = (int) nifty.getScreen("mapEditor").findNiftyControl("seed", Slider.class).getValue();
        amplitude = nifty.getScreen("mapEditor").findNiftyControl("amplitude", Slider.class).getValue();
        x_scale = nifty.getScreen("mapEditor").findNiftyControl("x_scale", Slider.class).getValue();
        y_scale = nifty.getScreen("mapEditor").findNiftyControl("y_scale", Slider.class).getValue();
        z_scale = nifty.getScreen("mapEditor").findNiftyControl("z_scale", Slider.class).getValue();

        settings.setRadius(radius);
        settings.setHeightmapwidth(heightmapwidth);
        settings.setSeed(seed);
        
        settings.setAmplitude(amplitude);
        settings.setX_scale(x_scale);
        settings.setY_scale(y_scale);
        settings.setZ_scale(z_scale);
        
     }
     settings.printSettings();
     main.goToMapEditor(settings);
  }
  
  
  
  public void pauseGame(){
      main.pause();
      if(main.getMatch().isPause()){
          nifty.getScreen("actualGame").findNiftyControl("PauseButton", Button.class).setText("Resume");
      }else{
          nifty.getScreen("actualGame").findNiftyControl("PauseButton", Button.class).setText("Pause");

      }
  }
  public void quitGame() {
    app.stop();
  }
  
  public void selectAlgorithm1(){

      if(nifty.getCurrentScreen().findElementById("sliderPanel6")!=null){
         nifty.removeElement(nifty.getCurrentScreen(),nifty.getCurrentScreen().findElementById("sliderPanel6"));
         nifty.removeElement(nifty.getCurrentScreen(),nifty.getCurrentScreen().findElementById("sliderPanel7"));
         nifty.removeElement(nifty.getCurrentScreen(),nifty.getCurrentScreen().findElementById("sliderPanel8"));
         nifty.removeElement(nifty.getCurrentScreen(),nifty.getCurrentScreen().findElementById("sliderPanel9"));

      }
      
      
      
      Screen screen = nifty.getCurrentScreen();
      Element layer = screen.findElementById("sliderPanel");
      Element layer2;
      PanelBuilder panelBuilder = new PanelBuilder();
      SliderBuilder sliderBuilder = new SliderBuilder(false);
      LabelBuilder labelBuilder = new LabelBuilder();
      panelBuilder.childLayoutHorizontal();
      
      
      panelBuilder.id("sliderPanel1");
      panelBuilder.build(nifty, screen, layer);
      layer2 = screen.findElementById("sliderPanel1");
      labelBuilder.text("N Islands: ");
      labelBuilder.build(nifty, screen, layer2);
      sliderBuilder.id("numIslands");
      sliderBuilder.min(1);
      sliderBuilder.max(35);
      sliderBuilder.stepSize(5);
      sliderBuilder.initial(20);
      sliderBuilder.build(nifty, screen, layer2);
     
      
      panelBuilder.id("sliderPanel2");
      panelBuilder.build(nifty, screen, layer);
      layer2 = screen.findElementById("sliderPanel2");
      labelBuilder.text("Island radius: ");
      labelBuilder.build(nifty, screen, layer2);
      sliderBuilder.id("islandRadius");
      sliderBuilder.min(20);
      sliderBuilder.max(90);
      sliderBuilder.stepSize(5);
      sliderBuilder.initial(90);
      sliderBuilder.build(nifty, screen, layer2);
      
      panelBuilder.id("sliderPanel3");
      panelBuilder.build(nifty, screen, layer);
      layer2 = screen.findElementById("sliderPanel3");
      labelBuilder.text("Iterations: ");
      labelBuilder.build(nifty, screen, layer2);
      sliderBuilder.id("iterations");
      sliderBuilder.min(15000);
      sliderBuilder.max(50000);
      sliderBuilder.stepSize(1000);
      sliderBuilder.initial(25000);
      sliderBuilder.build(nifty, screen, layer2);
      
      panelBuilder.id("sliderPanel4");
      panelBuilder.build(nifty, screen, layer);
      layer2 = screen.findElementById("sliderPanel4");
      labelBuilder.text("Displacement: ");
      labelBuilder.build(nifty, screen, layer2);
      sliderBuilder.id("displacement");
      sliderBuilder.min(-1);
      sliderBuilder.max(1);
      sliderBuilder.stepSize(0.1f);
      sliderBuilder.initial(.70f);
      sliderBuilder.build(nifty, screen, layer2);
      
      
       panelBuilder.id("sliderPanel5");
      panelBuilder.build(nifty, screen, layer);
      layer2 = screen.findElementById("sliderPanel5");
      labelBuilder.text("Smoothing: ");
      labelBuilder.build(nifty, screen, layer2);
      sliderBuilder.id("smoothing");
      sliderBuilder.min(0);
      sliderBuilder.max(1);
      sliderBuilder.stepSize(0.1f);
      sliderBuilder.initial(.3f);
      sliderBuilder.build(nifty, screen, layer2);
      
      
      
      
      
      
/*
      Screen screen = nifty.getCurrentScreen();
      Element layer = screen.findElementById("sliderPanel");
      SliderBuilder sliderBuilder = new SliderBuilder(false);
      sliderBuilder.id("numIslands");
      sliderBuilder.build(nifty, screen, layer);
      sliderBuilder.id("islandRadius");
      sliderBuilder.build(nifty, screen, layer);
      sliderBuilder.id("iterations");
      sliderBuilder.build(nifty, screen, layer);
      sliderBuilder.id("displacement");
      sliderBuilder.build(nifty, screen, layer);
      sliderBuilder.id("smoothing");
      sliderBuilder.build(nifty, screen, layer);
      */
      
     

  }
  public void selectAlgorithm2(){
     /*
      Element numIslands = nifty.getCurrentScreen().findElementById("numIslands");
      Element islandRadius = nifty.getCurrentScreen().findElementById("islandRadius");
      Element iterations = nifty.getCurrentScreen().findElementById("iterations");
      Element displacement = nifty.getCurrentScreen().findElementById("displacement");
      Element smoothing = nifty.getCurrentScreen().findElementById("smoothing");
      
      Element numIslandsLabel= nifty.getCurrentScreen().findElementById("numIslandsLabel");
      Element islandRadiusLabel = nifty.getCurrentScreen().findElementById("islandRadiusLabel");
      Element iterationsLabel = nifty.getCurrentScreen().findElementById("iterationsLabel");
      Element displacementLabel = nifty.getCurrentScreen().findElementById("displacementLabel");
      Element smoothingLabel = nifty.getCurrentScreen().findElementById("smoothingLabel");
      */

      if(nifty.getCurrentScreen().findElementById("sliderPanel1")!=null){
            
          nifty.removeElement(nifty.getCurrentScreen(),nifty.getCurrentScreen().findElementById("sliderPanel1"));
          nifty.removeElement(nifty.getCurrentScreen(),nifty.getCurrentScreen().findElementById("sliderPanel2"));
          nifty.removeElement(nifty.getCurrentScreen(),nifty.getCurrentScreen().findElementById("sliderPanel3"));
          nifty.removeElement(nifty.getCurrentScreen(),nifty.getCurrentScreen().findElementById("sliderPanel4"));
          nifty.removeElement(nifty.getCurrentScreen(),nifty.getCurrentScreen().findElementById("sliderPanel5"));
/*
        
        nifty.removeElement(nifty.getCurrentScreen(), numIslands);
        nifty.removeElement(nifty.getCurrentScreen(), islandRadius);
        nifty.removeElement(nifty.getCurrentScreen(), iterations);
        nifty.removeElement(nifty.getCurrentScreen(), displacement);
        nifty.removeElement(nifty.getCurrentScreen(), smoothing);
        
        nifty.removeElement(nifty.getCurrentScreen(), numIslandsLabel);
        nifty.removeElement(nifty.getCurrentScreen(), islandRadiusLabel);
        nifty.removeElement(nifty.getCurrentScreen(), iterationsLabel);
        nifty.removeElement(nifty.getCurrentScreen(), displacementLabel);
        nifty.removeElement(nifty.getCurrentScreen(), smoothingLabel);
          */
      }
      

      Screen screen = nifty.getCurrentScreen();
      Element layer = screen.findElementById("sliderPanel");
      Element layer2;
      PanelBuilder panelBuilder = new PanelBuilder();
      SliderBuilder sliderBuilder = new SliderBuilder(false);
      LabelBuilder labelBuilder = new LabelBuilder();
      panelBuilder.childLayoutHorizontal();
      
      
      panelBuilder.id("sliderPanel6");
      panelBuilder.build(nifty, screen, layer);
      layer2 = screen.findElementById("sliderPanel6");
      labelBuilder.text("Amplitude: ");
      labelBuilder.build(nifty, screen, layer2);
      sliderBuilder.id("amplitude");
      sliderBuilder.min(1);
      sliderBuilder.max(25);
      sliderBuilder.stepSize(5);
      sliderBuilder.initial(10.0f);
      sliderBuilder.build(nifty, screen, layer2);
     
      
      panelBuilder.id("sliderPanel7");
      panelBuilder.build(nifty, screen, layer);
      layer2 = screen.findElementById("sliderPanel7");
      labelBuilder.text("X_scale: ");
      labelBuilder.build(nifty, screen, layer2);
      sliderBuilder.id("x_scale");
      sliderBuilder.min(1);
      sliderBuilder.max(100);
      sliderBuilder.stepSize(10);
      sliderBuilder.initial(100.0f);
      sliderBuilder.build(nifty, screen, layer2);
      
      panelBuilder.id("sliderPanel8");
      panelBuilder.build(nifty, screen, layer);
      layer2 = screen.findElementById("sliderPanel8");
      labelBuilder.text("Y_scale: ");
      labelBuilder.build(nifty, screen, layer2);
      sliderBuilder.id("y_scale");
      sliderBuilder.min(1);
      sliderBuilder.max(100);
      sliderBuilder.stepSize(10);
      sliderBuilder.initial(100.0f);
      sliderBuilder.build(nifty, screen, layer2);
      
      panelBuilder.id("sliderPanel9");
      panelBuilder.build(nifty, screen, layer);
      layer2 = screen.findElementById("sliderPanel9");
      labelBuilder.text("Z_scale: ");
      labelBuilder.build(nifty, screen, layer2);
      sliderBuilder.id("z_scale");
      sliderBuilder.min(1);
      sliderBuilder.max(100);
      sliderBuilder.stepSize(10);
      sliderBuilder.initial(100.0f);
      sliderBuilder.build(nifty, screen, layer2);
      
  

  }


  /** Nifty GUI ScreenControl methods */
  public void bind(Nifty nifty, Screen screen) {
 
    this.nifty = nifty;
    this.screen = screen;
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }

  /** jME3 AppState methods */
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    this.app = app;
  }

  @Override
  public void update(float tpf) {
   if(main.getMatch()!=null && !main.getMatch().isPause()){
       nifty.getScreen("actualGame").findNiftyControl("evadersLeft", Label.class).setText("Number of Evaders left: "+main.getMatch().getEvadersLeft());
   }
  }
  
  
  
 @NiftyEventSubscriber(id="RadioGroup-1")
  public void onRadioGroup1Changed(final String id, final RadioButtonGroupStateChangedEvent event) {
           System.out.println("Here");
      if(event.getSelectedId().equals("algorithm-1")){
          selectAlgorithm1();
          settings.setTypeOfAlgorithm(1);
      }
      if(event.getSelectedId().equals("algorithm-2")){
          selectAlgorithm2();
          settings.setTypeOfAlgorithm(2);

      }
      
  }

  
}

