package script.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.dreambot.api.ClientSettings;
import org.dreambot.api.data.ActionMode;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankType;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.items.Item;

import script.p;


public class API {
	
	public static String currentBranch = "";
    public static String currentLeaf = "";
    public static enum modes {
		IDLE,TRAIN_COMBAT,QUEST,FISH,CHOP,MINE
    }
public static boolean unlockedTrade = false;
public static int gameTimeHours;
public static String playtime = "";
public static int initWorld = 335;
public static String sayCmd = null;
public static int attEndpoint = 0;
public static int defEndpoint = 0;
public static int strEndpoint = 0;
public static int attMidpoint = 0;
public static int defMidpoint = 0;
public static int strMidpoint = 0;
public static modes unlockMode = null;
public static boolean tradeUnlocked = false;
	public static boolean initialized = false;
	public static Random rand2 = new Random();
	public static double sleepMod;
	public static Timer runTimer;
	
	 
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
    			Sleepz.sleep(420, 696);
    			return true;
    		}
    		if(Walking.clickTileOnMinimap(p.l.getTile())) 
    		{
    			Sleepz.sleep(696, 420);
    		}
    		return true;
    	}
    	if(Dialogues.canContinue())
		{
    		int sleep = Sleepz.calculate(2222,2222);
			Keyboard.holdSpace(() -> !Dialogues.inDialogue() || Dialogues.areOptionsAvailable(), sleep);
			Sleep.sleepUntil(() -> !Dialogues.inDialogue() || Dialogues.areOptionsAvailable(), sleep);
			return true;
		}
    	return false;
    }
	public static boolean walkOpenBank()
    {
    	if(Bank.isOpen()) return true;
    	Entity bank = Bank.getClosestBank(BankType.BOOTH);
    	if(bank != null)
    	{
    		if(bank.distance() <= 8)
        	{
        		if(bank.isOnScreen())
        		{
        			if(bank.interact("Bank"))
        			{
        				Sleep.sleepUntil(() -> Bank.isOpen() || Dialogues.inDialogue(), () -> p.l.isMoving(), Sleepz.calculate(2222, 2222), 69);
        			}
        			Sleepz.sleep(69, 420);
        			return false;
        		}
        	}
    	}
    	if(Walking.shouldWalk(6) && !Bank.open(Bank.getClosestBankLocation())) Sleepz.sleep(420, 696);
    	return false;
    }
public static boolean onlyHaveItems(int... itemIDs)
{
	List<Integer> itemID2s = new ArrayList<Integer>();
	for(int i : itemIDs)
	{
		itemID2s.add(i);
	}
	return onlyHaveItems(itemID2s);
}
public static boolean onlyHaveItems(List<Integer> itemIDs) {
	if(itemIDs == null || itemIDs.isEmpty())
	{
		return Inventory.isEmpty();
	}
	Filter<Item> filter = i -> i != null && itemIDs.contains(i.getID());
	List<Item> invyExceptCoins = Inventory.except(filter);
	boolean haveException = false;
	for(Item i : invyExceptCoins)
	{
		if(i == null) 
		{
			continue;
		}
		if(i.getName() == null || i.getName().toLowerCase().equals("null"))
		{
			Logger.log("Name Null!");
			continue;
		}
		haveException = true;
	}
	return !haveException;
}
public static boolean onlyHaveItemNames(String... itemNames)
{
	List<String> itemID2s = new ArrayList<String>();
	for(String i : itemNames)
	{
		itemID2s.add(i);
	}
	return onlyHaveItemNames(itemID2s);
}
public static boolean onlyHaveItemNames(List<String> itemNames) {
	if(itemNames == null || itemNames.isEmpty())
	{
		return Inventory.isEmpty();
	}
	Filter<Item> filter = i -> i != null && itemNames.contains(i.getName());
	List<Item> invyExceptCoins = Inventory.except(filter);
	boolean haveException = false;
	for(Item i : invyExceptCoins)
	{
		if(i == null) 
		{
			continue;
		}
		if(i.getName() == null || i.getName().toLowerCase().equals("null"))
		{
			Logger.log("Name Null!");
			continue;
		}
		haveException = true;
	}
	return !haveException;
}
public static boolean checkedBank()
{
	//return true;
	if(Bank.getLastBankHistoryCacheTime() <= 0)
	{
		if(Bank.isOpen()) 
		{
			Bank.contains(995);
		}
		else
		{
			if(!Bank.open(Bank.getClosestBankLocation())) Sleepz.sleep(666,666);
		}
	}
	if(Bank.getLastBankHistoryCacheTime() > 0)
	{
		return true;
	}
	return false;
}
public static void randomizeSkillSetpoints() {
	//combat midpoints 5-15
	attMidpoint = (int) Calculations.nextGaussianRandom(15,5);
	defMidpoint = (int) Calculations.nextGaussianRandom(15,5);
	strMidpoint = (int) Calculations.nextGaussianRandom(15,5);
	
	//attack end 20-30
	attEndpoint = (int) Calculations.nextGaussianRandom(35,5);
	//defence end 20-30
	defEndpoint = (int) Calculations.nextGaussianRandom(35,5);
	//strength end 20-30
	strEndpoint = (int) Calculations.nextGaussianRandom(35,5);
}

	/**
	 * customizes settings to be less AIDS.
	 * Returns true if no AIDS, false if still AIDS.
	 * @return
	 */
	public static boolean customizeSettings()
	{
		if(ClientSettings.isAcceptAidEnabled())
    	{
    		ClientSettings.toggleAcceptAid(false);
    		
    	}
		else if(ClientSettings.roofsEnabled())
    	{
    		ClientSettings.toggleRoofs(false);
    		
    	}
		else if(ClientSettings.getNPCAttackOptionsMode() != ActionMode.ALWAYS_RIGHT_CLICK)
    	{
    		ClientSettings.setNPCAttackOptionsMode(ActionMode.ALWAYS_RIGHT_CLICK);
    		
    	}
		else if(ClientSettings.getPlayerAttackOptionsMode() != ActionMode.ALWAYS_RIGHT_CLICK)
    	{
    		ClientSettings.setPlayerAttackOptionsMode(ActionMode.ALWAYS_RIGHT_CLICK);
    		
    	}
		else if(!ClientSettings.isShiftClickDroppingEnabled())
    	{
    		ClientSettings.toggleShiftClickDropping(true);
    		
    	}
		else if(!ClientSettings.isEscInterfaceClosingEnabled())
    	{
    		ClientSettings.toggleEscInterfaceClosing(true);
    		
    	}
		else if(ClientSettings.isGameAudioOn())
    	{
    		ClientSettings.toggleGameAudio(false);
    		
    	}
    	else if(ClientSettings.isResizableActive())
    	{
    		ClientSettings.toggleResizable(false);
    		
    	}
    	else if(ClientSettings.isTradeDelayEnabled())
    	{
    		ClientSettings.toggleTradeDelay(false);
    		
    	}
    	else if(PlayerSettings.getConfig(1074) == 0)
    	{
        	//exit button for main Settings menu visible
        	if(Widgets.getWidgetChild(134,4) != null && Widgets.getWidgetChild(134,4).isVisible())
        	{
        		//chat tab of settings window is NOT selected ("select chat" action exists)
        		if(Widgets.getWidgetChild(134, 23, 2) != null &&
        				Widgets.getWidgetChild(134, 23, 2).isVisible())
        		{
        			Widgets.getWidgetChild(134, 23, 2).interact("Select Chat");
        			Sleep.sleepUntil(() -> Widgets.getWidgetChild(134, 23, 2) == null || 
        					!Widgets.getWidgetChild(134, 23, 2).isVisible(), 5555);
            		Sleepz.sleep(333,444);
        		}
        		//Enable profanity button toggle is visible
        		if(Widgets.getWidgetChild(134, 19, 1) != null && Widgets.getWidgetChild(134, 19, 1).isVisible())
            	{
        			Widgets.getWidgetChild(134, 19, 1).interact("Toggle");
        			Sleep.sleepUntil(() -> Widgets.getWidgetChild(134,18,4) == null, 5555);
        			Sleepz.sleep(777,333);
                }
        	} 
        	else
        	{
        		//"All settings" button visible in Settings tab
        		if(Widgets.getWidgetChild(116,75) != null && Widgets.getWidgetChild(116,75).isVisible())
            	{
        			Widgets.getWidgetChild(116,75).interact();
        			Sleep.sleepUntil(() -> Widgets.getWidgetChild(116,75) == null,5555);
            		Sleepz.sleep(333,444);
                } 
        		else 
            	{
            		if(Widgets.getWidgetChild(548,50) != null && Widgets.getWidgetChild(548,50).isVisible())
                	{
            			Widgets.getWidgetChild(548,50).interact();
            			Sleep.sleepUntil(() -> Widgets.getWidgetChild(548,50) == null,5555);
                		Sleepz.sleep(333,444);
                    }
            	}
        	}
    	}
		if(ClientSettings.isAcceptAidEnabled() || 
    			ClientSettings.roofsEnabled() || 
    			PlayerSettings.getConfig(1074) == 0 ||
    			ClientSettings.getNPCAttackOptionsMode() != ActionMode.ALWAYS_RIGHT_CLICK  || 
    			ClientSettings.getPlayerAttackOptionsMode() != ActionMode.ALWAYS_RIGHT_CLICK ||
    			!ClientSettings.isShiftClickDroppingEnabled() || 
    			!ClientSettings.isEscInterfaceClosingEnabled() || 
    			ClientSettings.isGameAudioOn() || 
    			ClientSettings.isResizableActive() || 
    			ClientSettings.isTradeDelayEnabled())
		{
			Sleepz.sleep(666, 111);
			return false;
		}
		else
		{
			return true;
		}
		
	}
	
	public static void randomAFK()
	{
		int tmp = API.rand2.nextInt(40000);
		if(tmp < 2)  
		{
			Logger.log("AFK: 0.001% chance, max 240s");
			Sleepz.sleep(50,10000);
		}
		else if(tmp < 6)  
		{
			Logger.log("AFK: 0.003% chance, max 120s");
			Sleepz.sleep(50,5000);
		}
		else if(tmp < 25)
		{
			Logger.log("AFK: 0.095% chance, max 40s");
			Sleepz.sleep(50,3000);
		}
		else if(tmp < 150)  
		{
			Logger.log("AFK: .625% chance, max 20s");
			Sleepz.sleep(50,2000);
		}
		else if(tmp < 1000)  
		{
			Logger.log("AFK: 4.25% chance, max 6.0s");
			Sleepz.sleep(50,1200);
		}
		else if(tmp < 3000)  
		{
			Logger.log("AFK: 10.0% chance, max 3.2");
			Sleepz.sleep(50,600);
		}
	}
	
	public static int getF2PWorld()
	{
		List<World> verifiedWorlds = new ArrayList<World>();
		for(World tmp : Worlds.noMinimumLevel())
		{
			if(!tmp.isMembers()
					&& tmp.isF2P()
					&& !tmp.isPVP()
					&& !tmp.isHighRisk() 
					&& !tmp.isLastManStanding()
					&& !tmp.isPvpArena() 
					&& !tmp.isSuspicious()
					&& !tmp.isLeagueWorld()
					&& !tmp.isTargetWorld()
					&& !tmp.isTournamentWorld()
					&& !tmp.isDeadmanMode()
					&& tmp.getWorld() != 301) //just avoid popular world)
			{
				verifiedWorlds.add(tmp);
			}
		}
		Collections.shuffle(verifiedWorlds);
		return verifiedWorlds.size() > 0 ? verifiedWorlds.get(0).getWorld() : 543; // default world 543 (f2p) if none found
	}
}
