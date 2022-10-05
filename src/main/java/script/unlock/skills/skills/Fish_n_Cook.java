package script.unlock.skills.skills;

import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.bank.BankMode;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Map;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.helpers.ItemProcessing;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import script.Main;
import script.behaviour.DecisionLeaf;
import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Sleepz;

import script.p;
public class Fish_n_Cook extends Leaf {

    @Override
    public boolean isValid() 
    {
    	return API.unlockMode == API.modes.FISH;
    }
    private final static Area treeArea = new Area(3082, 3245, 3087, 3241, 0);
    private final static Area fishArea = new Area(3080, 3237, 3090, 3218, 0);
    public static boolean announcedTasty = false;
    
    @Override
    public int onLoop() {
    	if(API.canHandleDialogues())
		{
			return Sleepz.calculate(69, 69);
		}
    	if(DecisionLeaf.taskTimer.finished())
    	{
    		if(!Inventory.isEmpty())
    		{
    			if(Bank.open(BankLocation.getNearest()))
    			{
    				if(Bank.depositAllItems())
    				{
    					Bank.close();
    				}
    			}
    			return Sleepz.calculate(420,1111);
    		}
    		Logger.log("Finished fishing task!");
    		API.unlockMode = null;
    		return 10;
    	}
    	//step 0-1: announce MMM TASTY
    	if(!announcedTasty)
    	{
    		Keyboard.type("Mmmmmmmm tasty~");
    		announcedTasty = true;
    	}
    	if(getAxe() && getSmallNet() && getTinderbox())
    	{
    		if(Inventory.isFull() || Inventory.emptySlotCount() <= 1)
        	{
        		if(!Inventory.contains("Raw shrimps") && !Inventory.contains("Raw anchovies"))
        		{
        			handleBanking();
        			return Sleepz.calculate(111,1212);
        		}
        		cookFish();
        		return Sleepz.calculate(111,1212);
        	}
        	walkFish();
    	}
		return Sleepz.calculate(111,111);
    }
    private final static Area takeAxeArea = new Area(3230, 3298, 3236, 3292, 0);
    public static boolean getAxe()
    {
    	if(Equipment.contains("Rune axe","Adamant axe","Mithril axe","Steel axe","Iron axe","Bronze axe") ||
    			Inventory.contains("Rune axe","Adamant axe","Mithril axe","Steel axe"))
    		return true;
    	String axe = null;
    	if(Inventory.contains("Bronze axe")) axe = "Bronze axe";
    	if(Inventory.contains("Iron axe")) axe = "Iron axe";
    	if(axe != null)
    	{
    		if(!Tabs.isOpen(Tab.INVENTORY))
    		{
    			Tabs.open(Tab.INVENTORY);
    			return false;
    		}
    		if(Inventory.interact(axe, "Wield"))
    		{
    			final String axeFinal = axe;
    			Sleep.sleepUntil(() -> Equipment.contains(axeFinal), Sleepz.calculate(2222,2222));
    		}
    		return false;
    	}
    	//no iron/bronze axe in invy
    	if(Bank.contains("Rune axe","Adamant axe","Mithril axe","Steel axe","Iron axe","Bronze axe")) 
    	{
    		axe = Bank.get("Rune axe","Adamant axe","Mithril axe","Steel axe","Iron axe","Bronze axe").getName();
    	}
    	if(axe != null)
    	{
    		if(Bank.open(Bank.getClosestBankLocation()))
    		{
    			if(Bank.getWithdrawMode() != BankMode.ITEM)
    			{
    				Bank.setWithdrawMode(BankMode.ITEM);
    				Sleepz.sleep(111,222);
    				return false;
    			}
    			if(Bank.withdraw(axe,1))
    			{
        			final String axeFinal = axe;
    				Sleep.sleepUntil(() -> Inventory.contains(axeFinal), Sleepz.calculate(2222,2222));
    			}
    		}
    		Sleepz.sleep(111,444);
    		return false;
    	}
    	//no axe in bank either
    	if(takeAxeArea.getCenter().distance() < 15)
    	{
    		GameObject axeLog = GameObjects.closest(g -> g!=null && 
    				g.getName().equals("Logs") && 
    				g.hasAction("Take-axe") && 
    				g.canReach() && 
    				g.isOnScreen());
    		if(axeLog != null)
    		{
    			if(axeLog.interact("Take-axe"))
    			{
    				Sleep.sleepUntil(() -> Inventory.contains("Bronze axe"), 
    						() -> p.l.isMoving(),Sleepz.calculate(2222,2222),69);
    			}
    			return false;
    		}
    	}
    	if(Walking.shouldWalk(6) && Walking.walk(Map.getWalkable(takeAxeArea.getRandomTile()))) Sleepz.sleep(111,1111);
		Sleepz.sleep(111,111);
		return false;
    }
    private final static Area fishNetArea = new Area(3245, 3154, 3242, 3159, 0);
    public static boolean getSmallNet()
    {
    	if(Inventory.contains("Small fishing net")) return true;
    	if(fishNetArea.getCenter().distance() < 15)
    	{
    		GameObject fishNet = GameObjects.closest(g -> g!=null && 
    				g.hasAction("Take") && 
    				g.getName().contains("Small fishing net") && 
    				g.isOnScreen());
    		if(fishNet != null)
    		{
    			if(fishNet.interact("Take"))
    			{
    				Sleep.sleepUntil(() -> Inventory.contains("Small fishing net"),
    						() -> p.l.isMoving() || p.l.isAnimating(),
    						Sleepz.calculate(2222,2222),69);
    			}
    			return false;
    		}
    	}
    	if(Walking.shouldWalk(6) && Walking.walk(Map.getWalkable(fishNetArea.getRandomTile()))) Sleepz.sleep(111,1111);
		Sleepz.sleep(111,111);
		return false;
    }
    private final static Area tinderboxArea = new Area(3094, 3251, 3087, 3255, 1);
	private final static Tile wizardDudeTile = new Tile(3088, 3254, 0);
	private final static Tile bookshelfTile = new Tile(3094, 3253, 1);
	private final static Area wizardDudeArea = new Area(3087, 3255, 3094, 3251, 0);
    public static boolean getTinderbox()
    {
    	if(Inventory.contains("Tinderbox") && !tinderboxArea.contains(p.l)) return true;
    	if(Bank.contains("Tinderbox"))
    	{
    		if(Bank.open(Bank.getClosestBankLocation()))
    		{
    			if(Bank.getWithdrawMode() != BankMode.ITEM)
    			{
    				Bank.setWithdrawMode(BankMode.ITEM);
    				Sleepz.sleep(111,222);
    				return false;
    			}
    			if(Bank.withdraw("Tinderbox",1))
    			{
    				Sleep.sleepUntil(() -> Inventory.contains("Tinderbox"), Sleepz.calculate(2222,2222));
    			}
    		}
    		Sleepz.sleep(111,444);
    		return false;
    	}
    	if(PlayerSettings.getConfig(279) == 0) //need to talk to wizard dued
		{
    		if(Dialogues.canContinue())
			{
				if(Dialogues.continueDialogue()) Sleepz.sleep(696,420);
				return false;
			}
			if(Dialogues.isProcessing()) 
			{	
				Sleepz.sleep(696,420);
				return false;
			}
			NPC wiseOldMan = NPCs.closest(n -> n!=null && n.getName().equals("Wise Old Man"));
			if(wiseOldMan != null)
			{
				if(wizardDudeTile.canReach())
				{
					if(wiseOldMan.interact("Talk-to"))
					{
						Sleep.sleepUntil(Dialogues::inDialogue,
								()->p.l.isMoving(),3000,69);
					}
					return false;
				}
			}
			if(Walking.shouldWalk(6) && Walking.walk(wizardDudeTile)) Sleepz.sleep(111,1111);
			Sleepz.sleep(111,111);
			return false;
		}
    	// must be OK to get tinderboxes now
    	if(tinderboxArea.contains(p.l))
		{
    		if(Inventory.contains("Tinderbox"))
    		{
    			GameObject staircase = GameObjects.closest(g -> g!=null && g.getName().equals("Staircase") && g.hasAction("Climb-down"));
    			if(staircase != null)
    			{
    				if(staircase.interact("Climb-down"))
    				{
    					Sleep.sleepUntil(() -> !tinderboxArea.contains(p.l),
    							() -> p.l.isMoving(),Sleepz.calculate(2222,2222),69);
    				}
    			}
    			return false;
    		}
			GameObject books = GameObjects.closest(p -> p.getTile().equals(bookshelfTile) && 
					p.getName().toLowerCase().contains("book"));
			if(books != null)
			{
				if(books.interact("Search"))
				{
					Mouse.moveMouseOutsideScreen();
					Sleep.sleepUntil(() -> Inventory.contains("Tinderbox"),
							() -> p.l.isMoving() || p.l.isAnimating(),Sleepz.calculate(2222,2222),69);
					Sleepz.sleep(111,1111);
				}
			}
			Sleepz.sleep(111,111);
			return false;
		}
		GameObject staircase = GameObjects.closest(g -> g!=null &&
				g.getName().equals("Staircase") &&
				wizardDudeArea.contains(g) && 
				g.canReach() && 
				g.isOnScreen());
		if(staircase != null)
		{
			if(staircase.interact("Climb-up"))
			{
				Sleep.sleepUntil(() -> tinderboxArea.contains(p.l), () -> p.l.isMoving(),Sleepz.calculate(2222,2222), 69);
			}
			return false;
		}
		if(Walking.shouldWalk(6) && Walking.walk(wizardDudeTile)) Sleepz.sleep(111,1111);
		Sleepz.sleep(111,111);
		return false;
    }
    public static void cookFish()
    {
    	if(ItemProcessing.isOpen())
    	{
    		if(ItemProcessing.makeAll("Raw anchovies"))
    		{
    			Main.customPaintText1 = "Make all -> Raw anchovies";
    			Sleep.sleepUntil(() -> !Inventory.contains("Raw anchovies"),
    					() -> p.l.isAnimating(),Sleepz.calculate(2222,2222),69);
    			Main.customPaintText1 = "Make all -> Raw anchovies SleepUntil expired";
    		}
    		else if(ItemProcessing.makeAll("Raw shrimps"))
    		{
    			Main.customPaintText1 = "Make all -> Raw shrimps";
    			Sleep.sleepUntil(() -> !Inventory.contains("Raw shrimps"),
    					() -> p.l.isAnimating(),Sleepz.calculate(2222,2222),69);
    			Main.customPaintText1 = "Make all -> Raw shrimps SleepUntil expired";
    		}
    		return;
    	}
    	if(!p.l.isAnimating() && 
    			!p.l.isMoving()) 
    	{
    		Sleepz.sleep(696,1111);
    		if(p.l.isAnimating() || 
        			p.l.isMoving()) 
        	{
        		Sleepz.sleep(696,1111);
        		return;
        	}
    	}
    	GameObject fire = GameObjects.closest(g -> g!=null && 
    			g.getName().equals("Fire") &&
    			g.getID() == 26185 && 
    			g.distance() < 15 && 
    			g.isOnScreen());
    	if(fire != null)
    	{
    		if(Inventory.contains("Raw anchovies"))
    		{
    			if(Inventory.get("Raw anchovies").useOn(fire))
    			{
        			Main.customPaintText1 = "Use Raw anchovies -> fire";
    				Sleep.sleepUntil(() -> ItemProcessing.isOpen() || !Inventory.contains("Raw anchovies"),
    						() -> p.l.isMoving(),Sleepz.calculate(2222,2222),69);
        			Main.customPaintText1 = "Use Raw anchovies -> fire SleepUntil expired";
    			}
    		} else if(Inventory.contains("Raw shrimps"))
    		{
    			if(Inventory.get("Raw shrimps").useOn(fire))
    			{
        			Main.customPaintText1 = "Use Raw shrimps -> fire";
    				Sleep.sleepUntil(() -> ItemProcessing.isOpen() || !Inventory.contains("Raw shrimps"),
    						() -> p.l.isMoving(),Sleepz.calculate(2222,2222),69);
        			Main.customPaintText1 = "Use Raw shrimps -> fire SleepUntil expired";
    			}
    		}
    		return;
    	}
    	if(Inventory.contains("Logs"))
    	{
    		if(Inventory.get("Logs").useOn("Tinderbox"))
    		{
    			Main.customPaintText1 = "Use logs -> tinderbox";
    			Sleep.sleepUntil(() -> GameObjects.closest(g -> g!=null && 
    					g.getName().equals("Fire") &&
    					g.getID() == 26185 && 
    					g.distance() < 15 && 
    					g.isOnScreen()) != null,
    					() -> p.l.isAnimating(),
    					Sleepz.calculate(2222,2222),69);
    			Main.customPaintText1 = "Use logs -> tinderbox SleepUntil expired";
    		}
    		Sleepz.sleep(666,1111);
    		return;
    	}
    	if(treeArea.getCenter().distance() < 8)
    	{
    		if(Inventory.isFull())
    		{
    			if(!Inventory.drop("Raw anchovies")) Inventory.drop("Raw shrimps");
    			Sleepz.sleep(666,1111);
    			return;
    		}
    		GameObject tree = GameObjects.closest(g -> g!=null && g.getName().equals("Tree") && treeArea.contains(g) && 
    				g.getID() != 10041); //bad tree ID
    		if(tree != null)
    		{
    			if(tree.interact("Chop down"))
    			{
        			Main.customPaintText1 = "Chop down -> tree";
    				Sleep.sleepUntil(() -> Inventory.contains("Logs"),() -> p.l.isAnimating() || p.l.isMoving(),Sleepz.calculate(2222,2222), 69); 
        			Main.customPaintText1 = "Chop down -> tree SleepUntil expired";               
    			}
    			return;
    		}
    		return;
    	}
    	if(Walking.shouldWalk(6) && Walking.walk(Map.getWalkable(treeArea.getRandomTile())))
    	{
			Main.customPaintText1 = "Walk to Tree";
    		Sleepz.sleep(420,696);
    	}
    }
    public static void walkFish()
    {
    	if(!p.l.isAnimating() && 
    			!p.l.isMoving() && 
    			p.l.getInteractingCharacter() == null) 
    	{
    		Sleepz.sleep(696,1111);
    		if(p.l.isAnimating() || 
        			p.l.isMoving() || 
        			p.l.getInteractingCharacter() != null) 
        	{
        		Sleepz.sleep(696,1111);
        		return;
        	}
    	}
		NPC fish = NPCs.closest(f -> f!=null && f.getName().equals("Fishing spot") && f.hasAction("Small net"));
    	if(fish != null && fishArea.contains(fish))
    	{
    		if(fish.distance() <= 8)
        	{
        		if(fish.isOnScreen())
        		{
        			if(fish.interact("Small net"))
        			{
        				Main.customPaintText1 = "Small net -> fishing spot";
        				Sleep.sleepUntil(() -> Dialogues.inDialogue() || Inventory.emptySlotCount() <= 1, 
        						() -> p.l.isAnimating(),
        						Sleepz.calculate(2222,2222),69);
        				Main.customPaintText1 = "Small net -> fishing spot SleepUntil expired";
        			}
        			return;
        		}
        	}
    	}
    	if(Walking.shouldWalk(6) && Walking.walk(Map.getWalkable(fishArea.getRandomTile())))
		{
    		Main.customPaintText1 = "Walk to fish area";
			Sleepz.sleep(222, 1111);
		}
    }
    public static void handleBanking()
    {
    	if(API.walkOpenBank())
    	{
    		if(Bank.depositAllExcept("Tinderbox","Small fishing net"))
    		{
    			Bank.close();
    		}
    	}
    }
}