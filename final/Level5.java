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

public class Level5 extends SimpleApplication implements ActionListener,PhysicsCollisionListener{
	private static Level5 app;
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
		// --------------- Zone de repos  ----------------
		Terrain r1 = new Terrain(2f, 0.7f,1f, ColorRGBA.Green);
		r1.getGeom().move(-19f,-7,-10f);
		Terrain r2 = new Terrain(1f, 0.7f,1f, ColorRGBA.Green);
		r2.getGeom().move(20f,-7,-10f);
		Terrain r3 = new Terrain(1.2f, 0.7f, 1f, ColorRGBA.Green);
		r3.getGeom().move(-20f, -7, -6f);
		Terrain r4 = new Terrain(0.8f, 0.7f, 2f, ColorRGBA.Green);
		r4.getGeom().move(9.6f,-7,-0.9f);
		// --------------- Terrain 1 3x6 ----------------
		Terrain t1 = new Terrain(18f, 0.7f,1f, ColorRGBA.White);
		t1.getGeom().move(1f,-7,-10f);
	
		// --------------- Terrain 2 2x1 ----------------
		Terrain t2 = new Terrain(1f, 0.7f, 8.7f, ColorRGBA.White);
		t2.getGeom().move(18f,-7,0f);
	// --------------- Terrain 3 10x4 ----------------
		Terrain t3 = new Terrain(15f, 0.7f, 1f, ColorRGBA.White);
		t3.getGeom().move(2.5f, -7, 7.7f);
		// --------------- Terrain 2 2x1 ----------------
		Terrain t4 = new Terrain(1f, 0.7f, 5.9f, ColorRGBA.White);
		t4.getGeom().move(-11.5f,-7,0.8f);
		// --------------- Terrain 4 2x1 ----------------
		Terrain t5 = new Terrain(16f, 0.7f, 1f, ColorRGBA.White);
		t5.getGeom().move(-2.8f, -7, -6f);
		// --------------- Terrain 5 3x6 ----------------
		Terrain t6 = new Terrain(1f, 0.7f, 6.25f, ColorRGBA.White);
		t6.getGeom().move(14f, -7, -0.75f);
		// --------------- Terrain 2 2x1 ----------------
		Terrain t7 = new Terrain(9.6f, 0.7f, 1f, ColorRGBA.White);
		t7.getGeom().move(3.4f,-7,4.5f);
		// --------------- Terrain 63x6 ----------------
		Terrain t8 = new Terrain(1f, 0.7f, 4.2f, ColorRGBA.White);
		t8.getGeom().move(-7.2f,-7,1.3f);
		// --------------- Terrain 2 2x1 ----------------
		Terrain t9 = new Terrain(1f, 0.7f, 1f, ColorRGBA.White);
		t9.getGeom().move(-5.18f,-7,-1.9f);
		// --------------- Terrain 63x6 ----------------
		Terrain t10 = new Terrain(6.5f, 0.7f, 2f, ColorRGBA.White);
		t10.getGeom().move(2.3f,-7,-0.9f);
		// --------------- Terrain Ground -----------
		t1.setGround();
		t2.setGround();
		t3.setGround();
		t4.setGround();
		t5.setGround();
		t6.setGround();
		t7.setGround();
		t8.setGround();
		t9.setGround();
		t10.setGround();
		// --------------- Terrain Collision -----------
		Terrain.setCollision();
                 
		// ----------- Configuration des Spheres ---------
		SphereObstacle.init(32, 32, 0.5f,assetManager);
		sphereGroup = new SphereGroup();
        spheres = sphereGroup.getNode();
        rootNode.attachChild(spheres);
        spheres.setLocalTranslation(new Vector3f(15,0,0));
        // ----------- Spheres 1 ---------
        SphereObstacle s1 = new SphereObstacle();
		s1.getGeom().move(-28,-6,-16f); 
        // ----------- Spheres 2 ---------
        SphereObstacle s2 = new SphereObstacle();
		s2.getGeom().move(-24f,-6f,-9f); 
		// ----------- Spheres 3 ---------
        SphereObstacle s3 = new SphereObstacle();
		s3.getGeom().move(-20,-6,-6);
		// ----------- Spheres 4 ---------
        SphereObstacle s4 = new SphereObstacle();
		s4.getGeom().move(-16,-6,-2.5f);
		// ----------- Spheres 4 ---------
		SphereObstacle s5 = new SphereObstacle();
		s5.getGeom().move(-12,-6,0.9f); 
        // ----------- Spheres 2 ---------
        SphereObstacle s6 = new SphereObstacle();
		s6.getGeom().move(-8f,-6f,4.8f); 
		// ----------- Spheres 3 ---------
        SphereObstacle s7 = new SphereObstacle();
		s7.getGeom().move(-4f,-6,7.2f);
		// ----------- Spheres 4 ---------
        SphereObstacle s8 = new SphereObstacle();
		s8.getGeom().move(0f,-6,10f);
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
        SphereObstacle sg1 = new SphereObstacle();
		sg1.getGeom().move(16,-6,-10.5f); 
        // ----------- Spheres 2 ---------
        SphereObstacle sg2 = new SphereObstacle();
		sg2.getGeom().move(12,-6,-8f); 
		// ----------- Spheres 3 ---------
        SphereObstacle sg3 = new SphereObstacle();
		sg3.getGeom().move(10,-6,-6);
		// ----------- Spheres 4 ---------
        SphereObstacle sg4 = new SphereObstacle();
		sg4.getGeom().move(4,-6,-2.5f);
		// ----------- Spheres 1 ---------
        SphereObstacle sg5 = new SphereObstacle();
	sg5.getGeom().move(-2,-6,1f); 
        // ----------- Spheres 2 ---------
        SphereObstacle sg6 = new SphereObstacle();
		sg6.getGeom().move(-6,-6,4.5f); 
		// ----------- Spheres 3 ---------
        SphereObstacle sg7 = new SphereObstacle();
		sg7.getGeom().move(-9,-6,7f);
		// ----------- Spheres 4 ---------
        SphereObstacle sg8 = new SphereObstacle();
		sg8.getGeom().move(-12,-6,10);
		// ----------- Add Spheres to the group ---------
		sphereGroup2.addSphere(sg1);
			sphereGroup2.addSphere(sg2);
		sphereGroup2.addSphere(sg3);
		sphereGroup2.addSphere(sg4);
		sphereGroup2.addSphere(sg5);
		sphereGroup2.addSphere(sg6);
			sphereGroup2.addSphere(sg7);
	   sphereGroup2.addSphere(sg8);
        // ----------- Spheres Collision ----------------
                CollisionShape sphereShape =
			    CollisionShapeFactory.createDynamicMeshShape(spheres);
		spherecontrol = new RigidBodyControl(sphereShape, 0);
                spherecontrol.setKinematic(true);
		spheres.addControl(spherecontrol);
		bulletAppState.getPhysicsSpace().add(spherecontrol);
                
                CollisionShape sphereShape2 =
			    CollisionShapeFactory.createDynamicMeshShape(spheres2);
		spherecontrol2 = new RigidBodyControl(sphereShape2, 0);
                spherecontrol2.setKinematic(true);
		spheres2.addControl(spherecontrol2);
		bulletAppState.getPhysicsSpace().add(spherecontrol2);
		// ---------------- Player -----------------------
		Box b1=new Box(Vector3f.ZERO, 0.6f, 0.6f, 0.6f);
		geom1 = new Geometry("Box", b1);
            Material matp = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	    Texture tex_ml =assetManager.loadTexture("Textures/player.png");
	    matp.setTexture("ColorMap", tex_ml);
        geom1.setMaterial(matp);                   // set the cube's material
        geom1.move(0f, -4f, 0f);
        
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
        finishFlag.move(30, -7, 5);
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
		moveObstacle(tpf);
                
		debris.setLocalTranslation(geom1.getLocalTranslation());
		camNode.lookAt(geom1.getLocalTranslation(), Vector3f.UNIT_Y);
		camNode.setLocalTranslation(geom1.getLocalTranslation().x, 10, geom1.getLocalTranslation().z+10);
    }


	private void moveObstacle(float tpf) {
        sphereGroup.getNode().rotate(0, 1f*tpf, 0);
		sphereGroup2.getNode().rotate(0, -1f*tpf, 0);
	}

        private void movePlayer() {
		speed = player.getLinearVelocity();
		player.setAngularVelocity(Vector3f.ZERO);
		if (touchTerrain && geom1.getLocalTranslation().y >=-8.95f) {
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
                else touchTerrain = false;

	}
        
	private void movePlayer(float tpf){
            player.setAngularVelocity(Vector3f.ZERO);
		speed = player.getLinearVelocity();
    	  if (touchTerrain && geom1.getLocalTranslation().y >=-8.95f) {
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
                
                app = new Level5();
                app.start(); // start the game

	}
        
        public void detachAll(){
            terrain.detachAllChildren();
            spheres.detachAllChildren();
            spheres2.detachAllChildren();
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
        if("Terrain".equals(event.getNodeA().getName())) 
            touchTerrain = true;
    
        if("Box".equals(event.getNodeA().getName()) && "Finish".equals(event.getNodeB().getName()))
            finish = true;
    
        if("TerrainFloor".equals(event.getNodeA().getName()) || "TerrainFloor".equals(event.getNodeB().getName())) 
            finish();
    }
    
    private void finish() {
        player.setPhysicsLocation(new Vector3f(0, -4, 0));
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
