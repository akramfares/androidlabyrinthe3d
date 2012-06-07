package androidlabyrinthe3d;


import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.texture.Texture;

public class Level1 extends SimpleApplication implements ActionListener{
	private FilterPostProcessor fpp;
	private FogFilter fog;
	private CameraNode camNode;
	private PssmShadowRenderer pssmRenderer;
	private BasicShadowRenderer bsr;
	private BulletAppState bulletAppState;
	private SphereGroup sphereGroup;
	private SphereGroup sphereGroup2;
	protected Node terrain;
	protected Node spheres;
	protected Node spheres2;
	protected Node playerNode;
	protected Geometry geom1;
	protected Geometry levelgeom;
	protected Geometry s1geom;
	protected Geometry s2geom;
	protected RigidBodyControl player;
	protected RigidBodyControl spherecontrol;
	protected float speedSphere = 4f;
	protected float x,y,z;
	boolean isRunning=true;
	private boolean left = false, right = false, up = false, down = false;
	private Vector3f speed = Vector3f.ZERO;
	private CollisionResults results;
	
	@Override
	public void simpleInitApp() {
		 /*if (renderer.getCaps().contains(Caps.GLSL100)){
	            fpp=new FilterPostProcessor(assetManager);
	            //fpp.setNumSamples(4);
	            //CartoonEdgeFilter toon=new CartoonEdgeFilter();
	            //toon.setDepthThreshold(0);
	            //toon.setDepthSensitivity(150);
	            //fpp.addFilter(toon);
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
		 DirectionalLight sun = new DirectionalLight();
		 sun.setColor(ColorRGBA.White);
		 sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
		 rootNode.addLight(sun);
		 	
		 
		 pssmRenderer = new PssmShadowRenderer(assetManager, 256 , 2);
	        pssmRenderer.setDirection(new Vector3f(0.99f,-14.99f,4.99f).normalizeLocal());
	        pssmRenderer.setLambda(0.55f);
	        pssmRenderer.setShadowIntensity(0.6f);
	        pssmRenderer.setCompareMode(CompareMode.Software);
	        pssmRenderer.setFilterMode(FilterMode.PCF8);
	        pssmRenderer.setEdgesThickness(1);
	        pssmRenderer.setShadowZExtend(500);
	        viewPort.addProcessor(pssmRenderer);
		*/
        
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		bulletAppState.getPhysicsSpace().enableDebug(assetManager);
		
		BulletAppState bulletAppState=new BulletAppState();
		stateManager.attach(bulletAppState);
		
		
		viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
		cam.setLocation(new Vector3f(10,20,10));
		
		
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
        terrain = Terrain.getTerrain();
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
		
		// ----------- Configuration des Spheres ---------
		SphereObstacle.setAsset(assetManager);
		sphereGroup = new SphereGroup();
        spheres = sphereGroup.getNode();
        rootNode.attachChild(spheres);
        spheres.setLocalTranslation(new Vector3f(15,0,0));
        // ----------- Spheres 1 ---------
        SphereObstacle s1 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		s1.getGeom().move(10,-8,0); 
        // ----------- Spheres 2 ---------
        SphereObstacle s2 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		s2.getGeom().move(5,-8,0); 
		// ----------- Spheres 3 ---------
        SphereObstacle s3 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		s3.getGeom().move(-5,-8,0);
		// ----------- Spheres 4 ---------
        SphereObstacle s4 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		s4.getGeom().move(-10,-8,0);
		// ----------- Add Spheres to the group ---------
		sphereGroup.addSphere(s1);
		sphereGroup.addSphere(s2);
		sphereGroup.addSphere(s3);
		sphereGroup.addSphere(s4);
		
		// ----------- Configuration des Spheres ---------
		sphereGroup2 = new SphereGroup();
		spheres2 = sphereGroup2.getNode();
        rootNode.attachChild(spheres2);
        spheres2.setLocalTranslation(new Vector3f(15,0,0));
        // ----------- Spheres 1 ---------
        SphereObstacle sg1 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		sg1.getGeom().move(10,-8,0); 
        // ----------- Spheres 2 ---------
        SphereObstacle sg2 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		sg2.getGeom().move(5,-8,0); 
		// ----------- Spheres 3 ---------
        SphereObstacle sg3 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		sg3.getGeom().move(-5,-8,0);
		// ----------- Spheres 4 ---------
        SphereObstacle sg4 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		sg4.getGeom().move(-10,-8,0);
		// ----------- Add Spheres to the group ---------
		sphereGroup2.addSphere(sg1);
		sphereGroup2.addSphere(sg2);
		sphereGroup2.addSphere(sg3);
		sphereGroup2.addSphere(sg4);
		
		/* ---------------- Spheres Collision -----------
		CollisionShape sphereShape =
			    CollisionShapeFactory.createDynamicMeshShape(spheres);
		spherecontrol = new RigidBodyControl(sphereShape, 3);
		spheres.addControl(spherecontrol);
		bulletAppState.getPhysicsSpace().add(spherecontrol);
		*/
		// ---------------- Player -----------------------
		Box b1=new Box(Vector3f.ZERO, 0.6f, 0.6f, 0.6f);
		geom1 = new Geometry("Box", b1);
		geom1.updateModelBound();
//        Material matp = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");  // create a simple material
//        matp.setBoolean("UseMaterialColors",true); 
//        matp.setColor("Diffuse", ColorRGBA.Red);
//        matp.setColor("Ambient", ColorRGBA.Red);
//        matp.setColor("Specular", ColorRGBA.Red);
		Material matp = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	    //Texture tex_ml = assetManager.loadTexture("");
	    assetManager.registerLocator("assets/Textures/", FileLocator.class);
	    Texture tex_ml = assetManager.loadTexture("player.png");
	    matp.setTexture("ColorMap", tex_ml);
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
    	
    	/*
    	terrain.setShadowMode(ShadowMode.Receive);
    	spheres.setShadowMode(ShadowMode.Cast);
    	spheres2.setShadowMode(ShadowMode.Cast);
    	geom1.setShadowMode(ShadowMode.Cast);
    	*/
		
    	setUpKeys();
    	setupLighting();
	}
	
	private void setUpKeys() {
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
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
		movePlayer();
		moveObstacle();
    }

	private void moveObstacle() {
		/*if(s1geom.getLocalTranslation().x < 5f && speedSphere<0) speedSphere = 0.5f;
		if(s1geom.getLocalTranslation().x > 25f && speedSphere>0) speedSphere = -0.5f;
		s1geom.move(new Vector3f(speedSphere,0,0));*/
		sphereGroup.getNode().rotate(0, 0.001f, 0);
		sphereGroup2.getNode().rotate(0, -0.001f, 0);
		
    	  if (sphereGroup.collideWith(geom1)) {
    		  player.setPhysicsLocation(new Vector3f(0, 5f, 0));
    		  player.setLinearVelocity(new Vector3f(0,0,0));
    	  }
    	  
    	  if (sphereGroup2.collideWith(geom1)) {
    		  player.setPhysicsLocation(new Vector3f(0, 5f, 0));
    		  player.setLinearVelocity(new Vector3f(0,0,0));
    	  }
    	  
	}

	private void movePlayer() {
		speed = player.getLinearVelocity();
		player.setAngularVelocity(Vector3f.ZERO);
		// Collision
		results = new CollisionResults();
    	BoundingVolume bv2 = geom1.getWorldBound();
    		terrain.collideWith(bv2, results);
    	  if (results.size() > 0 && geom1.getLocalTranslation().y >=-8.95f) {
    		  // speed = new Vector3f(0,0,0); //geom1.getLocalTranslation();
    			if (left)  { if(speed.x > -4f) speed.x -= 0.4f; }
    			if (right) { if(speed.x < 4f) speed.x += 0.4f; }
    			if (up)    { if(speed.z > -4f) speed.z -= 0.4f; }
    			if (down)  { if(speed.z < 4f) speed.z += 0.4f; }
    			if( left || right || up || down){
    				//player.setWalkDirection(new Vector3f(v.x,v.y,v.z));
    				//geom1.setLocalTranslation(v);
    				player.setLinearVelocity(speed);
    			}
    		  }
    	  camNode.lookAt(geom1.getLocalTranslation(), Vector3f.UNIT_Y);
    	//Move camNode, e.g. behind and above the target:
          camNode.setLocalTranslation(new Vector3f(geom1.getLocalTranslation().x, 20, 10));
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Level1 app = new Level1();
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
				player.setPhysicsRotation(Matrix3f.ZERO);
				player.setLinearVelocity(Vector3f.ZERO);
			}
		
	}

}
