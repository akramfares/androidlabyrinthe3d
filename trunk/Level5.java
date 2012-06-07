package androidlabyrinthe3d;


import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.collision.CollisionResults;
import com.jme3.effect.ParticleEmitter;
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

public class Level5 extends SimpleApplication implements ActionListener{
	private FilterPostProcessor fpp;
	private FogFilter fog;
	private CameraNode camNode;
	private PssmShadowRenderer pssmRenderer;
	private BasicShadowRenderer bsr;
	private BulletAppState bulletAppState;
	private SphereGroup sphereGroup;
	private SphereGroup sphereGroup2;
	protected Node spheres;
	protected Node spheres2;
	protected Node terrain;
	protected Node cubes;
	protected Node cubes2;
	protected Node playerNode;
	protected Geometry geom1;
	protected Geometry levelgeom;
	protected Geometry s1geom;
	protected Geometry s2geom;
	protected RigidBodyControl player;
	protected RigidBodyControl spherecontrol;
	protected  ParticleEmitter debris ;
	protected float speedSphere = 4f;
	protected float x,y,z;
	boolean isRunning=true;
	private boolean left = false, right = false, up = false, down = false;
	private Vector3f speed = Vector3f.ZERO;
	private CollisionResults results;
    private AudioNode audio;
	@Override
	public void simpleInitApp() {
		
		
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		bulletAppState.getPhysicsSpace().enableDebug(assetManager);

		BulletAppState bulletAppState=new BulletAppState();
		stateManager.attach(bulletAppState);

        
		viewPort.setBackgroundColor(new ColorRGBA(0f, 0.5f, 0.5f, 2f));
		cam.setLocation(new Vector3f(10,20,10));

		Effet eff=new Effet();
		Material mat_red = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		    mat_red.setTexture("Texture", assetManager.loadTexture(
		            "Effects/Explosion/flame.png"));
		     debris=eff.getParticleEmitter();
		    debris.setMaterial(mat_red);
		    
		 
		rootNode.attachChild(debris);
		playerNode = new Node("Player");
		// playerNode.attachChild(debris);
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
		// --------------- Zone de repos  ----------------
		Terrain r1 = new Terrain(2f, 0.7f,1f, ColorRGBA.Green);
		r1.getGeom().move(-19f,-25f,-10f);
		Terrain r2 = new Terrain(1f, 0.7f,1f, ColorRGBA.Green);
		r2.getGeom().move(20f,-25f,-10f);
		Terrain r3 = new Terrain(1.2f, 0.7f, 1f, ColorRGBA.Green);
		r3.getGeom().move(-20f, -25f, -6f);
		Terrain r4 = new Terrain(0.8f, 0.7f, 2f, ColorRGBA.Green);
		r4.getGeom().move(9.6f,-25,-0.9f);
		// --------------- Terrain 1 3x6 ----------------
		Terrain t1 = new Terrain(18f, 0.7f,1f, ColorRGBA.White);
		t1.getGeom().move(1f,-25f,-10f);
	
		// --------------- Terrain 2 2x1 ----------------
		Terrain t2 = new Terrain(1f, 0.7f, 8.7f, ColorRGBA.White);
		t2.getGeom().move(18f,-25f,0f);
	// --------------- Terrain 3 10x4 ----------------
		Terrain t3 = new Terrain(15f, 0.7f, 1f, ColorRGBA.White);
		t3.getGeom().move(2.5f, -25f, 7.7f);
		// --------------- Terrain 2 2x1 ----------------
		Terrain t4 = new Terrain(1f, 0.7f, 5.9f, ColorRGBA.White);
		t4.getGeom().move(-11.5f,-25f,0.8f);
		// --------------- Terrain 4 2x1 ----------------
		Terrain t5 = new Terrain(16f, 0.7f, 1f, ColorRGBA.White);
		t5.getGeom().move(-2.8f, -25f, -6f);
		// --------------- Terrain 5 3x6 ----------------
		Terrain t6 = new Terrain(1f, 0.7f, 6.25f, ColorRGBA.White);
		t6.getGeom().move(14f, -25, -0.75f);
		// --------------- Terrain 2 2x1 ----------------
		Terrain t7 = new Terrain(9.6f, 0.7f, 1f, ColorRGBA.White);
		t7.getGeom().move(3.4f,-25,4.5f);
		// --------------- Terrain 63x6 ----------------
		Terrain t8 = new Terrain(1f, 0.7f, 4.2f, ColorRGBA.White);
		t8.getGeom().move(-7.2f,-25,1.3f);
		// --------------- Terrain 2 2x1 ----------------
		Terrain t9 = new Terrain(1f, 0.7f, 1f, ColorRGBA.White);
		t9.getGeom().move(-5.18f,-25,-1.9f);
		// --------------- Terrain 63x6 ----------------
		Terrain t10 = new Terrain(6.5f, 0.7f, 2f, ColorRGBA.White);
		t10.getGeom().move(2.3f,-25,-0.9f);
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
		s1.getGeom().move(-28,-25,-16f); 
        // ----------- Spheres 2 ---------
        SphereObstacle s2 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		s2.getGeom().move(-24f,-24f,-9f); 
		// ----------- Spheres 3 ---------
        SphereObstacle s3 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		s3.getGeom().move(-20,-24,-6);
		// ----------- Spheres 4 ---------
        SphereObstacle s4 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		s4.getGeom().move(-16,-24,-2.5f);
		// ----------- Spheres 4 ---------
		SphereObstacle s5 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		s5.getGeom().move(-12,-24,0.9f); 
        // ----------- Spheres 2 ---------
        SphereObstacle s6 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		s6.getGeom().move(-8f,-24f,4.8f); 
		// ----------- Spheres 3 ---------
        SphereObstacle s7 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		s7.getGeom().move(-4f,-24,7.2f);
		// ----------- Spheres 4 ---------
        SphereObstacle s8 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		s8.getGeom().move(0f,-24,10f);
		// ----------- Add Spheres to the group ---------
		sphereGroup.addSphere(s1);
		sphereGroup.addSphere(s2);
		sphereGroup.addSphere(s3);
		sphereGroup.addSphere(s4);
		sphereGroup.addSphere(s5);
		sphereGroup.addSphere(s6);
		sphereGroup.addSphere(s7);
		sphereGroup.addSphere(s8);
		
		// ----------- Configuration des Spheres ---------
		sphereGroup2 = new SphereGroup();
		spheres2 = sphereGroup2.getNode();
        rootNode.attachChild(spheres2);
        spheres2.setLocalTranslation(new Vector3f(0,0,0));
        // ----------- Spheres 1 ---------
        SphereObstacle sg1 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Green);
		sg1.getGeom().move(16,-24,-10.5f); 
        // ----------- Spheres 2 ---------
        SphereObstacle sg2 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		sg2.getGeom().move(12,-24,-8f); 
		// ----------- Spheres 3 ---------
        SphereObstacle sg3 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Green);
		sg3.getGeom().move(10,-24,-6);
		// ----------- Spheres 4 ---------
        SphereObstacle sg4 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Green);
		sg4.getGeom().move(4,-24,-2.5f);
		// ----------- Spheres 1 ---------
        SphereObstacle sg5 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
	sg5.getGeom().move(-2,-24,1f); 
        // ----------- Spheres 2 ---------
        SphereObstacle sg6 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		sg6.getGeom().move(-6,-24,4.5f); 
		// ----------- Spheres 3 ---------
        SphereObstacle sg7 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		sg7.getGeom().move(-9,-24,7f);
		// ----------- Spheres 4 ---------
        SphereObstacle sg8 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		sg8.getGeom().move(-12,-24,10);
		// ----------- Add Spheres to the group ---------
	sphereGroup2.addSphere(sg1);
		sphereGroup2.addSphere(sg2);
	sphereGroup2.addSphere(sg3);
	sphereGroup2.addSphere(sg4);
	sphereGroup2.addSphere(sg5);
	sphereGroup2.addSphere(sg6);
		sphereGroup2.addSphere(sg7);
   sphereGroup2.addSphere(sg8);
		
		// ---------------- Player -----------------------
		Box b1=new Box(Vector3f.ZERO, 0.6f, 0.6f, 0.6f);
		geom1 = new Geometry("Box", b1);
		geom1.updateModelBound();
		
		Material matp = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		
		assetManager.registerLocator("assets/Textures/", FileLocator.class);
		Texture tex_ml = assetManager.loadTexture("player.png");
		matp.setTexture("ColorMap", tex_ml);
		geom1.setMaterial(matp);                   // set the cube's material
		geom1.move(-3f, 30f, -10f);

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
		//moveEffet();
		moveObstacle();
	}

	private void moveObstacle() {
		/*if(s1geom.getLocalTranslation().x < 5f && speedSphere<0) speedSphere = 0.5f;
		if(s1geom.getLocalTranslation().x > 25f && speedSphere>0) speedSphere = -0.5f;
		s1geom.move(new Vector3f(speedSphere,0,0));*/
		sphereGroup.getNode().rotate(0, 0.001f, 0);
		sphereGroup2.getNode().rotate(0, -0.001f, 0);
		float x=player.getPhysicsLocation().x ;
		float z=player.getPhysicsLocation().z;
		System.out.println("abscisse :" + x );
		if(x<20 && (z==(-10f))){
    	  if ((sphereGroup.collideWith(geom1))) {
    		  
    			        player.setPhysicsLocation(new Vector3f(-18f,-24f,-10f));
    	    		  player.setLinearVelocity(new Vector3f(0,0,0));
    	    	     
    		  }
    	  }
    	  
	}
	private void moveEffet() {
		debris.setLocalTranslation(player.getPhysicsLocation().x,player.getPhysicsLocation().y,
				player.getPhysicsLocation().z);
		}

	private void movePlayer() {
		speed = player.getLinearVelocity();
		player.setAngularVelocity(Vector3f.ZERO);
		
		
		// Collision
		results = new CollisionResults();
		
		BoundingVolume bv2 = geom1.getWorldBound();
		terrain.collideWith(bv2, results);
		
		if (results.size() > 0) {
		
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
		camNode.setLocalTranslation(new Vector3f(geom1.getLocalTranslation().x, 25, 20));

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Level5 app = new Level5();
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
