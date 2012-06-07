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

public class Level2 extends SimpleApplication implements ActionListener{
	private FilterPostProcessor fpp;
	private FogFilter fog;
	private CameraNode camNode;
	private PssmShadowRenderer pssmRenderer;
	private BasicShadowRenderer bsr;
	private BulletAppState bulletAppState;
	private CubeGroup cubeGroup;
    private CubeGroup CubeGroups;
    private CubeGroup cubeGroup1;
    private CubeGroup CubeGroup2;
    private CubeGroup cubeBonus;
    private CubeGroup cubeBonus1;
    private CubeGroup cubeBonus2;
    private CubeGroup cubeBonus3;
    private Node Bonus;
    private Node Bonus1;
    private Node Bonus2;
    private Node Bonus3;
    
	protected Node spheres;
	private SphereGroup sphereGroup;
	protected Node terrain;
	protected Node cubes;
	protected Node cube;
	protected Node cube1;
	protected Node cube2;
	protected Node cube3;
	
	
	protected Node playerNode;
	protected Geometry geom1;
	protected Geometry levelgeom;
	protected Geometry s1geom;
	protected Geometry s2geom;
	protected RigidBodyControl player;
	protected RigidBodyControl spherecontrol;
	protected  ParticleEmitter debris ;
	protected float speedSphere = 4f;
	protected float speedSpheres = 3f;
	protected float speedSphere1 = 3f;
	protected float speedSphere2 = 3f;
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

        
		viewPort.setBackgroundColor(new ColorRGBA(2f, 0.0f, 2f, 0f));
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
		Terrain t1 = new Terrain(20f, 20f, 20f, ColorRGBA.White);
		//t1.getGeom().move(0,-10,0);
		// --------------- Terrain 2 2x1 ----------------
		Terrain t2 = new Terrain(9.5f, 0.5f, 0.5f, ColorRGBA.Green);
		t2.getGeom().move(-1f,20,10f);
		// --------------- Terrain 3 10x4 ----------------
		Terrain t3 = new Terrain(0.5f, 0.5f, 9.3f, ColorRGBA.Red);
		t3.getGeom().move(-10.0f, 20, 0.2f);
		// --------------- Terrain 4 2x1 ----------------
		Terrain t4 = new Terrain(9f, 0.5f, 0.5f, ColorRGBA.Yellow);
		t4.getGeom().move(-0.5f, 20, -9f);
		// --------------- Terrain 5 3x6 ----------------
		Terrain t5 = new Terrain(0.5f, 0.5f, 10f, ColorRGBA.Gray);
		t5.getGeom().move(9f, 20, 0.5f);
	
		// --------------- Terrain Collision -----------
		Terrain.setCollision();
//*****************************************************************************************************
		
		// ----------- Configuration des Cubes Groupe 1 ---------
		CubeObstacle.setAsset(assetManager);
		cubeGroup = new CubeGroup();
	cubes = cubeGroup.getNode();
		rootNode.attachChild(cubes);
		cubes.setLocalTranslation(new Vector3f(-2,0,0));
		// ----------- Cube 1 ---------
		CubeObstacle c1 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c1.getGeom().move(-6,20,8); 
		// ----------- Cube 2 ---------
		CubeObstacle c2 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c2.getGeom().move(-5f,20,8); 
		// ----------- Cube 3 ---------
		CubeObstacle c3 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c3.getGeom().move(-4,20,8); 
		// ----------- Cube 4 ---------
		CubeObstacle c4 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c4.getGeom().move(-3f,20,8); 
		// ----------- Cube 5 ---------
		CubeObstacle c5 =new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c5.getGeom().move(-2,20,8f); 
		// ----------- Cube 6 ---------
		CubeObstacle c6 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c6.getGeom().move(-1f,20,8); 
		// ----------- Cube 7 ---------
		CubeObstacle c7 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c7.getGeom().move(0f,20,8); 
		// ----------- Cube 8 ---------
		CubeObstacle c8 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c8.getGeom().move(1f,20,8); 
		// ----------- Cube 1 ---------
		CubeObstacle c9 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c9.getGeom().move(2,20,8); 
		// ----------- Cube 2 ---------
		CubeObstacle c10 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c10.getGeom().move(3,20,8); 
		// ----------- Cube 3 ---------
		CubeObstacle c11 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c11.getGeom().move(4,20,8); 
		// ----------- Cube 4 ---------
		CubeObstacle c12 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c12.getGeom().move(5,20,8); 
		// ----------- Cube 5 ---------
		CubeObstacle c13 =new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c13.getGeom().move(6,20,8f); 
		// ----------- Cube 6 ---------
		CubeObstacle c14 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c14.getGeom().move(7,20,8); 
		// ----------- Cube 7 ---------
		CubeObstacle c15 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c15.getGeom().move(8,20,8); 
		// ----------- Cube 8 ---------
		CubeObstacle c16 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c16.getGeom().move(9,20,8); 
		// ----------- Add Spheres to the group ---------
		cubeGroup.addCube(c1);//cubeGroup.addCube(c2);
		cubeGroup.addCube(c3);//cubeGroup.addCube(c4);
		cubeGroup.addCube(c5);//cubeGroup.addCube(c6);
		cubeGroup.addCube(c7);//cubeGroup.addCube(c8);
		cubeGroup.addCube(c9);//cubeGroup.addCube(c10);
		cubeGroup.addCube(c11);//cubeGroup.addCube(c12);
		cubeGroup.addCube(c13);//cubeGroup.addCube(c14);
		cubeGroup.addCube(c15);//cubeGroup.addCube(c16);
//***********************************************************************/
		// ----------- Configuration des Cubes Groupe 2 ---------
		
		CubeGroups = new CubeGroup();
		cube = CubeGroups.getNode();
		rootNode.attachChild(cube);
		cube.setLocalTranslation(new Vector3f(-6f,0,0));
		// ----------- Cube 1 ---------
		CubeObstacle c21 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Green);
		c21.getGeom().move(10,20,-8); 
		// ----------- Cube 2 ---------
		CubeObstacle c22 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Red);
		c22.getGeom().move(10,20,-7f); 
		// ----------- Cube 3 ---------
		CubeObstacle c23 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c23.getGeom().move(10f,20,-6); 
		// ----------- Cube 4 ---------
		CubeObstacle c24 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Yellow);
		c24.getGeom().move(10f,20,-5); 
		// ----------- Cube 5 ---------
		CubeObstacle c25 =new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Gray);
		c25.getGeom().move(10,20,-4); 
		// ----------- Cube 6 ---------
		CubeObstacle c26 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Black);
		c26.getGeom().move(10f,20,-3); 
		// ----------- Cube 7 ---------
		CubeObstacle c27 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Brown);
		c27.getGeom().move(10f,20,-2); 
		// ----------- Cube 8 ---------
		CubeObstacle c28 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Cyan);
		c28.getGeom().move(10f,20,-1); 
		// ----------- Cube 1 ---------
		CubeObstacle c29 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Magenta);
		c29.getGeom().move(10,20,0); 
		// ----------- Cube 2 ---------
		CubeObstacle c210 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Orange);
		c210.getGeom().move(10,20,1); 
		// ----------- Cube 3 ---------
		CubeObstacle c211 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Pink);
		c211.getGeom().move(10,20,2); 
		// ----------- Cube 4 ---------
		CubeObstacle c212 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.White);
		c212.getGeom().move(10,20,3); 
		// ----------- Cube 5 ---------
		CubeObstacle c213 =new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.DarkGray);
		c213.getGeom().move(10,20,4); 
		// ----------- Cube 6 ---------
		CubeObstacle c214 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.LightGray);
		c214.getGeom().move(10,20,5); 
		// ----------- Cube 7 ---------
		CubeObstacle c215 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.randomColor());
		c215.getGeom().move(10,20,6); 
		// ----------- Cube 8 ---------
		CubeObstacle c216 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c216.getGeom().move(10f,20,7); 
		// ----------- Add Spheres to the group ---------
		//CubeGroups.addCube(c21);//CubeGroups.addCube(c22);
		CubeGroups.addCube(c23);//CubeGroups.addCube(c24);
		CubeGroups.addCube(c25);//CubeGroups.addCube(c26);
		CubeGroups.addCube(c27);//CubeGroups.addCube(c28);
		CubeGroups.addCube(c29);//CubeGroups.addCube(c210);
		CubeGroups.addCube(c211);//CubeGroups.addCube(c212);
		CubeGroups.addCube(c213);//CubeGroups.addCube(c214);
		CubeGroups.addCube(c215);//CubeGroups.addCube(c216);
		
		// ----------- Configuration des Cubes Groupe 3 ---------
		CubeObstacle.setAsset(assetManager);
		cubeGroup1 = new CubeGroup();
		cube1 = cubeGroup1.getNode();
		rootNode.attachChild(cube1);
		cube1.setLocalTranslation(new Vector3f(-2,0,0));
		// ----------- Cube 1 ---------
		CubeObstacle c31 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Green);
		c31.getGeom().move(-6,20,-8); 
		// ----------- Cube 2 ---------
		CubeObstacle c32 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Red);
		c32.getGeom().move(-5,20,-8f); 
		// ----------- Cube 3 ---------
		CubeObstacle c33 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c33.getGeom().move(-3,20,-8); 
		// ----------- Cube 4 ---------
		CubeObstacle c34 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Yellow);
		c34.getGeom().move(-2f,20,-8); 
		// ----------- Cube 5 ---------
		CubeObstacle c35 =new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Gray);
		c35.getGeom().move(-1,20,-8); 
		// ----------- Cube 6 ---------
		CubeObstacle c36 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Black);
		c36.getGeom().move(0f,20,-8); 
		// ----------- Cube 7 ---------
		CubeObstacle c37 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Brown);
		c37.getGeom().move(1f,20,-8); 
		// ----------- Cube 8 ---------
		CubeObstacle c38 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Cyan);
		c38.getGeom().move(2f,20,-8); 
		// ----------- Cube 1 ---------
		CubeObstacle c39 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Magenta);
		c39.getGeom().move(3,20,-8); 
		// ----------- Cube2 ---------
		CubeObstacle c310 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Orange);
		c310.getGeom().move(4,20,-8); 
		// ----------- Cube 3 ---------
		CubeObstacle c311 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Pink);
		c311.getGeom().move(5,20,-8); 
		// ----------- Cube 4 ---------
		CubeObstacle c312 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.White);
		c312.getGeom().move(6,20,-8); 
		// ----------- Cube 5 ---------
		CubeObstacle c313 =new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.DarkGray);
		c313.getGeom().move(7,20,-8); 
		// ----------- Cube 6 ---------
		CubeObstacle c314 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.LightGray);
		c314.getGeom().move(8,20,-8); 
		// ----------- Cube 7 ---------
		CubeObstacle c315 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.randomColor());
		c315.getGeom().move(9,20,-8); 
		// ----------- Cube 8 ---------
		CubeObstacle c316 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c316.getGeom().move(10f,20,-8); 
		// ----------- Add Spheres to the group ---------
		cubeGroup1.addCube(c31);//cubeGroup1.addCube(c32);
		cubeGroup1.addCube(c33);//cubeGroup1.addCube(c34);
		cubeGroup1.addCube(c35);//cubeGroup1.addCube(c36);
		cubeGroup1.addCube(c37);//cubeGroup1.addCube(c38);
		cubeGroup1.addCube(c39);//cubeGroup1.addCube(c310);
		cubeGroup1.addCube(c311);//cubeGroup1.addCube(c312);
		cubeGroup1.addCube(c313);//cubeGroup1.addCube(c314);
		cubeGroup1.addCube(c315);//cubeGroup1.addCube(c316);
//***********************************************************************/
		// ----------- Configuration des Cubes Groupe 2 ---------
		
		CubeGroup2 = new CubeGroup();
		cube2 = CubeGroup2.getNode();
		rootNode.attachChild(cube2);
		cube2.setLocalTranslation(new Vector3f(-8f,0,0));
		// ----------- Cube 1 ---------
		CubeObstacle c41 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Green);
		c41.getGeom().move(2,20,-8); 
		// ----------- Cube 2 ---------
		CubeObstacle c42 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Red);
		c42.getGeom().move(2,20,-7f); 
		// ----------- Cube 3 ---------
		CubeObstacle c43 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c43.getGeom().move(2,20,-6); 
		// ----------- Cube 4 ---------
		CubeObstacle c44 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Yellow);
		c44.getGeom().move(2,20,-5); 
		// ----------- Cube 5 ---------
		CubeObstacle c45 =new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Gray);
		c45.getGeom().move(2,20,-4); 
		// ----------- Cube 6 ---------
		CubeObstacle c46 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Black);
		c46.getGeom().move(2,20,-3); 
		// ----------- Cube 7 ---------
		CubeObstacle c47 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Brown);
		c47.getGeom().move(2,20,-2); 
		// ----------- Cube 8 ---------
		CubeObstacle c48 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Cyan);
		c48.getGeom().move(2,20,-1); 
		// ----------- Cube 1 ---------
		CubeObstacle c49 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Magenta);
		c49.getGeom().move(2,20,0); 
		// ----------- Cube 2 ---------
		CubeObstacle c410 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Orange);
		c410.getGeom().move(2,20,1); 
		// ----------- Cube 3 ---------
		CubeObstacle c411 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Pink);
		c411.getGeom().move(2,20,2); 
		// ----------- Cube 4 ---------
		CubeObstacle c412 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.White);
		c412.getGeom().move(2,20,3); 
		// ----------- Cube 5 ---------
		CubeObstacle c413 =new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.DarkGray);
		c413.getGeom().move(2,20,4); 
		// ----------- Cube 6 ---------
		CubeObstacle c414 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.LightGray);
		c414.getGeom().move(2,20,5); 
		// ----------- Cube 7 ---------
		CubeObstacle c415 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.randomColor());
		c415.getGeom().move(2,20,6); 
		// ----------- Cube 8 ---------
		CubeObstacle c416 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Blue);
		c416.getGeom().move(2f,20,7); 
		// ----------- Add Spheres to the group ---------
		//CubeGroup2.addCube(c41);
		//CubeGroup2.addCube(c42);
		CubeGroup2.addCube(c43);
		//CubeGroup2.addCube(c44);
		CubeGroup2.addCube(c45);
		//CubeGroup2.addCube(c46);
		CubeGroup2.addCube(c47);
		//CubeGroup2.addCube(c48);
		CubeGroup2.addCube(c49);
		//CubeGroup2.addCube(c410);
		CubeGroup2.addCube(c411);
		//CubeGroup2.addCube(c412);
		CubeGroup2.addCube(c413);
		//CubeGroup2.addCube(c414);
		CubeGroup2.addCube(c415);
		//CubeGroup2.addCube(c416);
		
//*****************************************************************************************************************
		// ----------- Configuration des Spheres ---------
		
		
		// ----------- Configuration des Spheres ---------
		SphereObstacle.setAsset(assetManager);
		sphereGroup = new SphereGroup();
        spheres = sphereGroup.getNode();
        rootNode.attachChild(spheres);
        spheres.setLocalTranslation(new Vector3f(-1,0,0));
     // ----------- Spheres 1 ---------
        SphereObstacle s1 = new SphereObstacle(10, 10, 0.3f, ColorRGBA.Green);
		s1.getGeom().move(2f,20.5f,-4); 
        // ----------- Spheres 2 ---------
        SphereObstacle s2 = new SphereObstacle(10, 10, 0.3f, ColorRGBA.Blue);
		s2.getGeom().move(-2,20.5f,-4); 
		// ----------- Spheres 3 ---------
        SphereObstacle s3 = new SphereObstacle(10, 10, 0.3f, ColorRGBA.Yellow);
		s3.getGeom().move(2,20.5f,4);
		// ----------- Spheres 4 ---------
        SphereObstacle s4 = new SphereObstacle(10, 10, 0.3f, ColorRGBA.Red);
		s4.getGeom().move(-2,20.5f,4);
		// ----------- Spheres 4 ---------
		SphereObstacle s5 = new SphereObstacle(10, 10, 0.3f, ColorRGBA.Cyan);
		s5.getGeom().move(4.5f,20.5f,0f); 
        // ----------- Spheres 2 ---------
        SphereObstacle s6 = new SphereObstacle(10, 10, 0.3f, ColorRGBA.Blue);
		s6.getGeom().move(-4.5f,20.5f,0f); 
		// ----------- Spheres 3 ---------
        SphereObstacle s7 = new SphereObstacle(10, 10, 0.3f, ColorRGBA.Black);
		s7.getGeom().move(-4f,20.5f,-2f);
		// ----------- Spheres 4 ---------
        SphereObstacle s8 = new SphereObstacle(10, 10, 0.3f, ColorRGBA.Blue);
		s8.getGeom().move(4f,20.5f,-2f);
		// ----------- Spheres 4 ---------
		SphereObstacle s9 = new SphereObstacle(10, 10, 0.3f, ColorRGBA.Brown);
		s9.getGeom().move(4,20.5f,2f); 
        // ----------- Spheres 2 ---------
        SphereObstacle s10 = new SphereObstacle(10, 10, 0.3f, ColorRGBA.Pink);
		s10.getGeom().move(-4f,20.5f,2.5f); 
		// ----------- Spheres 3 ---------
        SphereObstacle s11 = new SphereObstacle(10, 10, 0.3f, ColorRGBA.DarkGray);
		s11.getGeom().move(-0.1f,20.5f,4.5f);
		// ----------- Spheres 4 ---------
        SphereObstacle s12 = new SphereObstacle(10, 10, 0.3f, ColorRGBA.Magenta);
		s12.getGeom().move(-2f,20.5f,4f);
		
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
		sphereGroup.addSphere(s12);
		 
//***************************************************************************************************	
		//-------------------Jetton a prendre-------------------------
		CubeObstacle.setAsset(assetManager);
		cubeBonus = new CubeGroup();
		cubeBonus1 = new CubeGroup();
		cubeBonus2 = new CubeGroup();
		cubeBonus3 = new CubeGroup();
        Bonus = cubeBonus.getNode();
        Bonus1 = cubeBonus1.getNode();
        Bonus2 = cubeBonus2.getNode();
        Bonus3 = cubeBonus3.getNode();
        rootNode.attachChild(Bonus);
        rootNode.attachChild(Bonus1);
        rootNode.attachChild(Bonus2);
        rootNode.attachChild(Bonus3);
        
        Bonus.setLocalTranslation(new Vector3f(0,0,0));
        Bonus1.setLocalTranslation(new Vector3f(0,0,0));
        Bonus2.setLocalTranslation(new Vector3f(0,0,0));
        Bonus3.setLocalTranslation(new Vector3f(0,0,0));

        
        CubeObstacle b = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Red);
		b.getGeom().move(-8f,20,8f);
		CubeObstacle bs1 = new CubeObstacle(0.2f, 0.3f, 0.2f, ColorRGBA.Red);
		bs1.getGeom().move(8f,20,8f);
	    CubeObstacle b2 = new CubeObstacle(0.2f, 0.2f, 0.2f, ColorRGBA.Red);
		b2.getGeom().move(-8f,20,-8f);
		CubeObstacle b3 = new CubeObstacle(0.2f, 0.2f, 0.2f, ColorRGBA.Red);
		b3.getGeom().move(8f,20,-8f);
	
		cubeBonus.addCube(b);
		cubeBonus1.addCube(bs1);
		cubeBonus2.addCube(b2);
		cubeBonus3.addCube(b3);

		// ---------------- Player -----------------------
		Box b1=new Box(Vector3f.ZERO, 0.3f, 0.3f, 0.3f);
		geom1 = new Geometry("Box", b1);
		geom1.updateModelBound();
		
		Material matp = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		
		assetManager.registerLocator("assets/Textures/", FileLocator.class);
		Texture tex_ml = assetManager.loadTexture("player.png");
		matp.setTexture("ColorMap", tex_ml);
		geom1.setMaterial(matp);                   // set the cube's material
		geom1.move(-1f, 30f, 0);

		BoxCollisionShape playerShape = new BoxCollisionShape(new Vector3f(0.2f, 0.2f, 0.2f));
		
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
		bonus();
	 moveEffet();
	moveObstacle();	
	moveObstacleGroupe2();
	moveObstacleGroupe3();
	moveObstacleGroupe4();
		
	 }
	private void bonus() {
		
		  if (cubeBonus.collideWith(geom1)) {
			 rootNode.detachChild(Bonus);
  		  }
		  if (cubeBonus1.collideWith(geom1)) {
				 rootNode.detachChild(Bonus1);
	  		  }
		  if (cubeBonus2.collideWith(geom1)) {
				 rootNode.detachChild(Bonus2);
	  		  }
		  if (cubeBonus3.collideWith(geom1)) {
				 rootNode.detachChild(Bonus3);
	  		  }
  	  
  	     	  
	}
	
	
	
private void moveObstacle() {
	if(cubeGroup.getNode().getLocalTranslation().z< -4f && speedSphere<0) speedSphere = 0.01f;
	if(cubeGroup.getNode().getLocalTranslation().z> 0f && speedSphere>0) speedSphere = -0.01f;
	cubeGroup.getNode().move(new Vector3f(0,0,speedSphere));
			sphereGroup.getNode().rotate(0,0.001f , 0);
    	  if (sphereGroup.collideWith(geom1)) {
    		  player.setPhysicsLocation(new Vector3f(-1f, 30f, 0f));
    		  player.setLinearVelocity(new Vector3f(0,0,0));
    	  }
    	  if (cubeGroup.collideWith(geom1)) {
    		  player.setPhysicsLocation(new Vector3f(-1f, 30f, 0f));
    		  player.setLinearVelocity(new Vector3f(0,0,0));
    	  }
}
private void moveObstacleGroupe2() {
	if(CubeGroups.getNode().getLocalTranslation().x <-6f && speedSpheres<0) speedSpheres = 0.01f;
	if(CubeGroups.getNode().getLocalTranslation().x >-1f && speedSpheres>0) speedSpheres = -0.01f;
	CubeGroups.getNode().move(new Vector3f(speedSpheres,0,0));
	if (CubeGroups.collideWith(geom1)) {
		  player.setPhysicsLocation(new Vector3f(-1f, 30f, 0f));
		  player.setLinearVelocity(new Vector3f(0,0,0));
	  }
	}
private void moveObstacleGroupe3() {
	if(cubeGroup1.getNode().getLocalTranslation().z <0f && speedSphere1<0) speedSphere1 = 0.001f;
	if(cubeGroup1.getNode().getLocalTranslation().z > 4f && speedSphere1>0) speedSphere1 = -0.001f;
	cubeGroup1.getNode().move(new Vector3f(0,0,speedSphere1));
	if (cubeGroup1.collideWith(geom1)) {
		  player.setPhysicsLocation(new Vector3f(-1f, 30f, 0f));
		  player.setLinearVelocity(new Vector3f(0,0,0));
	  }
	}
private void moveObstacleGroupe4() {
	if(CubeGroup2.getNode().getLocalTranslation().x <-11f && speedSphere2<0) speedSphere2 = 0.001f;
	if(CubeGroup2.getNode().getLocalTranslation().x > -8f && speedSphere2>0) speedSphere2 = -0.001f;
	CubeGroup2.getNode().move(new Vector3f(speedSphere2,0,0));
	if (CubeGroup2.collideWith(geom1)) {
		  player.setPhysicsLocation(new Vector3f(-1f, 30f, 0f));
		  player.setLinearVelocity(new Vector3f(0,0,0));
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
		camNode.setLocalTranslation(new Vector3f(geom1.getLocalTranslation().x, 35,15));

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
			player.setPhysicsRotation(Matrix3f.ZERO);
			player.setLinearVelocity(Vector3f.ZERO);
		}

	}

}
