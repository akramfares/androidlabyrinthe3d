package androidlabyrinthe3d;


import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
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

public class Level4 extends SimpleApplication implements ActionListener{
	private FilterPostProcessor fpp;
	private FogFilter fog;
	private CameraNode camNode;
	private PssmShadowRenderer pssmRenderer;
	private BasicShadowRenderer bsr;
	private BulletAppState bulletAppState;
	private CubeGroup cubeGroup;
	private CubeGroup cubeGroup2;
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

	@Override
	public void simpleInitApp() {
		
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		bulletAppState.getPhysicsSpace().enableDebug(assetManager);

		BulletAppState bulletAppState=new BulletAppState();
		stateManager.attach(bulletAppState);


		viewPort.setBackgroundColor(new ColorRGBA(0.0f, 0.0f, 0f, 0f));
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
		Terrain t1 = new Terrain(3, 1f, 2f, ColorRGBA.Green);
		t1.getGeom().move(0,-10,0);
		// --------------- Terrain 2 2x1 ----------------
		Terrain t2 = new Terrain(1f, 0.5f, 1f, ColorRGBA.White);
		t2.getGeom().move(4,-10,0);
		// --------------- Terrain 3 10x4 ----------------
		Terrain t3 = new Terrain(1f, 0.5f, 8f, ColorRGBA.White);
		t3.getGeom().move(6f, -10, 7f);
		// --------------- Terrain 4 2x1 ----------------
		Terrain t4 = new Terrain(4, 0.5f, 1, ColorRGBA.White);
		t4.getGeom().move(9f, -10, 16f);
		// --------------- Terrain 5 3x6 ----------------
		Terrain t5 = new Terrain(1, 0.5f, 8f, ColorRGBA.White);
		t5.getGeom().move(12f, -10, 7f);
		// --------------- Terrain 63x6 ----------------
		Terrain t6 = new Terrain(3, 0.5f, 2f, ColorRGBA.Green);
		t6.getGeom().move(14f,-10,-3f);
		// --------------- Terrain Collision -----------
		Terrain.setCollision();

		// ----------- Configuration des Cubes ---------
		CubeObstacle.setAsset(assetManager);
		cubeGroup = new CubeGroup();
		cubes = cubeGroup.getNode();
		rootNode.attachChild(cubes);
		cubes.setLocalTranslation(new Vector3f(-2,0,0));
		// ----------- Cube 1 ---------
		CubeObstacle c1 = new CubeObstacle(0.6f, 0.6f, 0.6f, ColorRGBA.Blue);
		c1.getGeom().move(9,-8,0); 
		// ----------- Cube 2 ---------
		CubeObstacle c2 = new CubeObstacle(0.6f, 0.6f, 0.6f, ColorRGBA.Blue);
		c2.getGeom().move(6.5f,-8,4); 
		// ----------- Cube 3 ---------
		CubeObstacle c3 = new CubeObstacle(0.6f, 0.6f, 0.6f, ColorRGBA.Blue);
		c3.getGeom().move(9,-8,8); 
		// ----------- Cube 4 ---------
		CubeObstacle c4 = new CubeObstacle(0.6f, 0.6f, 0.6f, ColorRGBA.Blue);
		c4.getGeom().move(6.5f,-8,12); 
		// ----------- Cube 5 ---------
		CubeObstacle c5 = new CubeObstacle(0.6f, 0.6f, 0.6f, ColorRGBA.Blue);
		c5.getGeom().move(10,-8,15.25f); 
		// ----------- Cube 6 ---------
		CubeObstacle c6 = new CubeObstacle(0.6f, 0.6f, 0.6f, ColorRGBA.Blue);
		c6.getGeom().move(12.5f,-8,4); 
		// ----------- Cube 7 ---------
		CubeObstacle c7 = new CubeObstacle(0.6f, 0.6f, 0.6f, ColorRGBA.Blue);
		c7.getGeom().move(14.75f,-8,8); 
		// ----------- Cube 8 ---------
		CubeObstacle c8 = new CubeObstacle(0.6f, 0.6f, 0.6f, ColorRGBA.Blue);
		c8.getGeom().move(12.5f,-8,12); 
		// ----------- Add Spheres to the group ---------
		cubeGroup.addCube(c1);
		cubeGroup.addCube(c2);
		cubeGroup.addCube(c3);
		cubeGroup.addCube(c4);
		cubeGroup.addCube(c5);
		cubeGroup.addCube(c6);
		cubeGroup.addCube(c7);
		cubeGroup.addCube(c8);

		// ----------- Configuration des Spheres ---------
		

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
		moveEffet();
		moveObstacle();
	}

	private void moveObstacle() {
		

		if (cubeGroup.collideWith(geom1)) {
             debris.setEndSize(2);
			player.setPhysicsLocation(new Vector3f(0, 5f, 0));
			player.setLinearVelocity(new Vector3f(0,0,0));
		}

		debris.setEndSize(0.1f);

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
		Level4 app = new Level4();
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
