package androidlabyrinthe3d;

import java.util.ArrayList;

import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class CubeGroup {
	
	ArrayList<CubeObstacle> cubes = new ArrayList<CubeObstacle>();
	private Node node = new Node("Cubes");
	
	CubeGroup(){
	}
	
	public void addCube(CubeObstacle c){
		cubes.add(c);
		node.attachChild(c.getGeom());
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
		node.collideWith(bv, results);
   	 	if (results.size() > 0) return true;
   	 	else return false;
	}

	public ArrayList<CubeObstacle> getCubes() {
		return cubes;
	}

	public void setCubes(ArrayList<CubeObstacle> cubes) {
		this.cubes = cubes;
	}
	
	
}
