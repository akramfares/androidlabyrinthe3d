package androidlabyrinthe3d;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

public class SphereObstacle{
	private Sphere mesh;
	private Geometry geom;
	private Material mat;
	private static AssetManager assetManager;
	
	SphereObstacle(int x,int y,float z, ColorRGBA color){
		mesh = new Sphere(x, y, z);
		geom=new Geometry("Sphere",mesh);
		geom.updateModelBound();
		 
		mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		geom.setMaterial(mat);
	}
	
	public static void setAsset(AssetManager a) {
		assetManager = a;
	}
	
	public Geometry getGeom(){ 
		return geom; 
	}
	
	
}
