package script.behaviour.tradeunlock.combattraining;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.interactive.Player;

import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Locations;
import script.utilities.MissingAPI;
import script.utilities.Sleep;

public class DetectPlayer extends Leaf {
    @Override
    public boolean isValid() {
    	if(!Locations.chosenCHICKENS.contains(Players.localPlayer()) && 
    			!Locations.LARGE_COWPEN.contains(Players.localPlayer())) return false;
    	int tmp = 0;
    	for(Player p : Players.all())
    	{
    		if(Locations.LARGE_COWPEN.contains(Players.localPlayer()))
    		{
    			if(p != null && !p.equals(Players.localPlayer()) && Locations.LARGE_COWPEN.contains(p)) tmp++;
    		}
    		else if(Locations.chosenCHICKENS.contains(Players.localPlayer()))
    		{
    			if(p != null && !p.equals(Players.localPlayer()) && Locations.chosenCHICKENS.contains(p)) 
    			{
    				tmp++;tmp++;
    			}
    				
    		}
    	}
    	if(tmp >= 8) return true; //hop worlds if there are 3 players in your pen or 6 players in large cowpen
    	return false;
    }

    @Override
    public int onLoop() {
    	Widgets.closeAll();
    	int world = API.getF2PWorld();
    	MethodProvider.log("Hopping to random world after detecting >= 3 players in area");
    	MissingAPI.scrollHopWorld(world);
        return Sleep.calculate(50,555);
    }
}
