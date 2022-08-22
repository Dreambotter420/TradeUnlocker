package script.unlock.skills.skills;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankMode;
import org.dreambot.api.methods.container.impl.bank.BankType;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Map;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.methods.widget.helpers.ItemProcessing;
import org.dreambot.api.methods.widget.helpers.Smithing;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.map.impl.CollisionMap;

import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.utilities.API;
import script.utilities.MissingAPI;
import script.utilities.Sleep;

public class Mine_n_Smelt_n_Smith extends Leaf {

    @Override
    public boolean isValid() 
    {
    	return API.unlockMode == API.modes.MINE;
    }
    private final static Area rimmingtonMine = new Area(
			new Tile(2980, 3227, 0),
			new Tile(2986, 3228, 0),
			new Tile(2989, 3234, 0),
			new Tile(2990, 3243, 0),
			new Tile(2983, 3251, 0),
			new Tile(2972, 3251, 0),
			new Tile(2965, 3243, 0),
			new Tile(2964, 3238, 0),
			new Tile(2971, 3230, 0));
    private final static Area doricsAnvils = new Area(2950, 3454, 2953, 3449, 0);
    private final static Area faladorForge = new Area(
			new Tile(2976, 3368, 0),
			new Tile(2976, 3372, 0),
			new Tile(2977, 3373, 0),
			new Tile(2977, 3375, 0),
			new Tile(2973, 3374, 0),
			new Tile(2974, 3376, 0),
			new Tile(2969, 3376, 0),
			new Tile(2970, 3368, 0));
    public static boolean announcedTasty = false;
    public static final int copperOre = 436;
    public static final int tinOre = 438;
    public static final int clay = 434;
    public static final int hammer = 2347;
    public static final int ironOre = 440;
    public static final int clueGeodeBeginner = 23442;
    public static final int bronzeBar = 2349;
    public static final int bronzeDagger = 1205;
    public static final int bronzeAxe = 1351;
    public static final int bronzeScimitar = 1321;
    public static final int bronzeWarhammer = 1337;
    public static final int bronzeBattleaxe = 1375;
    public static final int bronzeKiteshield = 1189;
    public static final int bronzePlatelegs = 1075;
    public static final int bronzePlatebody = 1117;
    public static final int ironBar = 2351;
    public static final int ironDagger = 1203;
    public static final int ironAxe = 1349;
    public static final int ironScimitar = 1323;
    public static final int ironWarhammer = 1335;
    public static final int ironBattleaxe = 1363;
    public static final int ironKiteshield = 1191;
    public static final int ironPlatelegs = 1067;
    public static final int ironPlatebody = 1115;
    

    public static final int crashedStarTier3 = 41227; // lvl 30 mining
    public static final int crashedStarTier2 = 41228; //lvl 20 mining
    public static final int crashedStarTier1 = 41229;	//lvl 10 mining
    
    public static List<Integer> smithedItems = new ArrayList<Integer>();
    public static void initialize()
    {
    	smithedItems.add(bronzeAxe);
    	smithedItems.add(bronzeScimitar);
    	smithedItems.add(bronzeWarhammer);
    	smithedItems.add(bronzePlatelegs);
    	smithedItems.add(bronzePlatebody);
    	smithedItems.add(ironDagger);
    	smithedItems.add(ironAxe);
    	smithedItems.add(ironScimitar);
    	smithedItems.add(ironWarhammer);
    	smithedItems.add(ironPlatelegs);
    	smithedItems.add(ironPlatebody);
    	
    }
    @Override
    public int onLoop() {
    	if(DecisionLeaf.taskTimer.finished())
    	{
    		if(Inventory.contains(hammer) || Inventory.isEmpty())
    		{
    			MethodProvider.log("Finished mining task!");
    			API.unlockMode = null;
    			return 10;
    		}
    		Main.customPaintText3 = "~~ Task timer done - finishing last inventory";
    	}
    	if(Widgets.getWidgetChild(153,16) != null && 
    			Widgets.getWidgetChild(153,16).isVisible())
    	{
    		Main.customPaintText3 = "";
    		if(Widgets.getWidgetChild(153,16).interact("Close")) Sleep.sleep(111, 1111);
    		return Sleep.calculate(111, 111);
    	}
    	if(canHandleDialogues())
		{
			return Sleep.calculate(69, 420);
		}
    	//step 0-1: announce MMM TASTY
    	if(!announcedTasty)
    	{
    		Keyboard.type("Mmmmmmmm tasty~");
    		initialize();
    		announcedTasty = true;
    	}
    	if(!API.checkedBank()) return Sleep.calculate(111, 1111);
    	if(getBestPickaxe() && getHammer())
    	{
    		//here we need to do doric's quest to use his anvils
    		if(!completedDoricsQuest())
    		{
    			Main.customPaintText2 = "~~ Doing dorics quest for his anvils ~~";
    			doDoricsQuest();
    			return Sleep.calculate(111, 111);
    		}
    		Main.customPaintText2 = "~~ Finished Doric\'s Quest! ~~";
    		//here we have invy full to start smelting into bars
    		if(Inventory.isFull() && Inventory.onlyContains("Copper ore","Tin ore","Iron ore","Hammer",
    				"Clue geode (beginner)","Uncut sapphire","Uncut emerald","Uncut ruby","Uncut diamond",
    				"Rune pickaxe","Adamant pickaxe","Mithril pickaxe","Steel pickaxe"))
    		{
    			smeltBars();
    			return Sleep.calculate(111,1111);
    		}
    		if(Inventory.onlyContains("Copper ore","Tin ore","Iron ore","Hammer",
    				"Clue geode (beginner)","Uncut sapphire","Uncut emerald","Uncut ruby","Uncut diamond",
    				"Rune pickaxe","Adamant pickaxe","Mithril pickaxe","Steel pickaxe"))
    		{
    			//continue to smelt where we have already started to smelt and inventory is not full anymore and no bars present (iron bars) by seeing if we are already close to forge area with forgable materials
    			if((Inventory.containsAll(copperOre,tinOre) || Inventory.contains(ironOre)) && 
    					faladorForge.getCenter().distance() < 25)
    			{
    				smeltBars();
        			return Sleep.calculate(111,1111);
    			}
    			mineOre(null);
    			return Sleep.calculate(111,1111);
        	}
    		//continue to smelt once where we have bars in our invy already from smithing and have forgable materials
        	if(Inventory.contains(bronzeBar,ironBar) && 
        			(Inventory.containsAll(copperOre,tinOre) || Inventory.contains(ironOre)))
        	{
        		smeltBars();
        		return Sleep.calculate(111, 1111);
        	}
        	if(Inventory.contains(bronzeBar,ironBar) && 
        			!Inventory.containsAll(copperOre,tinOre) &&
        			!Inventory.contains(ironOre))
        	{
        		smithBars();
        		return Sleep.calculate(111, 1111);
        	}
        	if(!Inventory.contains(ironBar,bronzeBar,ironOre) && 
        			(Inventory.count(copperOre) <= 0 || Inventory.count(tinOre) <= 0)) handleBanking();
    	}
		return Sleep.calculate(111,111);
    }
    public static void smithBars()
    {
    	Main.customPaintText1 = "~~ Smithing bars into stuff ~~";
    	final int smithing = Skills.getRealLevel(Skill.SMITHING);
		final int bronzeBars = Inventory.count(bronzeBar);
		final int ironBars = Inventory.count(ironBar);
		SmithItem smithItem = null;
		Metal metal = null;
		if(bronzeBars > 0)
		{
			metal = Metal.BRONZE;
			if(smithing >= 18)
			{
				if(bronzeBars >= 5) smithItem = SmithItem.PLATEBODY;
				else if(bronzeBars >= 3) smithItem = SmithItem.PLATELEGS;
				else if(bronzeBars >= 2) smithItem = SmithItem.SCIMITAR;
				else smithItem = SmithItem.AXE;
			}
			else if(smithing >= 16)
			{
				if(bronzeBars >= 3) smithItem = SmithItem.PLATELEGS;
				else if(bronzeBars >= 2) smithItem = SmithItem.SCIMITAR;
				else smithItem = SmithItem.AXE;
			}
			else if(smithing >= 12)
			{
				if(bronzeBars >= 3) smithItem = SmithItem.KITESHIELD;
				else if(bronzeBars >= 2) smithItem = SmithItem.SCIMITAR;
				else smithItem = SmithItem.AXE;
			}
			else if(smithing >= 10)
			{
				if(bronzeBars >= 3) smithItem = SmithItem.BATTLEAXE;
				else if(bronzeBars >= 2) smithItem = SmithItem.SCIMITAR;
				else smithItem = SmithItem.AXE;
			}
			else if(smithing >= 9)
			{
				if(bronzeBars >= 3) smithItem = SmithItem.WARHAMMER;
				else if(bronzeBars >= 2) smithItem = SmithItem.SCIMITAR;
				else smithItem = SmithItem.AXE;
			}
			else if(smithing >= 5)
			{
				if(bronzeBars >= 2) smithItem = SmithItem.SCIMITAR;
				else smithItem = SmithItem.AXE;
			}
			else smithItem = SmithItem.AXE;
		}
		else if(ironBars > 0)
		{
			metal = Metal.IRON;
			if(smithing >= 33)
			{
				if(ironBars >= 5) smithItem = SmithItem.PLATEBODY;
				else if(ironBars >= 3) smithItem = SmithItem.PLATELEGS;
				else if(ironBars >= 2) smithItem = SmithItem.SCIMITAR;
				else smithItem = SmithItem.AXE;
			}
			else if(smithing >= 31)
			{
				if(ironBars >= 3) smithItem = SmithItem.PLATELEGS;
				else if(ironBars >= 2) smithItem = SmithItem.SCIMITAR;
				else smithItem = SmithItem.AXE;
			}
			else if(smithing >= 27)
			{
				if(ironBars >= 3) smithItem = SmithItem.KITESHIELD;
				else if(ironBars >= 2) smithItem = SmithItem.SCIMITAR;
				else smithItem = SmithItem.AXE;
			}
			else if(smithing >= 25)
			{
				if(ironBars >= 3) smithItem = SmithItem.BATTLEAXE;
				else if(ironBars >= 2) smithItem = SmithItem.SCIMITAR;
				else smithItem = SmithItem.AXE;
			}
			else if(smithing >= 24)
			{
				if(ironBars >= 3) smithItem = SmithItem.WARHAMMER;
				else if(ironBars >= 2) smithItem = SmithItem.SCIMITAR;
				else smithItem = SmithItem.AXE;
			}
			else if(smithing >= 20)
			{
				if(ironBars >= 2) smithItem = SmithItem.SCIMITAR;
				else smithItem = SmithItem.AXE;
			}
			else if(smithing >= 16) smithItem = SmithItem.AXE;
			else if(smithing >= 15) smithItem = SmithItem.DAGGER;
			else 
			{
				MethodProvider.log("Something wrong - have iron bars in invy but not lvl 15 smithing! depositing invy...");
				handleBanking();
				return;
			}
		}
		else
		{
			MethodProvider.log("Seriously wrong script logic! Not have any iron/bronze bars but we thought we had some before this function");
			return;
		}
    	if(doricsAnvils.contains(Players.localPlayer()))
    	{
    		if(Players.localPlayer().isAnimating())
    		{
    			Sleep.sleep(111, 1111);
    			return;
    		}
    		if(Smithing.isOpen())
    		{
    			int barsRequired = 0;
				if(smithItem == SmithItem.AXE || smithItem == SmithItem.DAGGER) barsRequired = 1;
				if(smithItem == SmithItem.SCIMITAR) barsRequired = 2;
				if(smithItem == SmithItem.WARHAMMER || smithItem == SmithItem.BATTLEAXE || 
						smithItem == SmithItem.KITESHIELD || smithItem == SmithItem.PLATELEGS) barsRequired = 3;
				if(smithItem == SmithItem.PLATEBODY) barsRequired = 5;
				final int finalBarsRequired = barsRequired;
    			final int smithID = getSmithItemID(smithItem,metal);
    			if(isAutoSmithingItem(smithItem,metal))
    			{
    				Keyboard.type(" ");
    				MethodProvider.sleepUntil(() -> Dialogues.inDialogue() || 
    						Inventory.count(smithID) < finalBarsRequired,
    						() -> Players.localPlayer().isAnimating(), Sleep.calculate(2222, 2222),69);
    				return;
    			}
    			if(Smithing.makeAll(smithID))
    			{
    				MethodProvider.sleepUntil(() -> Dialogues.inDialogue() || 
    						Inventory.count(smithID) < finalBarsRequired,
    						() -> Players.localPlayer().isAnimating(), Sleep.calculate(2222, 2222),69);
    			}
    			Sleep.sleep(111, 111);
    			return;
    		}
    		GameObject anvil = GameObjects.closest(g -> g!=null && g.getName().equals("Anvil") && doricsAnvils.contains(g));
    		if(anvil != null)
    		{
    			if(anvil.canReach())
    			{
    				if(anvil.interact("Smith"))
        			{
    					if(isAutoSmithingItem(smithItem,metal))
    					{
    						final int timeout = Sleep.calculate(3333, 3333);
    						Keyboard.holdSpace(() -> Players.localPlayer().isAnimating(), timeout);
    						MethodProvider.sleepUntil(() -> Players.localPlayer().isAnimating(), timeout);
    						return;
    					}
        				MethodProvider.sleepUntil(() -> Smithing.isOpen(),
        						() -> Players.localPlayer().isMoving(), Sleep.calculate(2222, 2222),69);
        			}
    				Sleep.sleep(111, 111);
    				return;
    			}
    		}
    	}
    	if(Walking.shouldWalk(6) && Walking.walk(doricsAnvils.getCenter())) Sleep.sleep(420, 696);
		Sleep.sleep(111, 111);
		return;
    }
    public static int getSmithItemID(SmithItem smithItem,Metal metal)
    {
    	int id = -1;
    	switch(smithItem)
    	{
    	case DAGGER: 
    	{
    		if(metal == Metal.BRONZE) id = bronzeDagger;
    		else if(metal == Metal.IRON) id = ironDagger;
    		break;
    	}
    	case AXE: 
    	{
    		if(metal == Metal.BRONZE) id = bronzeAxe;
    		else if(metal == Metal.IRON) id = ironAxe;
    		break;
    	}
    	case SCIMITAR: 
    	{
    		if(metal == Metal.BRONZE) id = bronzeScimitar;
    		else if(metal == Metal.IRON) id = ironScimitar;
    		break;
    	}
    	case WARHAMMER: 
    	{
    		if(metal == Metal.BRONZE) id = bronzeWarhammer;
    		else if(metal == Metal.IRON) id = ironWarhammer;
    		break;
    	}
    	case KITESHIELD: 
    	{
    		if(metal == Metal.BRONZE) id = bronzeKiteshield;
    		else if(metal == Metal.IRON) id = ironKiteshield;
    		break;
    	}
    	case PLATELEGS: 
    	{
    		if(metal == Metal.BRONZE) id = bronzePlatelegs;
    		else if(metal == Metal.IRON) id = ironPlatelegs;
    		break;
    	}
    	case PLATEBODY: 
    	{
    		if(metal == Metal.BRONZE) id = bronzePlatebody;
    		else if(metal == Metal.IRON) id = ironPlatebody;
    		break;
    	}
    	case BATTLEAXE: 
    	{
    		if(metal == Metal.BRONZE) id = bronzeBattleaxe;
    		else if(metal == Metal.IRON) id = ironBattleaxe;
    		break;
    	}
    	default:break;
    	}
    	return id;
    }
    public static enum SmithItem {
    	DAGGER,
    	AXE,
    	SCIMITAR,
    	WARHAMMER,
    	BATTLEAXE,
    	KITESHIELD,
    	PLATELEGS,
    	PLATEBODY,
    	NONE,
    	UNSCRIPTED
    }
    public static enum Metal {
    	BRONZE,
    	IRON,
    	NONE,
    	UNSCRIPTED
    }
    public static boolean isAutoSmithingItem(SmithItem itemToCheck, Metal metalToCheck)
    {
    	return getAutoSmithItem() == itemToCheck && getAutoSmithMetal() == metalToCheck;
    }
    /**
     * gets the SmithItem currently chosen to auto-smith when pressing space in smithin menu
     * @return
     */
    public static SmithItem getAutoSmithItem()
    {
    	switch(PlayerSettings.getBitValue(13895))
    	{
    	case(0):return SmithItem.NONE;
    	case(1):return SmithItem.DAGGER;
    	case(6):return SmithItem.AXE;
    	case(3):return SmithItem.SCIMITAR;
    	case(8):return SmithItem.WARHAMMER;
    	case(9):return SmithItem.BATTLEAXE;
    	case(12):return SmithItem.PLATELEGS;
    	case(14):return SmithItem.PLATEBODY;
    	case(19):return SmithItem.KITESHIELD;
    	default:return SmithItem.UNSCRIPTED;
    	}
    }
    /**
     * gets the Metal type currently chosen to auto-smith when pressing space in smithin menu
     * @return
     */
    public static Metal getAutoSmithMetal()
    {
    	switch(PlayerSettings.getBitValue(13984))
    	{
    	case(0):return Metal.NONE;
    	case(1):return Metal.BRONZE;
    	case(2):return Metal.IRON;
    	default:return Metal.UNSCRIPTED;
    	}
    }
    private final static int iron1 = 11364;
    private final static int iron2 = 11365;
    private final static int copper1 = 10943;
    private final static int copper2 = 11161;
    private final static int clay1 = 11362;
    private final static int clay2 = 11363;
    private final static int tin1 = 11360;
    private final static int tin2 = 11361;
    private final static Area copperBest = new Area(2978, 3245, 2977, 3248, 0);
    private final static Area bestIrons = new Area(2971, 3237, 2968, 3242, 0);
    private final static Area tinsBest = new Area(2984, 3237, 2986, 3235, 0);
    private static Tile randTile = null;
    public static boolean completedDoricsQuest()
    {
    	return PlayerSettings.getConfig(31) >= 100;
    }
    //0 is unstarted, 10 is started, 100 is turned in and complete
    public static void doDoricsQuest()
    {
    	final int copperCount = Inventory.count(copperOre);
    	final int clayCount = Inventory.count(clay);
    	final int ironCount = Inventory.count(ironOre);
    	if(copperCount >= 4 && clayCount >= 6 && ironCount >= 2)
    	{
    		Main.customPaintText3 = "~~ Have all supplies! Going to talk with Doric ~~";
    		if(doricsAnvils.getCenter().distance() <= 15)
    		{
    			NPC doric = NPCs.closest(n -> n!=null && n.getName().equals("Doric") && doricsAnvils.contains(n));
    			if(doric != null)
    			{
    				if(doric.canReach())
    				{
    					if(doric.interact("Talk-to"))
        				{
        					MethodProvider.sleepUntil(() -> Dialogues.inDialogue(), () -> Players.localPlayer().isMoving(), Sleep.calculate(2222,2222),69);
        				}
        				Sleep.sleep(111, 111);
        				return;
    				}
    				if(Walking.shouldWalk() && Walking.walk(doric)) Sleep.sleep(420, 696);
    				return;
    			}
    			
    		}
    		if(Walking.shouldWalk() && Walking.walk(doricsAnvils.getCenter())) Sleep.sleep(420, 696);
    		return;
    	}
    	if(Skills.getRealLevel(Skill.MINING) < 15)
    	{
    		if(dropQty == 0)
    		{
    			dropQty = (int) Calculations.nextGaussianRandom(4, 3);
    			if(dropQty < 1) dropQty = 1;
    			else if(dropQty >= 10) dropQty = 10;
    		}
    		Main.customPaintText3 = "~~ Powermining copper, next drop at "+dropQty+" copper ores ~~";
    		if(Inventory.count(copperOre) >= dropQty)
    		{
    			if(Inventory.dropAll(i -> i!=null && (i.getID() == copperOre || i.getID() == tinOre || i.getID() == clay || i.getID() == clueGeodeBeginner)))
    			{
    				MethodProvider.sleepUntil(() -> Inventory.contains(copperOre,tinOre,clay,clueGeodeBeginner), Sleep.calculate(2222, 2222));
    			}
    			dropQty = 0;
    			return;
    		}
    		Filter<GameObject> coppersFilter =g -> g!=null && g.hasAction("Mine") &&
    	    				copperBest.contains(g) && (g.getID() == copper1 || g.getID() == copper2);
    		mineOre(coppersFilter);
    		return;
    	}
    	//have 15 mining
		Main.customPaintText3 = "~~ Lvl "+Skills.getRealLevel(Skill.MINING)+" mining! Gathering 4 copper, 6 clay, then 2 iron ores ~~";
    	Filter<GameObject> neededOresFilter = g -> g!=null && g.hasAction("Mine") && (
    			(Inventory.count(copperOre) < 4 && copperBest.contains(g) && (g.getID() == copper1 || g.getID() == copper2)) || 
				(Inventory.count(copperOre) >= 4 && Inventory.count(clay) < 6 && rimmingtonMine.contains(g) && (g.getID() == clay1 || g.getID() == clay2) || 
				(Inventory.count(copperOre) >= 4 && Inventory.count(clay) >= 6 && bestIrons.contains(g) && (g.getID() == iron1 || g.getID() == iron2))));
    	mineOre(neededOresFilter);
		return;
    	
    }
    public static int dropQty = 0;
    public static void mineOre(Filter<GameObject> rockFilter)
    {
    	if(!rimmingtonMine.contains(Players.localPlayer()))
    	{
    		Main.customPaintText1 = "~~ Walking to Rimmington ~~";
    		if(randTile == null) randTile = rimmingtonMine.getRandomTile();
    		if(Walking.shouldWalk(6) && Walking.walk(randTile)) Sleep.sleep(420,696);
    		return;
    	}
    	if(randTile != null) randTile = null;
    	int playersCount = 0;
    	for(Player p : Players.all(p -> p!=null && !p.equals(Players.localPlayer()) && p.distance() < 4))
    	{
    		if(bestIrons.contains(Players.localPlayer())) playersCount++;
    		playersCount++;
    	}
    	if(playersCount >= 3)
    	{
    		final int world = API.getF2PWorld();
    		Main.customPaintText1 = "Hopping to w"+world+" after seeing "+(bestIrons.contains(Players.localPlayer()) ? (int)(playersCount / 2):playersCount)+" other players closeby!";
    		MethodProvider.log("Hopping to w"+world+" after seeing "+(bestIrons.contains(Players.localPlayer()) ? (int)(playersCount / 2):playersCount)+" other players closeby!");
    		MissingAPI.scrollHopWorld(world);
    		return;
    	}
    	Main.customPaintText1 = "~~ Mining ores ~~";
    	MethodProvider.log("Beginning selection / find / sort of ores");
    	if(rockFilter == null) 
    	{
    	    rockFilter = g -> g!=null && g.hasAction("Mine") &&
    	    		((Skills.getRealLevel(Skill.MINING) < 15 || Skills.getRealLevel(Skill.SMITHING) < 15) && 
    	    				((Inventory.count(copperOre) >= 13 && tinsBest.contains(g) && (g.getID() == tin1 || g.getID() == tin2)) ||
    	    						(Inventory.count(copperOre) < 13 && copperBest.contains(g) && (g.getID() == copper1 || g.getID() == copper2))) || 
    	    				((Skills.getRealLevel(Skill.MINING) >= 15 && Skills.getRealLevel(Skill.SMITHING) >= 15) && bestIrons.contains(g) && (g.getID() == iron1 || g.getID() == iron2)));
    	    	
    	}
    	List<GameObject> validRocks = GameObjects.all(rockFilter);
    	Timer test = new Timer(10000);
    	if(validRocks == null || validRocks.isEmpty()) return;
    	//time to go through all ores, find closest (walking dist) stand tile for each one, sort the list, and go mine the nearest rock
    	LinkedHashMap<GameObject,ObjectTile> rocksNTiles = new LinkedHashMap<GameObject,ObjectTile>();
    	final Tile loc = Players.localPlayer().getTile();
    	for(GameObject rock : validRocks)
    	{
    		if(rock == null || !rock.exists()) continue;
    		//sort walking distance lowest to highest
    		List<Tile> surrounding = rock.getSurrounding();
    		List<ObjectTile> surroundingRockTiles = new ArrayList<ObjectTile>();
    		for(Tile t : surrounding)
    		{
    			if(t == null || CollisionMap.isBlocked(Map.getFlag(t))) continue;
    			ObjectTile tt = new ObjectTile(t,(int)t.walkingDistance(loc));
    			surroundingRockTiles.add(tt);
    		}
    		if(surroundingRockTiles.isEmpty()) continue;
    		Collections.sort(surroundingRockTiles);
    		rocksNTiles.put(rock, surroundingRockTiles.get(0));
    	}
    	if(rocksNTiles.isEmpty()) return;
    	List<Entry<GameObject, ObjectTile>> rocksNTilesListOfMapEntries = new ArrayList<Entry<GameObject, ObjectTile>>(rocksNTiles.entrySet());
    	Collections.sort(
    			rocksNTilesListOfMapEntries,
                new Comparator<Entry<GameObject, ObjectTile> >() {
                    public int compare(
                        Entry<GameObject, ObjectTile> entry1,
                        Entry<GameObject, ObjectTile> entry2)
                    {
                        return entry1.getValue().getWalkingDistance() - entry2.getValue().getWalkingDistance();
                    }
                });
    	GameObject closestRock = rocksNTilesListOfMapEntries.get(0).getKey();
    	Tile closestRockTile = rocksNTilesListOfMapEntries.get(0).getValue().getTile();
    	final int changeX = closestRock.getX() - Players.localPlayer().getX();
    	final int changeY = closestRock.getY() - Players.localPlayer().getY();
    	
    	MethodProvider.log("Done sorting in "+test.elapsed()+"ms! Closest rock relative x/y: ("+changeX+", "+changeY+")");
    	if(closestRock.isOnScreen())
    	{
    		if(closestRock.interact("Mine"))
    		{
    			String name = closestRock.getName();
    			Main.customPaintText1 = "Mine -> "+name;
    			MethodProvider.sleepUntil(() -> Dialogues.inDialogue() || closestRock == null || !closestRock.exists(), 
    					() -> Players.localPlayer().isAnimating() || Players.localPlayer().isMoving(),
    					Sleep.calculate(2222,2222),69);
    			Main.customPaintText1 = "Mine -> "+name+" SleepUntil expired";
    		}
    		return;
    	}
    	
    	//instead of walking to closest object, walk to closest walking distance tile around the closest object
    	if(Walking.shouldWalk() && Walking.walk(closestRockTile))
    	{
    		MethodProvider.sleepUntil(() -> closestRock.isOnScreen(), Sleep.calculate(696,1111));
    	}
    }
    private final static Area hammerArea = new Area(
			new Tile(3350, 3168, 0),
			new Tile(3344, 3163, 0),
			new Tile(3351, 3159, 0),
			new Tile(3358, 3159, 0),
			new Tile(3359, 3168, 0));
    public static boolean getHammer()
    {
    	if(Inventory.contains(hammer)) return true;
    	if(Bank.contains(hammer))
    	{
        	Main.customPaintText1 = "~~ Getting hammer from Bank ~~";
    		if(API.walkOpenBank())
    		{
    			if(Bank.getWithdrawMode() != BankMode.ITEM)
				{
					Bank.setWithdrawMode(BankMode.ITEM);
					Sleep.sleep(111,222);
					return false;
				}
				if(Bank.withdraw(hammer,1))
				{
					MethodProvider.log("Withdrawing hammer");
					MethodProvider.sleepUntil(() -> Inventory.contains(hammer), Sleep.calculate(2222,2222));
				}
    		}
    		Sleep.sleep(111, 1111);
    		return false;
    	}
    	//no hammer in bank
    	Main.customPaintText1 = "~~ Getting hammer in bumfuck nowhere, pls wait ~~";
    	
    	if(hammerArea.contains(Players.localPlayer()))
    	{
    		GroundItem pickaxeGround = GroundItems.closest(g -> g!=null && 
    				g.getName().equals("Hammer") && 
    				g.hasAction("Take") &&
    				g.isOnScreen());
    		if(pickaxeGround != null)
    		{
    			if(pickaxeGround.interact("Take"))
    			{
    				MethodProvider.sleepUntil(() -> Inventory.contains(hammer), 
    						() -> Players.localPlayer().isMoving(),Sleep.calculate(2222,2222),69);
    			}
    			Sleep.sleep(111, 1111);
    			return false;
    		}
    	}
    	if(Walking.shouldWalk(6) && Walking.walk(Map.getWalkable(hammerArea.getCenter()))) Sleep.sleep(111,1111);
		Sleep.sleep(111,111);
		return false;
    }
    
    public static void smeltBars()
    {
    	Main.customPaintText1 = "~~ Walking to Rimmington ~~";
    	if(faladorForge.contains(Players.localPlayer()))
		{
    		if(!Inventory.contains(copperOre,tinOre,ironOre))
    		{
    			MethodProvider.log("Holy smokes! Mined invy of jewels, depositing...");
    			if(Bank.open(Bank.getClosestBankLocation()))
    			{
    				if(Bank.depositAllExcept("Rune pickaxe","Adamant pickaxe","Mithril pickaxe","Steel pickaxe"))
    				{
    					Bank.close();
    				}
    			}
    			Sleep.calculate(410, 1111);
    			return;
    		}
			
			if(ItemProcessing.isOpen())
			{
				if(Inventory.contains("Copper ore") && Inventory.contains("Tin ore"))
				{
					if(ItemProcessing.makeAll("Bronze bar"))
					{
						MethodProvider.sleepUntil(() -> !Inventory.contains("Tin ore") || !Inventory.contains("Copper ore"),
								() -> Players.localPlayer().isAnimating(),
								Sleep.calculate(2222,2222),69);
					}
					Sleep.sleep(420,1111);
					return;
				}
				if(Inventory.contains("Iron ore"))
				{
					if(ItemProcessing.makeAll("Iron bar"))
					{
						MethodProvider.sleepUntil(() -> !Inventory.contains("Iron ore"),
								() -> Players.localPlayer().isAnimating(),
								Sleep.calculate(2222,2222),69);
					}
					Sleep.sleep(420,1111);
					return;
				}
			}
			GameObject forge = GameObjects.closest(g -> g!=null && g.getName().equals("Furnace") && g.hasAction("Smelt") && 
					faladorForge.getCenter().distance(g.getTile()) < 15);
			if(forge != null)
			{
				if(forge.interact("Smelt"))
				{
					MethodProvider.sleepUntil(() -> ItemProcessing.isOpen(), () -> Players.localPlayer().isMoving(),Sleep.calculate(2222,2222),69);
				}
			}
			Sleep.sleep(420,1111);
			return;
		}
    	if(Walking.shouldWalk(6) && Walking.walk(faladorForge.getCenter())) Sleep.sleep(420, 1111);
		return;
    }
    private final static Area takePickaxeArea = new Area(
			new Tile(3009, 3341, 0),
			new Tile(3009, 3342, 0),
			new Tile(3013, 3342, 0),
			new Tile(3013, 3335, 0),
			new Tile(3013, 3335, 0),
			new Tile(3009, 3335, 0),
			new Tile(3009, 3336, 0),
			new Tile(3010, 3337, 0));
    public static boolean haveOtherPickaxesInvyEquip(String bestPick)
    {
    	if(Inventory.count(bestPick) + Equipment.count(bestPick) > 1) return true;
    	List<String> otherPicks = new ArrayList<String>();
		String[] otherPickz = {"Rune pickaxe","Adamant pickaxe","Mithril pickaxe","Steel pickaxe","Iron pickaxe","Bronze pickaxe"};
		otherPicks.addAll(Arrays.asList(otherPickz));
		otherPicks.remove(bestPick);
		if(Inventory.contains(i -> i!=null && otherPicks.contains(i.getName())) || 
				Equipment.contains(i -> i!=null && otherPicks.contains(i.getName()))) return true;
		return false;
    }
    public static boolean getBestPickaxe()
    {
    	String bestPick = bestPickaxe();
    	if(bestPick == null)
    	{
    		getBronzePickaxe();
    		return false;
    	}
    	if(!Equipment.contains(bestPick) && Inventory.contains(bestPick))
    	{
    		final int att = Skills.getRealLevel(Skill.ATTACK);
    		boolean cannotEquip = false;
    		if(bestPick.contains("Rune pickaxe") && att < 40) cannotEquip = true;
    		if(bestPick.contains("Adamant pickaxe") && att < 30) cannotEquip = true;
    		if(bestPick.contains("Mithril pickaxe") && att < 20) cannotEquip = true;
    		if(bestPick.contains("Steel pickaxe") && att < 5) cannotEquip = true;
    		if(cannotEquip) 
    		{
    			MethodProvider.log("Have best pick in invy but cannot equip it at att lvl! "+bestPick+" - lvl "+att);
    			return true;
    		}
    		MethodProvider.log("Equipping pickaxe: " + bestPick+" with att lvl: " + att);
    		Main.customPaintText1 = "Equipping pickaxe: " + bestPick+" with att lvl: " + att;
    		if(!Bank.isOpen() && !Tabs.isOpen(Tab.INVENTORY))
    		{
    			Tabs.open(Tab.INVENTORY);
    			Sleep.sleep(111, 111);
    			return false;
    		}
    		if(Inventory.interact(bestPick,"Wield")) MethodProvider.sleepUntil(() -> Equipment.contains(bestPick), Sleep.calculate(2222, 2222));
    		Sleep.sleep(69, 696);
    		return false;
    	}
    	if(Equipment.contains(bestPick) || Inventory.contains(bestPick))
    	{
    		if(!haveOtherPickaxesInvyEquip(bestPick)) return true;
    		MethodProvider.log("Depositing other picks to bank");
    		Main.customPaintText1 = "~~ Depositing other picks to bank ~~";
    	} else {
    		MethodProvider.log("Withdrawing best pick: "+ bestPick+" from bank ~~");
    		Main.customPaintText1 = "~~ Withdrawing best pick: " +bestPick+ " from bank ~~";
    	}
    	//best pick must be in bank here, or have other picks to deposit
    	if(API.walkOpenBank())
    	{
    		List<String> otherPicks = new ArrayList<String>();
    		String[] otherPickz = {"Rune pickaxe","Adamant pickaxe","Mithril pickaxe","Steel pickaxe","Iron pickaxe","Bronze pickaxe"};
    		otherPicks.addAll(Arrays.asList(otherPickz));
    		otherPicks.remove(bestPick);
    		for(String otherPick:otherPicks)
    		{
    			if(Inventory.contains(otherPick))
    			{
    				if(Bank.depositAll(i -> i!=null && 
    	    				otherPicks.contains(i.getName())))
    	    		{
    					MethodProvider.sleepUntil(() -> !Inventory.contains(otherPick),Sleep.calculate(2222, 2222));
    					Sleep.sleep(111,1111);
    					return false;
    	    		}
    			}
    		}
    		//check if extra picks of best type, deposit them
			if((Inventory.count(bestPick) + Equipment.count(bestPick)) > 1)
			{
				Main.customPaintText1 = "~~ Depositing extra of "+bestPick+" ~~";
				MethodProvider.log("~~ Depositing extra of "+bestPick+" ~~");
				if(Bank.deposit(bestPick, (Inventory.count(bestPick) + Equipment.count(bestPick)) - 1))
				{
					MethodProvider.sleepUntil(() -> (Inventory.count(bestPick) + Equipment.count(bestPick)) == 1, Sleep.calculate(2222, 2222));
				}
				Sleep.sleep(111, 111);
				return false;
			}
			Main.customPaintText1 = "~~ Withdrawing "+bestPick +" ~~";
			MethodProvider.log("~~ Withdrawing "+bestPick +" ~~");
			if(Bank.withdraw(bestPick, 1))
			{
				MethodProvider.sleepUntil(() -> (Inventory.count(bestPick) + Equipment.count(bestPick)) == 1, Sleep.calculate(2222, 2222));
			}
			Sleep.sleep(111, 111);
			return false;
    	}
    	Sleep.sleep(111, 111);
    	return false;
    }
    public static String bestPickaxe()
    {
    	final int mining = Skills.getRealLevel(Skill.MINING);
    	
    	if(mining >= 41 && Equipment.contains("Rune pickaxe") || 
    				Inventory.contains("Rune pickaxe") || 
    				Bank.contains("Rune pickaxe")) return "Rune pickaxe";
    	if(mining >= 31 && Equipment.contains("Adamant pickaxe") || 
    				Inventory.contains("Adamant pickaxe") || 
    				Bank.contains("Adamant pickaxe")) return "Adamant pickaxe";
    	if(mining >= 21 && Equipment.contains("Mithril pickaxe") || 
    				Inventory.contains("Mithril pickaxe") || 
    				Bank.contains("Mithril pickaxe")) return "Mithril pickaxe";
    	if(mining >= 6 && Equipment.contains("Steel pickaxe") || 
    				Inventory.contains("Steel pickaxe") || 
    				Bank.contains("Steel pickaxe")) return "Steel pickaxe";
    	if(Equipment.contains("Iron pickaxe") || 
			Inventory.contains("Iron pickaxe") || 
			Bank.contains("Iron pickaxe")) return "Iron pickaxe";
    	if(Equipment.contains("Bronze pickaxe") || 
    			Inventory.contains("Bronze pickaxe") || 
    			Bank.contains("Bronze pickaxe")) return "Bronze pickaxe";
    	
    	return null;
	}
		
    public static void getBronzePickaxe()
    {

		Main.customPaintText1 = "~~ Grabbing bonze pickaxe from Falador spawn ~~";
		MethodProvider.log("No pick, walking to get bronze one");
    	//no pickaxe in bank either
    	
    	if(takePickaxeArea.contains(Players.localPlayer()))
    	{
    		GroundItem pickaxeGround = GroundItems.closest(g -> g!=null && 
    				g.getName().equals("Bronze pickaxe") && 
    				g.hasAction("Take") &&
    				g.isOnScreen());
    		if(pickaxeGround != null)
    		{
    			if(pickaxeGround.interact("Take"))
    			{
    				MethodProvider.sleepUntil(() -> Inventory.contains("Bronze pickaxe"), 
    						() -> Players.localPlayer().isMoving(),Sleep.calculate(2222,2222),69);
    			}
    			Sleep.sleep(111, 1111);
    			return;
    		}
    	}
    	if(Walking.shouldWalk(6) && Walking.walk(Map.getWalkable(takePickaxeArea.getCenter()))) Sleep.sleep(111,1111);
		Sleep.sleep(111,111);
		return;
    }
    public static void handleBanking()
    {
    	if(API.walkOpenBank())
    	{
    		if(Bank.depositAllExcept("Rune pickaxe","Adamant pickaxe","Mithril pickaxe","Steel pickaxe"))
    		{
    			MethodProvider.sleepUntil(() -> Inventory.isEmpty() || Inventory.onlyContains("Rune pickaxe","Adamant pickaxe","Mithril pickaxe","Steel pickaxe"), Sleep.calculate(2222, 2222));
    			Sleep.sleep(420, 1111);
    		}
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
    		if(Dialogues.chooseOption("Sorry, would it be OK if I used your anvils?") || 
    				Dialogues.chooseOption("Yes.") || 
    				Dialogues.chooseOption("I wanted to use your anvils."))
    		{
    			Sleep.sleep(420, 696);
    			return true;
    		}
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