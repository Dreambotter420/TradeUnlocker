package script.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.dreambot.api.ClientSettings;
import org.dreambot.api.data.ActionMode;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.utilities.Timer;



public class API {
	
	public static String currentBranch = "";
    public static String currentLeaf = "";
    public static enum modes {
		IDLE,TRAIN_COMBAT,QUEST
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
        			MethodProvider.sleepUntil(() -> Widgets.getWidgetChild(134, 23, 2) == null || 
        					!Widgets.getWidgetChild(134, 23, 2).isVisible(), 5555);
            		Sleep.sleep(333,444);
        		}
        		//Enable profanity button toggle is visible
        		if(Widgets.getWidgetChild(134, 19, 1) != null && Widgets.getWidgetChild(134, 19, 1).isVisible())
            	{
        			Widgets.getWidgetChild(134, 19, 1).interact("Toggle");
        			MethodProvider.sleepUntil(() -> Widgets.getWidgetChild(134,18,4) == null, 5555);
        			Sleep.sleep(777,333);
                }
        	} 
        	else
        	{
        		//"All settings" button visible in Settings tab
        		if(Widgets.getWidgetChild(116,75) != null && Widgets.getWidgetChild(116,75).isVisible())
            	{
        			Widgets.getWidgetChild(116,75).interact();
        			MethodProvider.sleepUntil(() -> Widgets.getWidgetChild(116,75) == null,5555);
            		Sleep.sleep(333,444);
                } 
        		else 
            	{
            		if(Widgets.getWidgetChild(548,50) != null && Widgets.getWidgetChild(548,50).isVisible())
                	{
            			Widgets.getWidgetChild(548,50).interact();
            			MethodProvider.sleepUntil(() -> Widgets.getWidgetChild(548,50) == null,5555);
                		Sleep.sleep(333,444);
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
			Sleep.sleep(666, 111);
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
			MethodProvider.log("AFK: 0.001% chance, max 240s");
			Sleep.sleep(50,10000);
		}
		else if(tmp < 6)  
		{
			MethodProvider.log("AFK: 0.003% chance, max 120s");
			Sleep.sleep(50,5000);
		}
		else if(tmp < 25)
		{
			MethodProvider.log("AFK: 0.095% chance, max 40s");
			Sleep.sleep(50,3000);
		}
		else if(tmp < 150)  
		{
			MethodProvider.log("AFK: .625% chance, max 20s");
			Sleep.sleep(50,2000);
		}
		else if(tmp < 1000)  
		{
			MethodProvider.log("AFK: 4.25% chance, max 6.0s");
			Sleep.sleep(50,1200);
		}
		else if(tmp < 3000)  
		{
			MethodProvider.log("AFK: 10.0% chance, max 3.2");
			Sleep.sleep(50,600);
		}
	}
	
	public static int getF2PWorld()
	{
		List<World> verifiedWorlds = new ArrayList<World>();
		for(World tmp : Worlds.noMinimumLevel())
		{
			if(	!tmp.isMembers()
					&& !tmp.isPVP()
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
