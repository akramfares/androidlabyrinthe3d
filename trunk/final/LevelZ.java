/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;


import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.effect.ParticleEmitter;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.TouchInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.TouchListener;
import com.jme3.input.controls.TouchTrigger;
import com.jme3.input.event.TouchEvent;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import java.util.ArrayList;

public class LevelZ extends SimpleApplication implements ActionListener,PhysicsCollisionListener{
	private static LevelZ app;

	private CameraNode camNode;
	private BasicShadowRenderer bsr;
	private BulletAppState bulletAppState;
	private SphereGroup sphereGroup;
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
        private BitmapText txt;
        protected  ParticleEmitter debris ;
        private Spatial s;
        private ArrayList<Weapon> weapons = new ArrayList<Weapon>();
        private Terrain t1;
        private RigidBodyControl obstacle1control;
        private RigidBodyControl wcontrol;
        private Node weaponNode;
        private boolean touchTerrain=false;
        private int nbrObstacle=0;
        private Node weaponDebrisNode;
        private int score;
        private int highscore=0;
        private BitmapText txtHS;
	
	@Override
	public void simpleInitApp() {
		setDisplayFps(false);
                setDisplayStatView(false);
		
		
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		//bulletAppState.getPhysicsSpace().enableDebug(assetManager);
                bulletAppState.getPhysicsSpace().addCollisionListener(this);
		cam.setLocation(new Vector3f(10,20,10));
                
                BitmapFont fnt = assetManager.loadFont("Interface/Fonts/Default.fnt");
                txt = new BitmapText(fnt, false);
                txt.setBox(new Rectangle(0, 0, settings.getWidth(), settings.getHeight()));
                txt.setSize(fnt.getPreferredSize() * 2f);
                txt.setText("Score : "+Integer.toString(score));
                txt.setLocalTranslation(0, txt.getHeight()-40, 0);
                guiNode.attachChild(txt);
                
                txtHS = new BitmapText(fnt, false);
                txtHS.setBox(new Rectangle(0, 0, settings.getWidth(), settings.getHeight()));
                txtHS.setSize(fnt.getPreferredSize() * 2f);
                txtHS.setText("High Score : "+highscore);
                txtHS.setLocalTranslation(0, txtHS.getHeight(), 0);
                guiNode.attachChild(txtHS);
		
		
        playerNode = new Node("Player");
        rootNode.attachChild(playerNode);
        weaponNode = new Node("Weapon");
        rootNode.attachChild(weaponNode);
        weaponDebrisNode = new Node("WeaponDebris");
        rootNode.attachChild(weaponDebrisNode);
        
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
        Terrain.setTextureHaut("ice");
        setTextureFloor("magma_sphere");
        // --------------- Terrain 1 3x6 ----------------
        t1 = new Terrain(20f, 0.5f, 20f, ColorRGBA.Green);
        t1.getGeom().move(0f,-8,0f);
        // --------------- Terrain Collision -----------
        Terrain.setCollision();
        // --------- Spheres Configuration -------------
        SphereObstacle.init(32, 32, 0.5f,assetManager);
		sphereGroup = new SphereGroup();
        spheres = sphereGroup.getNode();
        rootNode.attachChild(spheres);
        spheres.setLocalTranslation(new Vector3f(0,0,0));
        // ------------------- Sphere ------------------
        

        // ---------------- Player -----------------------
        Box b1=new Box(Vector3f.ZERO, 0.6f, 0.6f, 0.6f);
        b1.setBound(new BoundingBox());
        b1.updateBound();
        geom1 = new Geometry("Box", b1);
        Material matp = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex_ml =assetManager.loadTexture("Textures/player.png");
        matp.setTexture("ColorMap", tex_ml);
        geom1.setMaterial(matp);                   // set the cube's material
        geom1.move(0, 25f, 0);
        
        BoxCollisionShape playerShape = new BoxCollisionShape(new Vector3f(0.6f, 0.6f, 0.6f));
        player = new RigidBodyControl(playerShape, 3);
        player.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
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
         // --------------- Weapons configuration --------------
         Weapon.setAsset(assetManager);
         
		
    	setUpKeys();


	}
	
	private void setUpKeys() {
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
                inputManager.addMapping("Mouse", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
                inputManager.addMapping("tap", new TouchTrigger(TouchInput.ALL));
		inputManager.addListener(this, "Left");
		inputManager.addListener(this, "Right");
		inputManager.addListener(this, "Up");
		inputManager.addListener(this, "Down");
		inputManager.addListener(this, "Jump");
                inputManager.addListener(this, "Mouse");
                inputManager.addListener(new TouchListener() {
 
                     public void onTouch(String name, TouchEvent event, float tpf) {
                         if (event.getType() == TouchEvent.Type.DOWN) {
                             ParticleEmitter wdebris;
                             Vector3f origin    = cam.getWorldCoordinates(new Vector2f(event.getX(),event.getY()), 0.0f);
                             Vector3f direction = cam.getWorldCoordinates(new Vector2f(event.getX(),event.getY()), 0.3f);
                            direction.subtractLocal(origin).normalizeLocal();

                            Ray ray = new Ray(origin, direction);
                            // Left
                            CollisionResults fResults = new CollisionResults();
                            t1.getGeom().collideWith(ray, fResults);
                            if(fResults.size()>0 && weaponNode.getChildren().isEmpty()) {
                                // Ajouter un Weapon
                                Weapon w = new Weapon(32, 32, 0.2f, ColorRGBA.Black);
                                w.getGeom().move(geom1.getLocalTranslation());
                                w.setV(20f);
                                wdebris = debris.clone();
                                w.setDebris(wdebris);
                                weaponDebrisNode.attachChild(wdebris);
                                w.setDirection(new Vector2f(fResults.getClosestCollision().getContactPoint().x,fResults.getClosestCollision().getContactPoint().z));
                                weaponNode.attachChild(w.getGeom());
                                weapons.add(w);
                                BoxCollisionShape wShape =
                                new BoxCollisionShape(new Vector3f(0.2f,0.2f,0.2f));
                                wcontrol = new RigidBodyControl(wShape, 0.1f);
                                wcontrol.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
                                w.getGeom().addControl(wcontrol);
                                wcontrol.setLinearVelocity(new Vector3f(fResults.getClosestCollision().getContactPoint().x,1,fResults.getClosestCollision().getContactPoint().z));
                                double angle = Math.atan2(w.getDirection().y-geom1.getLocalTranslation().z, w.getDirection().x-geom1.getLocalTranslation().x);
                                float vAX = (float) (w.getV()*Math.cos(angle));
                                float vAY = (float) (w.getV()*Math.sin(angle));
                                wcontrol.setLinearVelocity(new Vector3f(vAX, 2f, vAY));
                                bulletAppState.getPhysicsSpace().add(wcontrol);
                            }
                          }
                         if (event.getType() == TouchEvent.Type.UP) {
                         }
                     }

                 },
                 "tap");
                
		}
	
        @Override
	public void simpleUpdate(float tpf) {
	
		movePlayer(tpf);
                moveObstacle(tpf);
                setObstacle();
                moveWeapon();
                debris.setLocalTranslation(geom1.getLocalTranslation());
                camNode.lookAt(geom1.getLocalTranslation(), Vector3f.UNIT_Y);
                camNode.setLocalTranslation(new Vector3f(geom1.getLocalTranslation().x, 15, geom1.getLocalTranslation().z+20));     	  
	}
        private void moveWeapon() {
            for(int i=0; i<weapons.size();i++){
                weapons.get(i).move();
            }
            
            if(weaponNode.getChildren().isEmpty()){
                weaponDebrisNode.detachAllChildren();
            }
            
            
        }
	private void moveObstacle(float tpf) {
            synchronized(spheres.getChildren()){
                for(int i=0; i<spheres.getChildren().size(); i++){
                    s = spheres.getChildren().get(i);
                    double angle = Math.atan2(geom1.getLocalTranslation().z-s.getLocalTranslation().z, geom1.getLocalTranslation().x-s.getLocalTranslation().x);
                    float vAX = (float) (3f*Math.cos(angle)*tpf);
                    float vAY = (float) (3f*Math.sin(angle)*tpf);
                    s.move(vAX, 0f, vAY);
                }
            }
	}
        
        private void setObstacle(){
            if(spheres.getChildren().isEmpty()){
                nbrObstacle++;
                for(int i=0; i<nbrObstacle; i++){
                    
                          int lower = -19;
                          int higher = 19;
                          int random = (int)(Math.random() * (higher-lower)) + lower;
                          SphereObstacle s1 = new SphereObstacle();
                          s1.getGeom().move(random,-7,-18); 
                          s1.setV(3f);
                          sphereGroup.addSphere(s1);
                          CollisionShape sphereShape =
			    CollisionShapeFactory.createDynamicMeshShape(s1.getGeom());
                          obstacle1control = new RigidBodyControl(sphereShape, 0);
                          obstacle1control.setKinematic(true);
                          s1.getGeom().addControl(obstacle1control);
                          bulletAppState.getPhysicsSpace().add(s1.getGeom());
                }
            }
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
          
          if(geom1.getLocalTranslation().y < -18){
                    finish();
                }
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
				player.setLinearVelocity(speed);
			}
		}
                else touchTerrain = false;
                
                if(geom1.getLocalTranslation().y < -18){
                    finish();
                }

	}
        
	public void setPlayerVelocity(float x, float y){
		vX = x;
		vY = y;
        }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
                AppSettings settings = new AppSettings(true);
                settings.setRenderer(AppSettings.LWJGL_OPENGL1);
                settings.setFrameRate(60);
                settings.setVSync(true);
                settings.setFrequency(60);
                
                app = new LevelZ();
                app.setSettings(settings);
                app.start(); // start the game

	}
        public void detachAll(){
            terrain.detachAllChildren();
            spheres.detachAllChildren();
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
                        else if (binding.equals("Mouse")) {
                            ParticleEmitter wdebris;
                             Vector3f origin    = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
                             Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
                            direction.subtractLocal(origin).normalizeLocal();

                            Ray ray = new Ray(origin, direction);
                            // Left
                            CollisionResults fResults = new CollisionResults();
                            t1.getGeom().collideWith(ray, fResults);
                            if(fResults.size()>0 && weaponNode.getChildren().isEmpty() && value) {
                                // Ajouter un Weapon
                                Weapon w = new Weapon(32, 32, 0.2f, ColorRGBA.Black);
                                w.getGeom().move(geom1.getLocalTranslation());
                                w.setV(20f);
                                wdebris = debris.clone();
                                w.setDebris(wdebris);
                                weaponDebrisNode.attachChild(wdebris);
                                w.setDirection(new Vector2f(fResults.getClosestCollision().getContactPoint().x,fResults.getClosestCollision().getContactPoint().z));
                                weaponNode.attachChild(w.getGeom());
                                weapons.add(w);
                                BoxCollisionShape wShape =
                                new BoxCollisionShape(new Vector3f(0.2f,0.2f,0.2f));
                                wcontrol = new RigidBodyControl(wShape, 0.1f);
                                wcontrol.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
                                w.getGeom().addControl(wcontrol);
                                wcontrol.setLinearVelocity(new Vector3f(fResults.getClosestCollision().getContactPoint().x,1,fResults.getClosestCollision().getContactPoint().z));
                                double angle = Math.atan2(w.getDirection().y-geom1.getLocalTranslation().z, w.getDirection().x-geom1.getLocalTranslation().x);
                                float vAX = (float) (w.getV()*Math.cos(angle));
                                float vAY = (float) (w.getV()*Math.sin(angle));
                                wcontrol.setLinearVelocity(new Vector3f(vAX, 2f, vAY));
                                bulletAppState.getPhysicsSpace().add(wcontrol);
                            }
                             
                            
                        }
                         
		
	}

    public void collision(PhysicsCollisionEvent event) {
        if("Sphere".equals(event.getNodeA().getName()) && "Weapon".equals(event.getNodeB().getName())){
           
           score +=10;
           txt.setText("Score : "+Integer.toString(score)+"    "+"\n");
           event.getNodeA().removeFromParent();
           bulletAppState.getPhysicsSpace().remove(event.getObjectA());
           event.getNodeB().removeFromParent();
           bulletAppState.getPhysicsSpace().remove(event.getObjectB());
        }
        
        if("Terrain".equals(event.getNodeA().getName()) && "Box".equals(event.getNodeB().getName())) 
            touchTerrain = true;
        
        if(("Terrain".equals(event.getNodeA().getName()) || "TerrainFloor".equals(event.getNodeA().getName())) && "Weapon".equals(event.getNodeB().getName())) {
            event.getNodeB().removeFromParent();
            bulletAppState.getPhysicsSpace().remove(event.getObjectB());
        }
        
        if("Sphere".equals(event.getNodeB().getName())) {
            finish();
            bulletAppState.getPhysicsSpace().remove(event.getNodeB());
        }
        
    }

    public void setHighScore(int hs){
        highscore = hs;
    }
    public int getHighScore(){
        return highscore;
    }
    private void finish() {
            for(int i=0;i<spheres.getChildren().size();i++)
               bulletAppState.getPhysicsSpace().remove(spheres.getChildren().get(i));
            if(highscore < score) highscore = score;
            score=0;
            txtHS.setText("High Score : "+highscore);
            txt.setText("                                          ");
            txt.setText("Score : "+Integer.toString(score)+"    "+"\n");
            nbrObstacle = 0;
            spheres.detachAllChildren();
            
            player.setPhysicsLocation(new Vector3f(0, 10f, 0));
            player.setPhysicsRotation(Matrix3f.ZERO);
            player.setLinearVelocity(Vector3f.ZERO);
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
