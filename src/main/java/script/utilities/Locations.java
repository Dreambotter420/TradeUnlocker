package script.utilities;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;

public class Locations {
	public static Area chosenCHICKENS;
	public static Tile chosenCHICKENS_WALKABLE;
	
	public static void chooseLocations()
	{
		int rand = API.rand2.nextInt(1000);
		if(rand < 162)
		{
			chosenCHICKENS = CHICKEN_COOP_INSIDE;
			chosenCHICKENS_WALKABLE = CHICKEN_COOP_WALKABLE;
		}
		else 
		{
			chosenCHICKENS = LARGE_CHICKEN_COOP_ATTACKABLE;
			chosenCHICKENS_WALKABLE = LARGE_CHICKEN_COOP_WALKABLE;
		}
	}public static final Tile LARGE_COWPEN_WALKABLE = new Tile(3174, 3337, 0);
	public static final Area LARGE_COWPEN = new Area(
			new Tile(3153, 3318, 0),
			new Tile(3179, 3317, 0),
			new Tile(3180, 3314, 0),
			new Tile(3185, 3314, 0),
			new Tile(3192, 3308, 0),
			new Tile(3201, 3308, 0),
			new Tile(3207, 3319, 0),
			new Tile(3199, 3341, 0),
			new Tile(3155, 3351, 0),
			new Tile(3153, 3329, 0));
	public static final Tile BRONZE_AXE_CHICKENCOOP = new Tile(3232, 3296, 0);
	public static final Tile CHICKEN_COOP_WALKABLE = new Tile(3233, 3296, 0);
	public static final Area LARGE_CHICKEN_COOP_ATTACKABLE = new Area(
			new Tile(3173, 3307, 0),
			new Tile(3179, 3307, 0),
			new Tile(3180, 3304, 0),
			new Tile(3185, 3302, 0),
			new Tile(3186, 3296, 0),
			new Tile(3185, 3290, 0),
			new Tile(3184, 3289, 0),
			new Tile(3172, 3287, 0),
			new Tile(3169, 3289, 0),
			new Tile(3169, 3299, 0));
	public static final Tile LARGE_CHICKEN_COOP_WALKABLE = new Tile(3176, 3300, 0);
	public static final Area CHICKEN_COOP_INSIDE = new Area(
			new Tile(3236, 3297, 0),
			new Tile(3237, 3298, 0),
			new Tile(3237, 3300, 0),
			new Tile(3235, 3301, 0),
			new Tile(3225, 3301, 0),
			new Tile(3225, 3294, 0),
			new Tile(3230, 3295, 0),
			new Tile(3231, 3286, 0),
			new Tile(3236, 3287, 0),
			new Tile(3237, 3291, 0));
}
