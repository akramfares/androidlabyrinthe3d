package androidlabyrinthe3d;


import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
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

public class Level3 extends SimpleApplication implements ActionListener{
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
	protected RigidBodyControl player;
	private RigidBodyControl landscape;
	boolean isRunning=true;
	private boolean left = false, right = false, up = false, down = false;
	//*****************************************
	protected Geometry play ,geom,player1,player2;
	 protected Node node;
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
		
		terrain = Terrain.getTerrain();
        spheres = new Node("Spheres");
        rootNode.attachChild(spheres);
        playerNode = new Node("Player");
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
        
        // ----------- Configuration du Terrain ---------
        rootNode.attachChild(terrain);
        Terrain.setState(bulletAppState);
        Terrain.setAsset(assetManager);
		// --------------- Terrain 1 3x6 ----------------
		Terrain t1 = new Terrain(3, 0.5f, 6, ColorRGBA.Green);
		t1.getGeom().move(0,-10,0);
		// --------------- Terrain 2 2x1 ----------------
		Terrain t2 = new Terrain(2, 0.5f, 1, ColorRGBA.White);
		t2.getGeom().move(5,-10,5);
		// --------------- Terrain 3 10x4 ----------------
		Terrain t3 = new Terrain(10, 0.5f, 4, ColorRGBA.White);
		t3.getGeom().move(15f, -10, 0f);
		// --------------- Terrain 4 2x1 ----------------
		Terrain t4 = new Terrain(2, 0.5f, 1, ColorRGBA.White);
		t4.getGeom().move(25f, -10, -5f);
		// --------------- Terrain 5 3x6 ----------------
		Terrain t5 = new Terrain(3, 0.5f, 6, ColorRGBA.Green);
		t5.getGeom().move(30f, -10, 0f);
		// --------------- Terrain Collision -----------
		Terrain.setCollision();
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
        
        BoxCollisionShape playerShape = new BoxCollisionShape(new Vector3f(0.6f, 0.6f, 0.6f));
	        /*player = new CharacterControl(playerShape, 0.01f);
	        player.setJumpSpeed(0);
	        player.setFallSpeed(30);
	        player.setGravity(30);
	        player.setPhysicsLocation(new Vector3f(0, 15f, 1f));*/
        player = new RigidBodyControl(playerShape, 3);
		geom1.addControl(player);

		
         bulletAppState.getPhysicsSpace().add(player);
    	playerNode.attachChild(geom1);
		//-------------------------------------------------------------------
    	  Box b = new Box(Vector3f.ZERO, 0.1f, 0.1f, 0.1f);
	        b.setBound(new BoundingSphere());
	        b.updateBound();
	        play = new Geometry("blue cube", b);
	        
	        play.setLocalTranslation(new Vector3f(0,2,0)); 
	        Material mat = new Material(assetManager,
	          "Common/MatDefs/Misc/Unshaded.j3md");
	        mat.setColor("Color", ColorRGBA.Blue);
	        
	        play.setMaterial(mat);
	      
	        //*************************************************
	        Sphere s=new Sphere(10,10,1f); 
	        // Do bounds for the sphere, but we'll use a BoundingBox this time 
	        s.setBound(new BoundingBox()); 
	        s.updateBound(); 
	        geom = new Geometry("Box", s);  // create cube geometry from the shape
	        Material m = new Material(assetManager,
	          "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
	        //geom.setLocalTranslation(2, 0, 0);
	        m.setColor("Color",ColorRGBA.LightGray); // set color of material to blue
	        geom.setMaterial(m);
	     // ---------------- Spheres Collision -----------
			CollisionShape sphereShape2 =
				    CollisionShapeFactory.createDynamicMeshShape(geom);
			RigidBodyControl spherecontrol2 = new RigidBodyControl(sphereShape2, 0);
			geom.addControl(spherecontrol);
			
			bulletAppState.getPhysicsSpace().add(spherecontrol2);
	            node=new Node();
		       node.attachChild(play);
		       node.attachChild(geom);
		       node.scale(1);
	        
	       ///rootNode.attachChild(node);
	      


		
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
    		  Vector3f v = new Vector3f(0,0,0); //geom1.getLocalTranslation();
    			if (left)  { v.x -= 5f; }
    			if (right) { v.x += 5f; }
    			if (up)    { v.z -= 5f;}
    			if (down)  { v.z += 5f;}
    			if( left || right || up || down){
    				//player.setWalkDirection(new Vector3f(v.x,v.y,v.z));
    				//geom1.setLocalTranslation(v);
    				player.setLinearVelocity(v);
    			}
    		  } else {
    			  System.out.println("Dehors T1 " );
    		  }
    	  camNode.lookAt(geom1.getLocalTranslation(), Vector3f.UNIT_Y);
    	//Move camNode, e.g. behind and above the target:
          camNode.setLocalTranslation(new Vector3f(geom1.getLocalTranslation().x, 20, 10));
          //------------------------------------------------------------------------------
	    	try {
	            Thread.sleep(40);
	            Vector3f v = node.getLocalTranslation();
	            node.setLocalTranslation(v.x+0.1f, v.y, v.z);
	            Vector3f vec=spheres.getLocalScale();
	           
	        //  spheres.scale(1+0.001f);
	        } catch (InterruptedException ex) {
	           
	        }
	        // make the player rotate
	    for(int i=0 ;i<5 ;i++ ){
	      // spheres.rotate(i*tpf*0.4f, 0, 0); 
	      
	    }
	    
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Level3 app = new Level3();
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
