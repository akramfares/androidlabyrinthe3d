package androidlabyrinthe3d;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;


public class CubeObstacle{
	private Box mesh;
	private Geometry geom;
	private Material mat;
	private static AssetManager assetManager;
	
	CubeObstacle(float x,float y,float z, ColorRGBA color){
		mesh = new Box(x, y, z);
		geom=new Geometry("Sphere",mesh);
		geom.updateModelBound();
		 
		 mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");  // create a simple material
        mat.setBoolean("UseMaterialColors",true); 
        mat.setColor("Diffuse", color);
		geom.setMaterial(mat);
	}
	
	public static void setAsset(AssetManager a) {
		assetManager = a;
	}
	
	public Geometry getGeom(){ 
		return geom; 
	}
	
	
}

