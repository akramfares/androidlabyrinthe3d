package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

public class Terrain {
	private Box mesh;
	private Geometry geom;
	private Material mat;
	private static Node terrain = new Node("Terrain");
	private static RigidBodyControl control;
	private static BulletAppState bulletAppState;
	private static AssetManager assetManager;
        private float mX, mY, mZ;
        private static String textureHaut;
        private static String textureBas;
	Terrain(float x,float y,float z, ColorRGBA color){
		mX = x;
                mY = y;
                mZ = z;
		mesh=new Box(Vector3f.ZERO, x, y, z);
		geom=new Geometry("Terrain",mesh);
		
		//if(color == ColorRGBA.White){
		    mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		    Texture tex_ml =assetManager.loadTexture("Textures/"+textureHaut+".jpg");
		    tex_ml.setWrap(Texture.WrapMode.Repeat);
		    mat.setTexture("ColorMap", tex_ml);
		    mesh.scaleTextureCoordinates(new Vector2f(z/2,x/2));
		/*}
		else{
			mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
	        mat.setColor("Color", color);	
		}*/
		
		geom.setMaterial(mat);
		terrain.attachChild(geom);
	}
	
        public void setGround(){
            Box gmesh = new Box(Vector3f.ZERO, mX, mY, mZ);
            gmesh.scaleTextureCoordinates(new Vector2f(0.1f,0.1f));
            Geometry ggeom=new Geometry("Terrain",gmesh);
            Material gmat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            Texture tex_ml =assetManager.loadTexture("Textures/"+textureBas+".jpg");
            tex_ml.setWrap(Texture.WrapMode.Repeat);
            gmat.setTexture("ColorMap", tex_ml);
            ggeom.setMaterial(gmat);
            Vector3f v = geom.getLocalTranslation();
            ggeom.move(v.x, v.y-mY*2, v.z);
            terrain.attachChild(ggeom);
        }
        
	public Geometry getGeom(){ 
		return geom; 
	}
	
	public RigidBodyControl getControl(){ 
		return control; 
	}
	
	public static void setCollision(){
		CollisionShape sceneShape =
			    CollisionShapeFactory.createDynamicMeshShape(terrain);
                
		control = new RigidBodyControl(sceneShape, 0);
		terrain.addControl(control);
		
		bulletAppState.getPhysicsSpace().add(control);
	}

	public static Node getTerrain() {
		return terrain;
	}

	public static void setState(BulletAppState b) {
		bulletAppState = b;
	}
	public static void setAsset(AssetManager a) {
		assetManager = a;
	}
        
        
        public static void setTextureHaut(String tex){
            textureHaut = tex;
        }
        public static void setTextureBas(String tex){
            textureBas = tex;
        }
}
