package androidlabyrinthe3d;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class Test1 extends SimpleApplication implements ScreenController {

    private Nifty nifty;

    public static void main(String[] args){
        Test1 app = new Test1();
        app.setPauseOnLostFocus(false);
        app.start();
    }

    public void simpleInitApp() {
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        Geometry geom = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        geom.setMaterial(mat);
       // rootNode.attachChild(geom);

        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                                                          inputManager,
                                                           audioRenderer,
                                                          guiViewPort);
        nifty = niftyDisplay.getNifty();
        Screen screen=new Screen(nifty, null, null, null);
           
        nifty.fromXml("androidlabyrinthe3d/TestXMLSchema.xsd", "start", this);
      getViewPort().setBackgroundColor(null);
        // attach the nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);

        // disable the fly cam
//        flyCam.setEnabled(false);
//        flyCam.setDragToRotate(true);
        inputManager.setCursorVisible(true);
    }

    public void bind(Nifty nifty, Screen screen) {
        System.out.println("bind( " + screen.getScreenId() +"hello"+ ")");
    }

    public void onStartScreen() {
        System.out.println("onStartScreen");
    }

    public void onEndScreen() {
        System.out.println("onEndScreen");
    }

    public void quit(){
        nifty.gotoScreen("end");
    }

}