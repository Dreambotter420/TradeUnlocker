package script.unlock.skills.skills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankType;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Map;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.map.impl.CollisionMap;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Pathz;
import script.utilities.Sleep;
import script.utilities.Walkz;

public class Chop_n_Burn extends Leaf {

    @Override
    public boolean isValid() 
    {
    	return API.unlockMode == API.modes.CHOP;
    }
    
    private final static Area treeArea2 = new Area(
			new Tile(3033, 3520, 0),
			new Tile(2995, 3519, 0),
			new Tile(2998, 3535, 0),
			new Tile(3002, 3538, 0),
			new Tile(3006, 3544, 0),
			new Tile(3015, 3543, 0),
			new Tile(3019, 3545, 0),
			new Tile(3025, 3541, 0));
    private final static Area treeArea1 = new Area(
			new Tile(3134, 3232, 0),
			new Tile(3143, 3244, 0),
			new Tile(3141, 3259, 0),
			new Tile(3185, 3265, 0),
			new Tile(3207, 3248, 0),
			new Tile(3200, 3204, 0),
			new Tile(3159, 3207, 0),
			new Tile(3127, 3201, 0),
			new Tile(3118, 3215, 0),
			new Tile(3133, 3220, 0));
    private static Area treeArea = null;
    public static boolean announcedTasty = false;
    public static boolean startBurning = false;
    private static Tile randTreeTile = null;
    private static final Area endAreaTreeArea2 = new Area(3030, 3476, 3055, 3461);
    public static boolean onExit()
    {
    	if(API.onlyHaveItemNames("Tinderbox","Bronze axe","Iron axe","Steel axe","Mithril axe","Adamant axe","Rune axe"))
		{
			if(endAreaTreeArea2.contains(Players.localPlayer())) return true;
			if(Walkz.walkPath(Pathz.aroundIceMountainToTrees, true)) return false;
			if(!treeArea2.contains(Players.localPlayer())) return true;
		}
    	return false;
    }
    @Override
    public int onLoop() {
    	if(canHandleDialogues())
		{
    		MethodProvider.log("See dialogue");
			return Sleep.calculate(69, 69);
		}
    	if(DecisionLeaf.taskTimer.finished())
    	{
    		if(onExit())
    		{
    			MethodProvider.log("Finished Chop + Burn task!");
    			API.unlockMode = null;
    			return 10;
    		}
    	}
    	//step 0-1: announce MMM TASTY
    	if(!announcedTasty)
    	{
    		Keyboard.type("Mmmmmmmm tasty~");
    		if(treeArea1.contains(Players.localPlayer())) 
    		{
    			treeArea = treeArea1;
    			MethodProvider.log("Already in tree area 1!");
    		}
    		else if(treeArea2.contains(Players.localPlayer())) 
    		{
    			treeArea = treeArea2;
    			MethodProvider.log("Already in tree area 1!");
    		}
    		else if(Calculations.random(0,100) > 40) 
    		{
    			MethodProvider.log("Setting tree area 1");
    			treeArea = treeArea1;
    		}
    		else 
    		{
    			treeArea = treeArea2;
    			MethodProvider.log("Setting tree area 2");
    		}
    		announcedTasty = true;
    	}
    	if(Fish_n_Cook.getAxe() && Fish_n_Cook.getTinderbox())
    	{
    		if(!haveExtraStuff())
    		{
    			if(treeArea.contains(Players.localPlayer()))
        		{
        			randTreeTile = null;
        			if(startBurning)
            		{
            			burnStuff();
            			return Sleep.calculate(69,222);
            		}
            		if(Inventory.isFull())
            		{
            			startBurning = true;
            			return Sleep.calculate(111,1212);
            		}
                	chopWood();
                	return Sleep.calculate(222,420);
        		}
        		//walk to tree area, first walk path around mountain if that is our area
        		
        		if(treeArea == treeArea2)
        		{
        			if(Walkz.walkPath(Pathz.aroundIceMountainToTrees, false)) return Sleep.calculate(420,1111);
        			randTreeTile = Pathz.aroundIceMountainToTrees[0];
        		}
        		if(randTreeTile == null)
        		{
        			randTreeTile = Map.getWalkable(treeArea.getRandomTile());
        			return Sleep.calculate(111,111);
        		}
        		
        		if(Walking.shouldWalk(6) && Walking.walk(randTreeTile)) Sleep.sleep(420,696);
    		}
    	}
		return Sleep.calculate(111,111);
    }
    public static void chopWood()
    {
    	if(!Players.localPlayer().isAnimating()) Sleep.sleep(696,1111);
    	if(Players.localPlayer().isAnimating()) 
    	{
			Sleep.sleep(696,1111);
    		return;
    	}
    	MethodProvider.log("Beginning find / sort of trees");
    	Timer test = new Timer(10000);
    	List<GameObject> validTrees = GameObjects.all(g -> g!=null && treeArea.contains(g) && g.hasAction("Chop down") && 
    			g.distance() < 15 &&
    			((Skills.getRealLevel(Skill.WOODCUTTING) < 15 || Skills.getRealLevel(Skill.FIREMAKING) < 15) && (g.getName().equals("Dead tree") || g.getName().equals("Tree")) || 
    					((Skills.getRealLevel(Skill.WOODCUTTING) >= 15 && Skills.getRealLevel(Skill.FIREMAKING) >= 15) && g.getName().equals("Oak"))));
    	if(validTrees == null || validTrees.isEmpty()) return;
    	//time to go through all trees, find closest (walking dist) stand tile for each one, sort the list, and go chop the nearest tree
    	LinkedHashMap<GameObject,ObjectTile> treesNTiles = new LinkedHashMap<GameObject,ObjectTile>();
    	final Tile loc = Players.localPlayer().getTile();
    	for(GameObject tree : validTrees)
    	{
    		if(tree == null || !tree.exists()) continue;
    		//sort walking distance lowest to highest
    		List<Tile> surrounding = tree.getSurrounding();
    		List<ObjectTile> surroundingTreeTiles = new ArrayList<ObjectTile>();
    		for(Tile t : surrounding)
    		{
    			if(t == null ||
        				CollisionMap.isBlocked(Map.getFlag(t)) ||
        				!treeArea.contains(t)) continue;
    			ObjectTile tt = new ObjectTile(t,(int)t.walkingDistance(loc));
    			surroundingTreeTiles.add(tt);
    		}
    		if(surroundingTreeTiles.isEmpty()) continue;
    		Collections.sort(surroundingTreeTiles);
    		treesNTiles.put(tree, surroundingTreeTiles.get(0));
    	}
    	if(treesNTiles.isEmpty()) return;
    	List<Entry<GameObject, ObjectTile>> treesNTilesListOfMapEntries = new ArrayList<Entry<GameObject, ObjectTile>>(treesNTiles.entrySet());
    	Collections.sort(
    			treesNTilesListOfMapEntries,
                new Comparator<Entry<GameObject, ObjectTile> >() {
                    public int compare(
                        Entry<GameObject, ObjectTile> entry1,
                        Entry<GameObject, ObjectTile> entry2)
                    {
                        return entry1.getValue().getWalkingDistance() - entry2.getValue().getWalkingDistance();
                    }
                });
    	GameObject closestTree = treesNTilesListOfMapEntries.get(0).getKey();
    	Tile closestTreeTile = treesNTilesListOfMapEntries.get(0).getValue().getTile();
    	MethodProvider.log("Done sorting in "+test.elapsed()+"ms! Closest tree / walkable tile: " + closestTree.getTile().toString()+" / "+closestTreeTile.toString());
    	if(closestTree.isOnScreen())
    	{
    		if(closestTree.interact("Chop down"))
    		{
    			String name = closestTree.getName();
    			Main.customPaintText1 = "Chop down -> "+name;
    			MethodProvider.sleepUntil(() -> Dialogues.inDialogue() || closestTree == null || !closestTree.exists(), 
    					() -> Players.localPlayer().isAnimating() || Players.localPlayer().isMoving(),
    					Sleep.calculate(2222,2222),69);
    			Main.customPaintText1 = "Chop down -> "+name+" SleepUntil expired";
    		}
    		return;
    	}
    	if(Walking.shouldWalk() && Walking.walk(closestTreeTile))
    	{
    		MethodProvider.sleepUntil(() -> closestTree.isOnScreen(), Sleep.calculate(1111,1111));
    	}
    }
    public static boolean haveExtraStuff()
    {
    	if(API.onlyHaveItemNames("Tinderbox","Bronze axe","Iron axe","Steel axe","Mithril axe","Adamant axe","Rune axe"))
		{
			startBurning = false;
			Sleep.sleep(111,1212);
    		return false;
		}
    	if(!API.onlyHaveItemNames("Tinderbox","Bronze axe","Iron axe","Steel axe","Mithril axe","Adamant axe","Rune axe",
    			"Oak logs","Logs"))
    	{
    		//deposit everything except woodcutting supplies
    		if(API.walkOpenBank())
    		{
    			if(Bank.depositAllExcept("Tinderbox","Bronze axe","Iron axe","Steel axe","Mithril axe","Adamant axe","Rune axe",
    					"Oak logs","Logs"))
    			{
    				MethodProvider.log("Deposited all items except woodcutting supplies!");
    				Main.customPaintText1 = "Deposited all items except woodcutting supplies!";
    				Bank.close();
    			}
    		}
    		Sleep.sleep(420,696);
    		return true;
    	}
    	return false;
    }
    public static boolean moveForFire = false;
    public static void burnStuff()
    {
    	
		if(!Tabs.isOpen(Tab.INVENTORY))
		{
			Tabs.open(Tab.INVENTORY);
			return;
		}
		if(!Players.localPlayer().isAnimating() && 
    			!Players.localPlayer().isMoving()) Sleep.sleep(222,425);
		if(Players.localPlayer().isAnimating() || 
    			Players.localPlayer().isMoving()) 
    	{
			Sleep.sleep(222,425);
    		return;
    	}
		//can start a fire here or walk
		Filter<GameObject> blockedBelowFilter = g -> g!=null &&
				g.getTile().equals(Players.localPlayer().getTile()) &&
				(g.getName().equals("Fern") || 
						g.getName().equals("Fire") || 
						g.getName().equals("Daisies") ||
						g.getName().equals("Flower") ||  
						g.getName().equals("Thistle") ||  
						g.getName().equals("Small fern") ||  
						g.getName().equals("Dock leaf plant") ||  
						g.getName().equals("Stones"));
		GameObject blockedBelow = GameObjects.closest(blockedBelowFilter);
		//walk another spot
		if(blockedBelow != null || moveForFire)
		{
			Main.customPaintText1 = "Walk to another burn tile";
			walkToAnotherBurnTile();
			moveForFire = false;
			return;
		}
		//start a fire
		Item tinderbox = Inventory.get("Tinderbox");
		Item logs = null;
		if(Inventory.contains("Logs")) logs = Inventory.get("Logs");
		if(Skills.getRealLevel(Skill.FIREMAKING) >= 15 && Inventory.contains("Oak logs")) logs = Inventory.get("Oak logs");
		if(tinderbox == null || logs == null)
		{
			MethodProvider.log("Tinderbox or logs null in firemaking function!");
			return;
		}
		final Tile burnTile = Players.localPlayer().getTile();
		final String logName = logs.getName();
		Filter<Item> logFilter = l -> l != null && l.getName().equals(logName);
		double leastDist = -1;
		int slotLeastDist = -1;
		for(Item i : Inventory.all(logFilter))
		{
			if(i == null)continue;
			final double currentDist = i.getDestination().getCenterPoint().distance(Mouse.getPosition());
			if(leastDist == -1) 
			{
				leastDist = currentDist;
				slotLeastDist = i.getSlot();
				continue;
			}
			if(currentDist < leastDist)
			{
				leastDist = currentDist;
				slotLeastDist = i.getSlot();
			}
		}
		
		final double mouseDistToTinderbox = tinderbox.getDestination().getCenterPoint().distance(Mouse.getPosition());
		logs = Inventory.getItemInSlot(slotLeastDist);
		if(leastDist < mouseDistToTinderbox)
		{
			if(logs.useOn(tinderbox))
			{
				Main.customPaintText1 = "Use logs -> tinderbox";
				MethodProvider.sleepUntil(() -> !Players.localPlayer().getTile().equals(burnTile), () -> Players.localPlayer().isAnimating(),Sleep.calculate(2222, 2222),69);
				Main.customPaintText1 = "Use logs -> tinderbox SleepUntil expired";
			}
		}
		else 
		{
			if(tinderbox.useOn(logs))
			{
				Main.customPaintText1 = "Use tinderbox -> logs";
				MethodProvider.sleepUntil(() -> !Players.localPlayer().getTile().equals(burnTile), () -> Players.localPlayer().isAnimating(),Sleep.calculate(2222, 2222),69);
				Main.customPaintText1 = "Use tinderbox -> logs SleepUntil expired";
			}
		}
		Sleep.sleep(69, 222);
		return;
    }
    public static void walkToAnotherBurnTile()
    {
    	int radius = Calculations.random(1,7);
    	if(radius != 5) radius = (int) ((double) radius / 2);
    	if(radius < 1) radius = 1;
    	Area searchArea = Players.localPlayer().getTile().getArea(radius);
    	List<Tile> validTiles = new ArrayList<Tile>();
    	for(Tile t : searchArea.getTiles())
    	{
    		//check for both walking blockages and fire-starting blockages then add valid tiles to list, shuffle list
    		if(t == null ||
    				CollisionMap.isBlocked(Map.getFlag(t)) ||
    				Players.localPlayer().getTile().equals(t) ||
    				!treeArea.contains(t) ||
    				GameObjects.closest(g -> g!=null &&
    				g.getTile().equals(t) &&
    				(g.getName().equals("Fern") || 
    						g.getName().equals("Fire") || 
    						g.getName().equals("Daisies") ||
    						g.getName().equals("Flower") ||  
    						g.getName().equals("Thistle") ||  
    						g.getName().equals("Small fern") ||  
    						g.getName().equals("Dock leaf plant") ||  
    						g.getName().equals("Stones"))) != null) continue;
    		validTiles.add(t);
    	}
    	if(validTiles.isEmpty()) 
    	{
    		MethodProvider.log("Fire-startable tiles in radius "+radius+" around us not found! Searching again...");
    		return;
    	}
    	Collections.shuffle(validTiles);
    	if(Map.isTileOnScreen(validTiles.get(0)))
    	{
    		if(Walking.walkOnScreen(validTiles.get(0)))
    		{
    			MethodProvider.sleepUntil(() -> Players.localPlayer().getTile().equals(validTiles.get(0)), 
        				() -> Players.localPlayer().isMoving(),Sleep.calculate(2222, 2222),69);
    		}
    		return;
    	}
    	if(Walking.walk(validTiles.get(0)))
    	{
    		MethodProvider.sleepUntil(() -> Players.localPlayer().getTile().equals(validTiles.get(0)), 
    				() -> Players.localPlayer().isMoving(),Sleep.calculate(2222, 2222),69);
    	}
    }
    /**
     * returns true if any dialogues can be handled or have been handled
     * otherwise returns false
     * @return
     */
    public static boolean canHandleDialogues()
    {
    	if(Dialogues.isProcessing()) return true;
    	if(Dialogues.areOptionsAvailable())
    	{
    		if(Walking.clickTileOnMinimap(Players.localPlayer().getTile())) 
    		{
    			Sleep.sleep(696, 420);
    		}
    		return true;
    	}
    	if(Dialogues.canContinue())
		{
    		int sleep = Sleep.calculate(2222,2222);
			Keyboard.holdSpace(() -> !Dialogues.inDialogue() || Dialogues.areOptionsAvailable(), sleep);
			MethodProvider.sleepUntil(() -> !Dialogues.inDialogue() || Dialogues.areOptionsAvailable(), sleep);
			return true;
		}
    	return false;
    }
    
}
