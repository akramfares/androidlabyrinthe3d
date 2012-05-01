package androidlabyrinthe3d;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.CartoonEdgeFilter;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.Caps;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

public class Test1 extends SimpleApplication implements ActionListener{
	private FilterPostProcessor fpp;
	private FogFilter fog;
	private CameraNode camNode;
	private BulletAppState bulletAppState;
	protected Geometry sceneModel;
	protected Node terrain;
	protected Node spheres;
	protected Node playerNode;
	protected Geometry geom1;
	protected Geometry levelgeom;
	protected CharacterControl player;
	private RigidBodyControl landscape;
	boolean isRunning=true;
	private boolean left = false, right = false, up = false, down = false;
	
	@Override
	public void simpleInitApp() {
		 if (renderer.getCaps().contains(Caps.GLSL100)){
	            fpp=new FilterPostProcessor(assetManager);
	            //fpp.setNumSamples(4);
	            CartoonEdgeFilter toon=new CartoonEdgeFilter();
	            //toon.setDepthThreshold(0);
	            //toon.setDepthSensitivity(150);
	            fpp.addFilter(toon);
	            //viewPort.addProcessor(fpp);
	            
	            //fpp=new FilterPostProcessor(assetManager);
	            //fpp.setNumSamples(4);
	            fog=new FogFilter();
	            fog.setFogColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
	            fog.setFogDistance(155);
	            fog.setFogDensity(2.0f);
	            fpp.addFilter(fog);
	            viewPort.addProcessor(fpp);
	        }
		 
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		bulletAppState.getPhysicsSpace().enableDebug(assetManager);
		
		BulletAppState bulletAppState=new BulletAppState();
		stateManager.attach(bulletAppState);
		
		viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
		cam.setLocation(new Vector3f(10,20,10));
		
		terrain = new Node("Terrain");
        rootNode.attachChild(terrain);
        spheres = new Node("Spheres");
        rootNode.attachChild(spheres);
      //  playerNode = new Node("Player");
        rootNode.attachChild(playerNode);
        
        // ------------ Setup the camera ---------------
        // Disable the default flyby cam
        flyCam.setEnabled(false);
        //create the camera Node
        camNode = new CameraNode("Camera Node", cam);
        //This mode means that camera copies the movements of the target:
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        //Attach the camNode to the target:
        playerNode.attachChild(camNode);
        
        
		
		// --------------- Terrain 1 3x6 ----------------
		Box levelmesh=new Box(Vector3f.ZERO, 3, 0.5f, 6);
		levelgeom=new Geometry("level",levelmesh);
		levelgeom.updateModelBound();
		 
		Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat1.setColor("Color", ColorRGBA.Green);
		levelgeom.setMaterial(mat1);
		levelgeom.move(0,-10,0); 
		terrain.attachChild(levelgeom);
		
		// --------------- Terrain 2 2x1 ----------------
		
		Box t2mesh=new Box(Vector3f.ZERO, 2, 0.5f, 1);
		Geometry t2geom=new Geometry("level",t2mesh);
		t2geom.updateModelBound();
		t2geom.move(5f, 0, 5f);
		 
		Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat2.setColor("Color", ColorRGBA.White);
		t2geom.setMaterial(mat2);
		t2geom.move(0,-10,0); 
		terrain.attachChild(t2geom);
		
		// --------------- Terrain 3 10x4 ----------------
		
		Box t3mesh=new Box(Vector3f.ZERO, 10, 0.5f, 4);
		Geometry t3geom=new Geometry("level",t3mesh);
		t3geom.updateModelBound();
		t3geom.move(15f, 0, 0f);
		 
		Material mat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat3.setColor("Color", ColorRGBA.White);
		t3geom.setMaterial(mat3);
		t3geom.move(0,-10,0); 
		terrain.attachChild(t3geom);
		
		// --------------- Terrain 4 2x1 ----------------
		
		Box t4mesh=new Box(Vector3f.ZERO, 2, 0.5f, 1);
		Geometry t4geom=new Geometry("level",t4mesh);
		t4geom.updateModelBound();
		t4geom.move(25f, 0, -5f);
		 
		Material mat4 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat4.setColor("Color", ColorRGBA.White);
		t4geom.setMaterial(mat4);
		t4geom.move(0,-10,0); 
		terrain.attachChild(t4geom);
		
		// --------------- Terrain 5 3x6 ----------------
		Box t5mesh=new Box(Vector3f.ZERO, 3, 0.5f, 6);
		Geometry t5geom=new Geometry("level",t5mesh);
		t5geom.updateModelBound();
		t5geom.move(30f, 0, 0f);
		 
		Material mat5 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat5.setColor("Color", ColorRGBA.Green);
		t5geom.setMaterial(mat5);
		t5geom.move(0,-10,0); 
		terrain.attachChild(t5geom);
		
		// --------------- Terrain Collision -----------
		
		CollisionShape sceneShape =
			    CollisionShapeFactory.createDynamicMeshShape(terrain);
		RigidBodyControl levelcontrol = new RigidBodyControl(sceneShape, 0);
		terrain.addControl(levelcontrol);
		
		bulletAppState.getPhysicsSpace().add(levelcontrol);
		
		// ---------------- Spheres ---------------------
		Sphere sphere = new Sphere(32, 32, 1f);
		Geometry s1geom=new Geometry("Sphere",sphere);
		s1geom.updateModelBound();
		 
		Material mats1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mats1.setColor("Color", ColorRGBA.Blue);
		s1geom.setMaterial(mats1);
		s1geom.move(10,-8,0); 
		spheres.attachChild(s1geom);
		
		// ---------------- Spheres Collision -----------
		CollisionShape sphereShape =
			    CollisionShapeFactory.createDynamicMeshShape(spheres);
		RigidBodyControl spherecontrol = new RigidBodyControl(sphereShape, 0);
		spheres.addControl(spherecontrol);
		bulletAppState.getPhysicsSpace().add(spherecontrol);
		
		// ---------------- Player -----------------------
		Box b1=new Box(Vector3f.ZERO, 0.6f, 0.6f, 0.6f);
		geom1 = new Geometry("Box", b1);
		geom1.updateModelBound();

        Material matp = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        matp.setColor("Color", ColorRGBA.Red);   // set color of material to blue
        geom1.setMaterial(matp);                   // set the cube's material
        geom1.move(0, 25f, 0);
        
        SphereCollisionShape playerShape = new SphereCollisionShape(0.6f);
	        player = new CharacterControl(playerShape, 0.01f);
	        player.setJumpSpeed(5);
	        player.setFallSpeed(30);
	        player.setGravity(30);
	        player.setPhysicsLocation(new Vector3f(0, 15f, 1f));
		geom1.addControl(player);

		
         bulletAppState.getPhysicsSpace().add(player);
    	playerNode.attachChild(geom1);
		
		
    	setUpKeys();
    	setupLighting();
	}
	
	private void setUpKeys() {
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_H));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_K));
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_U));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_J));
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(this, "Left");
		inputManager.addListener(this, "Right");
		inputManager.addListener(this, "Up");
		inputManager.addListener(this, "Down");
		inputManager.addListener(this, "Jump");
		}
	
	public void setupLighting(){
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-1, -1, 1).normalizeLocal());
        dl.setColor(new ColorRGBA(2,2,2,1));
        rootNode.addLight(dl);
    }
	
	
	public void simpleUpdate(float tpf) {
		// Collision
    	CollisionResults results = new CollisionResults();
    	BoundingVolume bv2 = geom1.getWorldBound();
    		terrain.collideWith(bv2, results);
    	  if (results.size() > 0) {
    		  Vector3f v = geom1.getLocalTranslation();
    			if (left)  { v.x -= 0.05f; }
    			if (right) { v.x += 0.05f; }
    			if (up)    { v.z -= 0.05f;}
    			if (down)  { v.z += 0.05f;}
    			if( left || right || up || down) player.setPhysicsLocation(new Vector3f(v.x,v.y,v.z));
    		  } else {
    			  System.out.println("Dehors T1 " );
    		  }
    	  camNode.lookAt(geom1.getLocalTranslation(), Vector3f.UNIT_Y);
    	//Move camNode, e.g. behind and above the target:
          camNode.setLocalTranslation(new Vector3f(geom1.getLocalTranslation().x, 20, 10));
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Test1 app = new Test1();
        app.start(); // start the game

	}

	@Override
	public void onAction(String binding, boolean value, float tpf) {
		if (binding.equals("Left")) {
			left = value;
			} else if (binding.equals("Right")) {
			right = value;
			} else if (binding.equals("Up")) {
			up = value;
			} else if (binding.equals("Down")) {
			down = value;
			} else if (binding.equals("Jump")) {
				player.setPhysicsLocation(new Vector3f(0, 25f, 0));
			}
		
	}

}
