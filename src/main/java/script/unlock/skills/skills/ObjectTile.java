package script.unlock.skills.skills;

import org.dreambot.api.methods.map.Tile;

public class ObjectTile implements Comparable<ObjectTile> {
	 
    Tile tile;
    int walkingDistance;
 
    // Class constructor
    ObjectTile(Tile tile, int walkingDistance)
    {
        this.tile = tile;
        this.walkingDistance = walkingDistance;
    }
    public int getWalkingDistance() { return walkingDistance; }
    public Tile getTile() { return tile; }
    
    // Overriding compareTo() method
    @Override public int compareTo(ObjectTile o)
    {
        if (this.walkingDistance > o.walkingDistance) {
 
            // if current object is greater,then return 1
            return 1;
        }
        else if (this.walkingDistance < o.walkingDistance) {
 
            // if current object is greater,then return -1
            return -1;
        }
        else {
            // if current object is equal to o,then return 0
            return 0;
        }
    }
}
