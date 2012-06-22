package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;


public class CubeObstacle{
	private Box mesh;
	private Geometry geom;
	private Material mat;
	private static AssetManager assetManager;
	
	CubeObstacle(float x,float y,float z, ColorRGBA color){
		mesh = new Box(x, y, z);
		geom=new Geometry("Sphere",mesh);
		 
		 mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
                Texture tex_ml =assetManager.loadTexture("Textures/ground.jpg");
                mat.setTexture("ColorMap", tex_ml);
		geom.setMaterial(mat);
	}
	
	public static void setAsset(AssetManager a) {
		assetManager = a;
	}
	
	public Geometry getGeom(){ 
		return geom; 
	}
	
	
}

