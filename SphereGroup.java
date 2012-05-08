package androidlabyrinthe3d;

import java.util.ArrayList;

import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class SphereGroup {
	
	ArrayList<SphereObstacle> spheres = new ArrayList<SphereObstacle>();
	private static Node node = new Node("Spheres");
	
	SphereGroup(){
	}
	
	public void addSphere(SphereObstacle s){
		spheres.add(s);
		node.attachChild(s.getGeom());
	}
	
	
	public Node getNode() {
		return node;
	}
	
	/*
	 params : Spatial (Geometry)
	 return : true if collision, else return false 
	 */
	public boolean collideWith(Spatial s){
		CollisionResults results = new CollisionResults();
   	BoundingVolume bv = s.getWorldBound();
   	s.collideWith(bv, results);
   	 if (results.size() > 0) return true;
   	 else return false;
	}
	
}
