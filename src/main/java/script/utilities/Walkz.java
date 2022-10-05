package script.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Map;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;

import script.p;
public class Walkz {
	public static boolean walkToArea(Area area,Tile walkableTile)
	{
		if(area.contains(p.l)) return true;
		if(!Walking.isRunEnabled() &&
				Walking.getRunEnergy() > Sleepz.calculate(15, 20)) 
		{
			Walking.toggleRun();
		}
		if(Walking.shouldWalk())
		{
			Walking.walk(walkableTile);
		}
		Sleepz.sleep(666, 1111);
		return area.contains(p.l);
	}
	public static boolean walkToTileInRadius(Tile walkableTile,int radius)
	{
		Area area = walkableTile.getArea(radius);
		
		if(area.contains(p.l)) return true;
		if(!Walking.isRunEnabled() &&
				Walking.getRunEnergy() > Sleepz.calculate(15, 20)) 
		{
			Walking.toggleRun();
		}
		if(Walking.shouldWalk())
		{
			Walking.walk(walkableTile);
		}
		
		Sleepz.sleep(666, 1111);
		return area.contains(p.l);
	}
	public static boolean walkPath(Tile[] path, boolean backwards)
	{
		List<Tile> pathTiles = new ArrayList<Tile>();
		for(Tile t : path)
		{
			pathTiles.add(t);
		}
		if(!backwards) Collections.reverse(pathTiles);
		
		for(Tile t : pathTiles)
		{
			if(Map.isTileOnMap(t))
			{
				if(Walking.shouldWalk(6))
				{
					if(Walking.walk(t))
					{
						Logger.log("Walked on path(regular walk)!");
						Sleepz.sleep(696,420);
					}
					else if(Walking.clickTileOnMinimap(t))
					{
						Logger.log("Walked on path (map)!");
						Sleepz.sleep(696,420);
					}
					else if(Map.interact(t,"Walk here"))
					{
						Logger.log("Walked here on path (screen)!");
						Sleepz.sleep(696,420);
					}
					else Logger.log("Missed path walk on valid tile");
				}
				return true;
			}
		}
		return false;
	}
}
