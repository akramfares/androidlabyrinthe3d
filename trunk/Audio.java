package androidlabyrinthe3d;
 
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;

import com.jme3.audio.AudioNode;

/** Sample 11 - playing 3D audio. */
public class Audio  {
 
  private AudioNode audio;
 private static  AssetManager assetManager;

  private void Audio() {
	  
		audio = new AudioNode(assetManager, "Sound/Environment/Nature.ogg", false);
	    audio.setLooping(true);  // activate continuous playing
	    audio.setPositional(true);
	    audio.setVolume(45);
	   audio.play();
  }
  public static void setAsset(AssetManager a) {
		assetManager = a;
	}
	
	public AudioNode getAudioNode(){ 
		return audio; 
	}
 
 
}