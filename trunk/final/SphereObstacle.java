package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

public class SphereObstacle{
	private static Box mesh;
	private Geometry geom;
	private static Material mat;
        private float v;
	private static AssetManager assetManager;
	
	SphereObstacle(){
		geom=new Geometry("Sphere",mesh);
		geom.setMaterial(mat);
	}
	
	public static void init(int x,int y,float z, AssetManager a) {
		assetManager = a;
                mesh = new Box(0.3f, 0.3f, 0.3f);
                //mesh.setTextureMode(Sphere.TextureMode.Projected);
                mesh.setBound(new BoundingSphere());
                mesh.updateBound();
                mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
                Texture tex_ml =assetManager.loadTexture("Textures/magma_sphere.jpg");
                mat.setTexture("ColorMap", tex_ml);
	}
	
	public Geometry getGeom(){ 
		return geom; 
	}
        
        public void setV(float vs){
            v = vs;
        }
        
        public float getV(){
            return v;
        }
	
	
}
