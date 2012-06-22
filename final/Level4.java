package mygame;


import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.effect.ParticleEmitter;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

public class Level4 extends SimpleApplication implements ActionListener,PhysicsCollisionListener{
	private static Level4 app;
	private CameraNode camNode;
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
	private float vX, vY;
	private CollisionResults results;
        protected  ParticleEmitter debris ;
        private Vector3f camtranslation = new Vector3f(0, 10, 10);
        private RigidBodyControl spherecontrol2;
        private boolean touchTerrain=false;
        private boolean finish=false;
        private CubeGroup cubeGroup;
        private Node cubes;
        private RigidBodyControl cubecontrol;
        private BitmapText txt;
        private BitmapText txtHS;
        private float score=0;
        private int highscore=0;
	
	@Override
	public void simpleInitApp() {
		setDisplayFps(false);
                setDisplayStatView(false);
		
                rootNode.detachAllChildren();
		
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
                bulletAppState.getPhysicsSpace().addCollisionListener(this);
                //bulletAppState.getPhysicsSpace().enableDebug(assetManager);
		cam.setLocation(new Vector3f(10,20,10));
		
                 BitmapFont fnt = assetManager.loadFont("Interface/Fonts/Default.fnt");
                txt = new BitmapText(fnt, false);
                txt.setBox(new Rectangle(0, 0, settings.getWidth(), settings.getHeight()));
                txt.setSize(fnt.getPreferredSize() * 2f);
                txt.setText("Score : "+(int)score+"\n");
                txt.setLocalTranslation(0, txt.getHeight()-40, 0);
                guiNode.attachChild(txt);
                
                txtHS = new BitmapText(fnt, false);
                txtHS.setBox(new Rectangle(0, 0, settings.getWidth(), settings.getHeight()));
                txtHS.setSize(fnt.getPreferredSize() * 2f);
                txtHS.setText("High Score : "+(int)highscore+"\n");
                txtHS.setLocalTranslation(0, txtHS.getHeight(), 0);
                guiNode.attachChild(txtHS);
		
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
        setTextureFloor("magma");
        Terrain.setTextureHaut("player_rock");
        Terrain.setTextureBas("magma_sphere");
		// --------------- Terrain 1 3x6 ----------------
		Terrain t1 = new Terrain(3, 1f, 2f, ColorRGBA.Green);
		t1.getGeom().move(0,-10,0);
		// --------------- Terrain 2 2x1 ----------------
		Terrain t2 = new Terrain(1f, 0.5f, 1f, ColorRGBA.White);
		t2.getGeom().move(4,-10,0);
		// --------------- Terrain 3 10x4 ----------------
		Terrain t3 = new Terrain(2f, 0.5f, 8f, ColorRGBA.White);
		t3.getGeom().move(6f, -10, 7f);
		// --------------- Terrain 2 2x1 ----------------
		Terrain t7 = new Terrain(3, 1f, 2f, ColorRGBA.Gray);
		t7.getGeom().move(0.8f,-10,7f);
		// --------------- Terrain 4 2x1 ----------------
		Terrain t4 = new Terrain(8, 0.5f, 2f, ColorRGBA.White);
		t4.getGeom().move(12f, -10, 17.12f);
		// --------------- Terrain 5 3x6 ----------------
		Terrain t5 = new Terrain(2f, 0.5f, 8f, ColorRGBA.White);
		t5.getGeom().move(18.2f, -10, 7f);
		// --------------- Terrain 2 2x1 ----------------
		Terrain t8 = new Terrain(2, 1f, 2f, ColorRGBA.Gray);
		t8.getGeom().move(14f,-10,6f);
		// --------------- Terrain 63x6 ----------------
		Terrain t6 = new Terrain(4, 0.5f, 2f, ColorRGBA.Green);
		t6.getGeom().move(20f,-10,-3f);
		// --------------- Terrain Ground --------------
                t1.setGround();
		t2.setGround();
		t3.setGround();
		t4.setGround();
		t5.setGround();
		t6.setGround();
		t7.setGround();
                t8.setGround();
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
		c6.getGeom().move(18.5f,-8,4); 
		// ----------- Cube 7 ---------
		CubeObstacle c7 = new CubeObstacle(0.6f, 0.6f, 0.6f, ColorRGBA.Blue);
		c7.getGeom().move(21f,-8,8); 
		// ----------- Cube 8 ---------
		CubeObstacle c8 = new CubeObstacle(0.6f, 0.6f, 0.6f, ColorRGBA.Blue);
		c8.getGeom().move(18.5f,-8,12); 
		// ----------- Add Spheres to the group ---------
		cubeGroup.addCube(c1);
		cubeGroup.addCube(c2);
		cubeGroup.addCube(c3);
		cubeGroup.addCube(c4);
		cubeGroup.addCube(c5);
		cubeGroup.addCube(c6);
		cubeGroup.addCube(c7);
		cubeGroup.addCube(c8);
        // ----------- Cube Collision ----------------
                CollisionShape cubeShape =
			    CollisionShapeFactory.createDynamicMeshShape(cubes);
		cubecontrol = new RigidBodyControl(cubeShape, 0);
                cubecontrol.setKinematic(true);
		cubes.addControl(cubecontrol);
		bulletAppState.getPhysicsSpace().add(cubecontrol);
		// ---------------- Player -----------------------
		Box b1=new Box(Vector3f.ZERO, 0.6f, 0.6f, 0.6f);
		geom1 = new Geometry("Box", b1);
            Material matp = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	    Texture tex_ml =assetManager.loadTexture("Textures/player.png");
	    matp.setTexture("ColorMap", tex_ml);
        geom1.setMaterial(matp);                   // set the cube's material
        geom1.move(0, 5f, 0);
        
        BoxCollisionShape playerShape = new BoxCollisionShape(new Vector3f(0.6f, 0.6f, 0.6f));
        player = new RigidBodyControl(playerShape, 3);
		geom1.addControl(player);
         bulletAppState.getPhysicsSpace().add(player);
    	playerNode.attachChild(geom1);
    	
        // ---------------- Particles --------------------------
        Effet eff=new Effet();
		Material mat_red = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		    mat_red.setTexture("Texture", assetManager.loadTexture("Textures/flame.png"));
		     debris=eff.getParticleEmitter();
		    debris.setMaterial(mat_red);
		 
		rootNode.attachChild(debris);
        // ---------------- Finish Flag ------------------------
         Box mesh = new Box(1, 0.2f, 1);
        Geometry finishFlag=new Geometry("Finish",mesh);
        finishFlag.move(20, -7, -3);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mat.setColor("Color", ColorRGBA.Yellow);
        finishFlag.setMaterial(mat);
        GhostControl ghost = new GhostControl(new BoxCollisionShape(new Vector3f(1,1,1)));
        finishFlag.addControl(ghost);
        bulletAppState.getPhysicsSpace().add(ghost);
        rootNode.attachChild(finishFlag);

    	setUpKeys();
            
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
	
	
	
        public void setHighScore(int hs){
        highscore = hs;
        }
        public float getScore(){
            return score;
        }
	
        @Override
	public void simpleUpdate(float tpf) {
	
                score += tpf;
                txt.setText("Score : "+ (int)score+"\n");
		movePlayer(tpf);
                
		debris.setLocalTranslation(geom1.getLocalTranslation());
		camNode.lookAt(geom1.getLocalTranslation(), Vector3f.UNIT_Y);
		camNode.setLocalTranslation(new Vector3f(geom1.getLocalTranslation().x, 10, geom1.getLocalTranslation().z+10));
    }


        private void movePlayer() {
		speed = player.getLinearVelocity();
		player.setAngularVelocity(Vector3f.ZERO);
		if (touchTerrain) {
			if (left)  { if(speed.x > -4f) speed.x -= 0.4f; }
			if (right) { if(speed.x < 4f) speed.x += 0.4f; }
			if (up)    { if(speed.z > -4f) speed.z -= 0.4f; }
			if (down)  { if(speed.z < 4f) speed.z += 0.4f; }
			if( left || right || up || down){
				player.setLinearVelocity(speed);
			}
		}
                else touchTerrain = false;

	}
        
	private void movePlayer(float tpf){
            player.setAngularVelocity(Vector3f.ZERO);
		speed = player.getLinearVelocity();
    	  if (touchTerrain) {
		speed.x += vX*tpf; 
		speed.z += vY*tpf;
		player.setLinearVelocity(speed);
          }
          else touchTerrain = false;
          
          
	}
	public void setPlayerVelocity(float x, float y){
		vX = x;
		vY = y;
        }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
                
                app = new Level4();
                app.start(); // start the game

	}
        
        public void detachAll(){
            terrain.detachAllChildren();
            cubes.detachAllChildren();
            rootNode.detachAllChildren();            
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
                                touchTerrain = false;
                        }
		
	}

    public boolean getFinish(){
        return finish;
    }
    public void setFinish(boolean f){
        finish = f;
    }
    
    
    public void collision(PhysicsCollisionEvent event) {
        if("Spheres".equals(event.getNodeA().getName())){
                finish();
        }
        if("Terrain".equals(event.getNodeA().getName())) {
            touchTerrain = true;
        }
        
        if("TerrainFloor".equals(event.getNodeA().getName()) || "TerrainFloor".equals(event.getNodeB().getName())) 
            finish();
        
        if("Box".equals(event.getNodeA().getName()) && "Finish".equals(event.getNodeB().getName()))
            finish = true;
    
    }
    
    private void finish() {
        player.setPhysicsLocation(new Vector3f(0, 5f, 0));
        player.setPhysicsRotation(Matrix3f.ZERO);
        player.setLinearVelocity(Vector3f.ZERO);
        score =0;
        touchTerrain = false;
    }

    private void setTextureFloor(String tex) {
        Box wmesh=new Box(Vector3f.ZERO, 100,1,100);
            Geometry wgeom=new Geometry("TerrainFloor",wmesh);
            Material gmat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            Texture tex_ml = null;
            tex_ml = assetManager.loadTexture("Textures/"+tex+".jpg");
            tex_ml.setWrap(Texture.WrapMode.Repeat);
            gmat.setTexture("ColorMap", tex_ml);
            wmesh.scaleTextureCoordinates(new Vector2f(40,40));
            wgeom.setMaterial(gmat);
            wgeom.move(0, -20, 0);
            rootNode.attachChild(wgeom);
            
            CollisionShape sphereShape =
			    CollisionShapeFactory.createDynamicMeshShape(wgeom);
		RigidBodyControl wcontrol = new RigidBodyControl(sphereShape, 0);
                wcontrol.setKinematic(true);
		wgeom.addControl(wcontrol);
		bulletAppState.getPhysicsSpace().add(wcontrol);
    }

}
