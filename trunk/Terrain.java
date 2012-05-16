package androidlabyrinthe3d;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class Terrain {
	private Box mesh;
	private Geometry geom;
	private Material mat;
	private static Node terrain = new Node("Terrain");
	private static RigidBodyControl control;
	private static BulletAppState bulletAppState;
	private static AssetManager assetManager;
	Terrain(float x,float y,float z, ColorRGBA color){
		
		mesh=new Box(Vector3f.ZERO, x, y, z);
		geom=new Geometry("Terrain",mesh);
		geom.updateModelBound();
		 
		mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");  // create a simple material
        mat.setBoolean("UseMaterialColors",true); 
        mat.setColor("Diffuse", color);
		geom.setMaterial(mat);
		terrain.attachChild(geom);
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
}
