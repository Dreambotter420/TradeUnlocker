package script.unlock.skills.melee;

import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.wrappers.interactive.Player;

import script.p;
import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Locations;
import script.utilities.MissingAPI;
import script.utilities.Sleepz;

public class DetectPlayer extends Leaf {
    @Override
    public boolean isValid() {
    	if(!Locations.chosenCHICKENS.contains(p.l) && 
    			!Locations.LARGE_COWPEN.contains(p.l)) return false;
    	boolean chickens = (Locations.LARGE_COWPEN.contains(p.l) ? false : true);
    	int tmp = 0;
    	for(Player p2 : Players.all())
    	{
    		if(!chickens)
    		{
    			if(p2 != null && !p2.equals(p.l) && Locations.LARGE_COWPEN.contains(p2)) tmp++;
    		}
    		else
    		{
    			if(p2 != null && !p2.equals(p.l) && Locations.chosenCHICKENS.contains(p2)) 
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
    	Logger.log("Hopping to random world "+world+" after detecting >= 3 players in area");
    	MissingAPI.scrollHopWorld(world);
        return Sleepz.calculate(50,555);
    }
}
