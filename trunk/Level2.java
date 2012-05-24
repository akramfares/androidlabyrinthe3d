package androidlabyrinthe3d;


import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
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

public class Level2 extends SimpleApplication implements ActionListener{
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
	protected Geometry s1geom ;
   protected float speedSphere = 4f;
   protected float speedT4 = 4f;
   protected Node node;
	//protected CharacterControl player;
	protected RigidBodyControl player;
	private RigidBodyControl landscape;
	boolean isRunning=true;
	private boolean left = false, right = false, up = false, down = false;
	private AudioNode audio ;
	private Terrain t1,t2,t3,t4,t5 ;
	@Override
	public void simpleInitApp() {
		 if (renderer.getCaps().contains(Caps.GLSL100)){
	            fpp=new FilterPostProcessor(assetManager);
	           
	            CartoonEdgeFilter toon=new CartoonEdgeFilter();
	           	fpp.addFilter(toon);
	           
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
		 t1 = new Terrain(1, 0.5f, 6, ColorRGBA.Orange);
		t1.getGeom().move(0,-10,0);
		// --------------- Terrain 2 2x1 ----------------
		 t2 = new Terrain(2f, 0.6f,1f, ColorRGBA.White);
		t2.getGeom().move(5,-10,5);
		// --------------- Terrain 3 10x4 ----------------
		t3 = new Terrain(1f, 0.5f, 0.6f, ColorRGBA.White);
		t3.getGeom().move(15f, -10, 0f);
		// --------------- Terrain 4 2x1 ----------------
		 t4 = new Terrain(0.6f, 0.5f, 0.6f, ColorRGBA.White);
		t4.getGeom().move(9f, -10, -5f);
		// --------------- Terrain 5 3x6 ----------------
		// t5 = new Terrain(2, 0.5f, 1, ColorRGBA.Green);
		//t5.getGeom().move(14f, -10, 0f);
		// --------------- Terrain Collision -----------
		Terrain.setCollision();
		// ---------------- Spheres ---------------------
		Sphere sphere = new Sphere(32, 32, 1f);
		 s1geom=new Geometry("Sphere",sphere);
		s1geom.updateModelBound();
		 
		Material mats1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mats1.setColor("Color", ColorRGBA.Blue);
		s1geom.setMaterial(mats1);
		s1geom.move(12,-8,0); 
		spheres.attachChild(s1geom);
		
		
		// ---------------- Player -----------------------
		Box b1=new Box(Vector3f.ZERO, 0.6f, 0.6f, 0.6f);
		geom1 = new Geometry("Box", b1);
		geom1.updateModelBound();
        Material matp = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        matp.setColor("Color", ColorRGBA.Red);   // set color of material to blue
        geom1.setMaterial(matp);                   // set the cube's material
        geom1.move(0, 25f, 0);
        
        BoxCollisionShape playerShape = new BoxCollisionShape(new Vector3f(0.6f, 0.6f, 0.6f));
	      
        player = new RigidBodyControl(playerShape, 3);
		geom1.addControl(player);

		
         bulletAppState.getPhysicsSpace().add(player);
    	playerNode.attachChild(geom1);
		
		
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
		//moveObstacle();
		moveTerrain();
    }
  private void moveTerrain(){
	  if(t2.getGeom().getLocalTranslation().x < 3f && speedSphere<0) speedSphere = 0.005f;
	  if(t2.getGeom().getLocalTranslation().x > 15f && speedSphere>0) speedSphere = -0.005f;
	  if(t4.getGeom().getLocalTranslation().x < 15f && speedSphere<0) speedT4 = 0.05f;
	  if(t4.getGeom().getLocalTranslation().x > -2f && speedSphere>0) speedT4  = -0.05f;
		//if(t3.getGeom().getLocalTranslation().z < -4f && speedSphere<0) speedSphere = 0.05f;
	//	if(t3.getGeom().getLocalTranslation().z > 4f && speedSphere>0) speedSphere = -0.05f;
		

		//t3.getGeom().move(new Vector3f(speedSphere,0,0));
		t2.getGeom().move(new Vector3f(speedSphere,0,0));
		t4.getGeom().move(new Vector3f(speedT4 ,0,0));
	
  }
	private void moveObstacle() {
		if(s1geom.getLocalTranslation().x < 5f && speedSphere<0) speedSphere = 0.09f;
		if(s1geom.getLocalTranslation().x > 12f && speedSphere>0) speedSphere = -0.09f;
		s1geom.move(new Vector3f(speedSphere,0,0));
		CollisionResults results = new CollisionResults();
    	BoundingVolume bv = geom1.getWorldBound();
    		spheres.collideWith(bv, results);
    	  if (results.size() > 0) {
    		  effet();
    		  audio = new AudioNode(assetManager, "Sound/Effects/Bang.wav", false);
  		    audio.setLooping(false);
  		    audio.setVolume(45);
  		    audio.playInstance();
  		    rootNode.attachChild(audio);
    		  player.setPhysicsLocation(new Vector3f(0, 5f, 0));
    		  player.setLinearVelocity(new Vector3f(0,0,0));
    	  }
    	  
	}

	private void movePlayer() {
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
    				
    				player.setLinearVelocity(v);
    			}
    		  } else {
    			  System.out.println("Dehors T1 " );
    		  }
    	  camNode.lookAt(geom1.getLocalTranslation(), Vector3f.UNIT_Y);
    	
          camNode.setLocalTranslation(new Vector3f(geom1.getLocalTranslation().x, 20, 10));
		
	}
	public void effet(){
		  ParticleEmitter fire = 
	            new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
	    Material mat_red = new Material(assetManager, 
	            "Common/MatDefs/Misc/Particle.j3md");
	    
	    mat_red.setTexture("Texture", assetManager.loadTexture(
	            "Effects/Explosion/flash.png"));
	    fire.setMaterial(mat_red);
	    
	    fire.setImagesX(2); 
	    fire.setImagesY(2); // 2x2 texture animation
	    fire.setEndColor(  new ColorRGBA(1f, 0f, 0f, 1f));   // red
	    fire.setStartColor(new ColorRGBA(0f, 1f, 0f, 0.5f)); // yellow
	    fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
	    fire.setStartSize(1.5f);
	    fire.setEndSize(0.1f);
	    fire.setGravity(0, 0, 0);
	    fire.setLowLife(1f);
	    fire.setHighLife(3f);
	    fire.getParticleInfluencer().setVelocityVariation(0.3f);
	    fire.move(8f, -10, 0f);
	    rootNode.attachChild(fire); 
	 
	    ParticleEmitter debris = 
	            new ParticleEmitter("Debris", ParticleMesh.Type.Triangle, 100);
	    Material debris_mat = new Material(assetManager, 
	            "Common/MatDefs/Misc/Particle.j3md");
	    debris_mat.setTexture("Texture", assetManager.loadTexture(
	            "Effects/Explosion/flame.png"));
	    debris.setMaterial(debris_mat);
	    debris.setImagesX(3); 
	    debris.setImagesY(3); // 3x3 texture animation
	    debris.setRotateSpeed(4);
	   
	    debris.setSelectRandomImage(true);
	    debris.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 4, 0));
	    debris.setStartColor(ColorRGBA.randomColor());
	    debris.setGravity(0, 6, 0);
	    debris.getParticleInfluencer().setVelocityVariation(.60f);
	    debris.move(8f, -10, 0f);
	    rootNode.attachChild(debris);
	    debris.emitAllParticles();
	    debris.setParticlesPerSec(speed);
		
		
		
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Level2 app = new Level2();
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
				player.setLinearVelocity(new Vector3f(0,0,0));
			}
		
	}

}
