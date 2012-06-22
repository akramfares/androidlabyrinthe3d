package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

public class Weapon{
	private Sphere mesh;
	private Geometry geom;
	private Material mat;
        private float v;
        private Vector2f direction;
	private static AssetManager assetManager;
        private ParticleEmitter debris;
	
	Weapon(int x,int y,float z, ColorRGBA color){
		mesh = new Sphere(x, y, z);
                mesh.setTextureMode(Sphere.TextureMode.Projected);
		geom=new Geometry("Weapon",mesh);
		mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
                mat.setColor("Color", color);	
		geom.setMaterial(mat);
	}
	
	public static void setAsset(AssetManager a) {
		assetManager = a;
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
	
	public void setDirection(Vector2f d){
            direction = d;
        }
        
        public Vector2f getDirection(){
            return direction;
        }

    void move() {
        debris.setLocalTranslation(geom.getLocalTranslation());
    }

    void setDebris(ParticleEmitter clone) {
        debris = clone;
    }

    ParticleEmitter getDebris() {
        return debris;
    }
}
