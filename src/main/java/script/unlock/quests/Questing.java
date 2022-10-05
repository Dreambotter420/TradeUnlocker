package script.unlock.quests;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.quest.Quests;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;

import script.p;
import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Sleepz;

public class Questing  extends Leaf {
    public final Tile idleTile = new Tile(3420, 3185, 1);
	@Override
    public boolean isValid() {
    	return API.unlockMode == API.modes.QUEST;
    }
	private final Tile veronicaTile = new Tile(3110,3330,0);
    @Override
    public int onLoop() {
    	if(Quests.getQuestPoints() >= 10)
    	{
    		Logger.log("~~~~~~~~~~~~~~");
    		Logger.log("U got 10 a q p + 100 total lvl + 20 hrs playtime! Have fun trading directly to other players :-)");
    		Logger.log("~~~~~~~~~~~~~~");
    		return -1;
    	}
    	
    	if(PlayerSettings.getConfig(32) != 0) //started Ernest the Chikken
    	{
    		//start questing now
			QuestScriptStarter questStarter = new QuestScriptStarter();
			Logger.log("Attempting to stop current script and start tmp's Ten QP");
			Thread t1 = new Thread(questStarter);
			t1.start();
			return -1;
    	}
    	else
    	{
    		if(veronicaTile.getArea(5).contains(p.l))
        	{
        		if(Dialogues.inDialogue())
        		{
        			if(Dialogues.areOptionsAvailable())
        			{
        				Dialogues.chooseOption("Yes.");
        			}
        			else if(Dialogues.canContinue())
        			{
        				Dialogues.continueDialogue();
        			}
        			else if(!Dialogues.isProcessing())
        			{
        				Logger.log("Error in dialogue handler of quest starting for tmps 10 qp fuck up");
        			}
        		}
        		else
        		{
        			if(!p.l.isMoving() || (p.l.isMoving() && 
        					p.l.getInteractingCharacter() != null &&
        					!p.l.getInteractingCharacter().getName().contains("Veronica")))
        			{
        				NPC veronica = NPCs.closest("Veronica");
        				if(veronica == null || 
        						!veronica.canReach() || 
        						veronica.distance() >= 8)
        				{
        					if(Walking.shouldWalk())
        					{
        						Walking.walk(veronicaTile);
        					}
        				}
        				else
        				{
        					veronica.interact("Talk-to");
        					Sleep.sleepUntil(Dialogues::inDialogue, 5000);
        				}
        			}
        		}
        	}
        	else
        	{
        		if(Walking.shouldWalk())
    			{
    				Walking.walk(veronicaTile);
    			}
        	}
    	}
    	
		return Sleepz.calculate(420,1111);
    }
}

