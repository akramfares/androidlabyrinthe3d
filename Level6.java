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

public class Level6 extends SimpleApplication implements ActionListener{
	private FilterPostProcessor fpp;
	private FogFilter fog;
	private CameraNode camNode;
	private PssmShadowRenderer pssmRenderer;
	private BasicShadowRenderer bsr;
	private BulletAppState bulletAppState;
	private SphereGroup sphereGroup;
	private SphereGroup sphereGroup2;
	private CubeGroup cubeGroup;
	protected Node cubes;
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
		// --------------- Terrain 1 3x6 ----------------
		Terrain t1 = new Terrain(2f, 0.5f, 2f, ColorRGBA.Green);
		t1.getGeom().move(-5.09f,-8,-9f);
		// --------------- Terrain 2 2x1 ----------------
		Terrain t2 = new Terrain(7f, 0.5f, 7f, ColorRGBA.White);
		t2.getGeom().move(0,-8,0);
		
		// --------------- Terrain Collision -----------
		Terrain.setCollision();
    	// ----------- Configuration des Spheres ---------
		
		//-------------------------------------------------------
		SphereObstacle.setAsset(assetManager);
		sphereGroup = new SphereGroup();
		sphereGroup2 = new SphereGroup();
        spheres = sphereGroup.getNode();
        spheres2 = sphereGroup2.getNode();
        rootNode.attachChild(spheres);
        rootNode.attachChild(spheres2);
        spheres.setLocalTranslation(new Vector3f(0,0,0));
        spheres2.setLocalTranslation(new Vector3f(0,0,0));
        //--------------------------------------------------
        SphereObstacle s21 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Red);
        s21.getGeom().move(-5f,-7,-9); 
		sphereGroup2.addSphere(s21);
		
     // ----------- Spheres 1 ---------
        SphereObstacle s1 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Green);
		s1.getGeom().move(2f,-7,-4); 
        // ----------- Spheres 2 ---------
        SphereObstacle s2 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		s2.getGeom().move(-2,-7,-4); 
		// ----------- Spheres 3 ---------
        SphereObstacle s3 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Yellow);
		s3.getGeom().move(2,-7,4);
		// ----------- Spheres 4 ---------
        SphereObstacle s4 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Red);
		s4.getGeom().move(-2,-7,4);
		// ----------- Spheres 4 ---------
		SphereObstacle s5 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Cyan);
		s5.getGeom().move(4.5f,-7,0f); 
        // ----------- Spheres 2 ---------
        SphereObstacle s6 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		s6.getGeom().move(-4.5f,-7,0f); 
		// ----------- Spheres 3 ---------
        SphereObstacle s7 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Black);
		s7.getGeom().move(-4f,-7,-2f);
		// ----------- Spheres 4 ---------
        SphereObstacle s8 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Blue);
		s8.getGeom().move(4f,-7,-2f);
		// ----------- Spheres 4 ---------
		SphereObstacle s9 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Brown);
		s9.getGeom().move(4,-7,2f); 
        // ----------- Spheres 2 ---------
        SphereObstacle s10 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Pink);
		s10.getGeom().move(-4f,-7,2.5f); 
		// ----------- Spheres 3 ---------
        SphereObstacle s11 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.DarkGray);
		s11.getGeom().move(-0.1f,-7,4.5f);
		// ----------- Spheres 4 ---------
        SphereObstacle s12 = new SphereObstacle(32, 32, 0.5f, ColorRGBA.Magenta);
		s12.getGeom().move(-2f,-7,4f);
		// ----------- Add Spheres to the group ---------
		sphereGroup.addSphere(s1);
		sphereGroup.addSphere(s2);
	    sphereGroup.addSphere(s3);
     	sphereGroup.addSphere(s4);
	    sphereGroup.addSphere(s5);
		sphereGroup.addSphere(s6);
	    sphereGroup.addSphere(s7);
	    sphereGroup.addSphere(s8);
		sphereGroup.addSphere(s9);
		sphereGroup.addSphere(s10);
	    sphereGroup.addSphere(s11);
//		sphereGroup.addSphere(s12);
		//-------------------Jetton a prendre-------------------------
		CubeObstacle.setAsset(assetManager);
		cubeGroup = new CubeGroup();
        cubes = cubeGroup.getNode();
        rootNode.attachChild(cubes);
        cubes.setLocalTranslation(new Vector3f(0,0,0));
        CubeObstacle c = new CubeObstacle(0.5f, 0.5f, 0.5f, ColorRGBA.Red);
		c.getGeom().move(-5.5f,-7,0f);
		

		// ---------------- Player -----------------------
		Box b1=new Box(Vector3f.ZERO, 0.6f, 0.6f, 0.6f);
		geom1 = new Geometry("Box", b1);
		geom1.updateModelBound();
		
		Material matp = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		
		assetManager.registerLocator("assets/Textures/", FileLocator.class);
		Texture tex_ml = assetManager.loadTexture("player.png");
		matp.setTexture("ColorMap", tex_ml);
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
	moveObstacle();
		bonus();
	}
private void bonus() {
		
		  if (sphereGroup2.collideWith(geom1)) {
			 rootNode.detachChild(spheres2);
    		 
    	  }
    	  
    	     	  
	}
	private void moveObstacle() {
		
		sphereGroup.getNode().rotate(0, 0.002f, 0);
		
		
    	  if (sphereGroup.collideWith(geom1)) {
    		  player.setPhysicsLocation(new Vector3f(0, 5f, 0));
    		  player.setLinearVelocity(new Vector3f(0,0,0));
    		  rootNode.attachChild(spheres2);
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
		camNode.setLocalTranslation(new Vector3f(geom1.getLocalTranslation().x, 25, 20));

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Level6 app = new Level6();
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
