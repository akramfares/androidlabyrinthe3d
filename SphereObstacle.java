package androidlabyrinthe3d;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

public class SphereObstacle {
	private Sphere mesh;
	private Geometry geom;
	private Material mat;
	private static Node spheres = new Node("Spheres");
	private static AssetManager assetManager;
	SphereObstacle(int x,int y,float z, ColorRGBA color){
		mesh = new Sphere(x, y, z);
		geom=new Geometry("Sphere",mesh);
		geom.updateModelBound();
		 
		mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		geom.setMaterial(mat);
		spheres.attachChild(geom);
	}
	

	public Geometry getGeom(){ 
		return geom; 
	}
	
	
	public static Node getSpheres() {
		return spheres;
	}

	public static void setAsset(AssetManager a) {
		assetManager = a;
	}
	
	/*
	 params : Spatial (Geometry)
	 return : true if collision, else return false 
	 */
	public static boolean collideWith(Spatial s){
		CollisionResults results = new CollisionResults();
    	BoundingVolume bv = s.getWorldBound();
    	spheres.collideWith(bv, results);
    	 if (results.size() > 0) return true;
    	 else return false;
	}
}
