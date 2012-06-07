package androidlabyrinthe3d;


import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

 
/** Sample 11 - how to create fire, water, and explosion effects. */
public class Effet  {
  private ParticleEmitter fire ;
  private  Material mat_red ;
 
  private static AssetManager assetManager ;
  
  Effet() {
 
     fire =new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
     fire.setImagesX(2); 
    fire.setImagesY(2); // 2x2 texture animation
    fire.setEndColor(  new ColorRGBA(1f, 0f, 0f, 1f));   // red
    fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
    fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
    fire.setStartSize(1.5f);
    fire.setEndSize(0.1f);
    fire.setGravity(0, 0, 0);
    fire.setLowLife(1f);
    fire.setHighLife(3f);
    fire.getParticleInfluencer().setVelocityVariation(0.3f);
  

  }
  public static void setAsset(AssetManager a) {
		assetManager = a;
	}
	
	public ParticleEmitter getParticleEmitter(){ 
		return fire; 
	}
}