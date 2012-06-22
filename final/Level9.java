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

public class Level9 extends SimpleApplication implements ActionListener,PhysicsCollisionListener{
	private static Level9 app;
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
        setTextureFloor("water");
        Terrain.setTextureHaut("grass");
        Terrain.setTextureBas("ground");
	// --------------- Terrain 1 -------------------
        Terrain t1 = new Terrain(20f, 0.5f, 20f, ColorRGBA.Green);
        t1.getGeom().move(0f,-8,0f);
        // --------------- Terrain Collision -----------
        Terrain.setCollision();
        // ----------- Configuration des Cubes ---------
		CubeObstacle.setAsset(assetManager);
		cubeGroup = new CubeGroup();
		cubes = cubeGroup.getNode();
		rootNode.attachChild(cubes);
		cubes.setLocalTranslation(new Vector3f(-2,0,0));
		// ----------- Cube 1 ---------
		CubeObstacle c1 = new CubeObstacle(8f, 0.6f, 0.6f, ColorRGBA.Blue);
		c1.getGeom().move(-10,-7,-16); 
		// ----------- Cube 2 ---------
		CubeObstacle c2 = new CubeObstacle(4f, 0.6f, 0.6f, ColorRGBA.Blue);
		c2.getGeom().move(15,-7,-16); 
		// ----------- Cube 3 ---------
		CubeObstacle c3 = new CubeObstacle(0.6f, 0.6f, 5.5f, ColorRGBA.Blue);
		c3.getGeom().move(-1.5f,-7,-11f); 
		// ----------- Cube 4 ---------
		CubeObstacle c4 = new CubeObstacle(0.6f, 0.6f, 2f, ColorRGBA.Blue);
		c4.getGeom().move(18.4f,-7,-18f); 
                // ----------- Cube 5 ---------
		CubeObstacle c5 = new CubeObstacle(0.6f, 0.6f, 2f, ColorRGBA.Blue);
		c5.getGeom().move(2.5f,-7,-14.6f); 
                // ----------- Cube 6 ---------
		CubeObstacle c6 = new CubeObstacle(0.6f, 0.6f, 2f, ColorRGBA.Blue);
		c6.getGeom().move(6.5f,-7,-14.6f); 
                // ----------- Cube 7 ---------
		CubeObstacle c7 = new CubeObstacle(4f, 0.6f, 0.6f, ColorRGBA.Blue);
		c7.getGeom().move(-10f,-7,-13.2f); 
                // ----------- Cube 8 ---------
		CubeObstacle c8 = new CubeObstacle(2f, 0.6f, 0.6f, ColorRGBA.Blue);
		c8.getGeom().move(0f,-7,-13.2f); 
                // ----------- Cube 9 ---------
		CubeObstacle c9 = new CubeObstacle(8f, 0.6f, 0.6f, ColorRGBA.Blue);
		c9.getGeom().move(14f,-7,-13.2f); 
                // ----------- Cube 10 ---------
		CubeObstacle c10 = new CubeObstacle(4f, 0.6f, 0.6f, ColorRGBA.Blue);
		c10.getGeom().move(-14f,-7,-10f); 
                // ----------- Cube 11 ---------
		CubeObstacle c11 = new CubeObstacle(4f, 0.6f, 0.6f, ColorRGBA.Blue);
		c11.getGeom().move(6f,-7,-10f); 
                // ----------- Cube 12 ---------
		CubeObstacle c12 = new CubeObstacle(4f, 0.6f, 0.6f, ColorRGBA.Blue);
		c12.getGeom().move(-14f,-7,-6f); 
                // ----------- Cube 13 ---------
		CubeObstacle c13 = new CubeObstacle(4f, 0.6f, 0.6f, ColorRGBA.Blue);
		c13.getGeom().move(2f,-7,-6f); 
                // ----------- Cube 14 ---------
		CubeObstacle c14 = new CubeObstacle(4f, 0.6f, 0.6f, ColorRGBA.Blue);
		c14.getGeom().move(-2f,-7,-2f); 
                // ----------- Cube 15 ---------
		CubeObstacle c15 = new CubeObstacle(6f, 0.6f, 0.6f, ColorRGBA.Blue);
		c15.getGeom().move(12f,-7,-2f); 
                // ----------- Cube 16 ---------
		CubeObstacle c16 = new CubeObstacle(6f, 0.6f, 0.6f, ColorRGBA.Blue);
		c16.getGeom().move(16f,-7,2f); 
                // ----------- Cube 17 ---------
		CubeObstacle c17 = new CubeObstacle(10f, 0.6f, 0.6f, ColorRGBA.Blue);
		c17.getGeom().move(12f,-7,6f); 
                // ----------- Cube 18 ---------
		CubeObstacle c18 = new CubeObstacle(10f, 0.6f, 0.6f, ColorRGBA.Blue);
		c18.getGeom().move(8f,-7,10f); 
                // ----------- Cube 19 ---------
		CubeObstacle c19 = new CubeObstacle(4f, 0.6f, 0.6f, ColorRGBA.Blue);
		c19.getGeom().move(-10f,-7,10); 
                // ----------- Cube 20 ---------
		CubeObstacle c20 = new CubeObstacle(8f, 0.6f, 0.6f, ColorRGBA.Blue);
		c20.getGeom().move(-10f,-7,14); 
                // ----------- Cube 21 ---------
		CubeObstacle c21 = new CubeObstacle(2f, 0.6f, 0.6f, ColorRGBA.Blue);
		c21.getGeom().move(8f,-7,14); 
                // ----------- Cube 22 ---------
		CubeObstacle c22 = new CubeObstacle(2f, 0.6f, 0.6f, ColorRGBA.Blue);
		c22.getGeom().move(16f,-7,14); 
                // ----------- Cube 23 ---------
		CubeObstacle c23 = new CubeObstacle(0.6f, 0.6f, 6.5f, ColorRGBA.Blue);
		c23.getGeom().move(-1.5f,-7,8f); 
                // ----------- Cube 24 ---------
		CubeObstacle c24 = new CubeObstacle(0.6f, 0.6f, 12f, ColorRGBA.Blue);
		c24.getGeom().move(-6f,-7,-1.5f); 
                // ----------- Cube 25 ---------
		CubeObstacle c25 = new CubeObstacle(0.6f, 0.6f, 6.5f, ColorRGBA.Blue);
		c25.getGeom().move(-10f,-7,0f); 
                // ----------- Cube 26 ---------
		CubeObstacle c26 = new CubeObstacle(0.6f, 0.6f, 6.5f, ColorRGBA.Blue);
		c26.getGeom().move(-14f,-7,4f); 
                // ----------- Cube 27 ---------
		CubeObstacle c27 = new CubeObstacle(0.6f, 0.6f, 4.5f, ColorRGBA.Blue);
		c27.getGeom().move(6f,-7,-2f); 
                // ----------- Cube 28 ---------
		CubeObstacle c28 = new CubeObstacle(0.6f, 0.6f, 3.5f, ColorRGBA.Blue);
		c28.getGeom().move(10f,-7,-10f); 
                // ----------- Cube 29 ---------
		CubeObstacle c29 = new CubeObstacle(0.6f, 0.6f, 3.5f, ColorRGBA.Blue);
		c29.getGeom().move(14f,-7,-6f); 
                // ----------- Cube 30 ---------
		CubeObstacle c30 = new CubeObstacle(0.6f, 0.6f, 4f, ColorRGBA.Blue);
		c30.getGeom().move(18f,-7,-5.5f); 
                // ----------- Cube 31 ---------
		CubeObstacle c31 = new CubeObstacle(0.6f, 0.6f, 4.5f, ColorRGBA.Blue);
		c31.getGeom().move(2,-7,2f); 
                // ----------- Cube 32 ---------
		CubeObstacle c32 = new CubeObstacle(0.6f, 0.6f, 3.2f, ColorRGBA.Blue);
		c32.getGeom().move(10f,-7,16.8f); 
                // ----------- Cube 33 ---------
		CubeObstacle c33 = new CubeObstacle(0.6f, 0.6f, 3.2f, ColorRGBA.Blue);
		c33.getGeom().move(2f,-7,16.8f); 
		// ----------- Add Spheres to the group ---------
		cubeGroup.addCube(c1);
		cubeGroup.addCube(c2);
		cubeGroup.addCube(c3);
		cubeGroup.addCube(c4);
                cubeGroup.addCube(c5);
                cubeGroup.addCube(c6);
                
                cubeGroup.addCube(c7);
                cubeGroup.addCube(c8);
                cubeGroup.addCube(c9);
                
                cubeGroup.addCube(c10);
                cubeGroup.addCube(c11);
                
                cubeGroup.addCube(c12);
                cubeGroup.addCube(c13);
                
                cubeGroup.addCube(c14);
                cubeGroup.addCube(c15);
                
                cubeGroup.addCube(c16);
                
                cubeGroup.addCube(c17);
                
                cubeGroup.addCube(c18);
                cubeGroup.addCube(c19);
                
                cubeGroup.addCube(c20);
                cubeGroup.addCube(c21);
                cubeGroup.addCube(c22);
                
                cubeGroup.addCube(c23);
                cubeGroup.addCube(c24);
                cubeGroup.addCube(c25);
                cubeGroup.addCube(c26);
                cubeGroup.addCube(c27);
                cubeGroup.addCube(c28);
                cubeGroup.addCube(c29);
                cubeGroup.addCube(c30);
                cubeGroup.addCube(c31);
                cubeGroup.addCube(c32);
                cubeGroup.addCube(c33);
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
        geom1.move(19, 5f, -19);
        
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
        finishFlag.move(19, -7, 19);
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
                
                app = new Level9();
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
				player.setPhysicsLocation(new Vector3f(19, 5f, -19));
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
        if("Terrain".equals(event.getNodeA().getName()) && "Box".equals(event.getNodeB().getName())) {
            touchTerrain = true;
        }
        
        if("TerrainFloor".equals(event.getNodeA().getName()) || "TerrainFloor".equals(event.getNodeB().getName())) 
            finish();
        
        if("Box".equals(event.getNodeA().getName()) && "Finish".equals(event.getNodeB().getName()))
            finish = true;
    
    }

    private void finish() {
            player.setPhysicsLocation(new Vector3f(19, 5f, -19));
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
