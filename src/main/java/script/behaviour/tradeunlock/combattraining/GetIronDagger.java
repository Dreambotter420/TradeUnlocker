package script.behaviour.tradeunlock.combattraining;

import java.util.List;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.methods.worldhopper.WorldHopper;
import org.dreambot.api.wrappers.items.GroundItem;

import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Sleep;

public class GetIronDagger extends Leaf {

    @Override
    public boolean isValid() {
    	return Equipment.getItemInSlot(EquipmentSlot.WEAPON) == null;
    }
    private final Tile ironDaggerTile = new Tile(3248, 3245, 0);
    private final int ironDaggerID = 1203;
    @Override
    public int onLoop() {
    	//open inventory tab first
    	if(!Tabs.isOpen(Tab.INVENTORY)) Tabs.open(Tab.INVENTORY);
    	else
    	{
    		//equip irondagger if have one
    		if(Inventory.contains(ironDaggerID)) Inventory.interact(ironDaggerID, "Wield");
    		else
    		{
    			if(ironDaggerTile.distance() > 8 || 
    					!ironDaggerTile.canReach()) 
    			{
    				if(Walking.shouldWalk())
    				{
    					Walking.walk(ironDaggerTile);
    				}
    			}
    			else
    			{
    				if(Players.localPlayer().isMoving()) return Sleep.calculate(666,1111);
    				List<GroundItem> items = GroundItems.getForTile(ironDaggerTile);
    				for(GroundItem g : items)
    				{
    					if(g != null && g.getID() == ironDaggerID) 
    					{
    						g.interact("Take");
    						return Sleep.calculate(666,1111);
    					}
    				}
    				Widgets.closeAll();
    		    	int world = API.getF2PWorld();
    		    	MethodProvider.log("Hopping to random world after detecting no iron dagger");
    		    	for(int i = 1; i < 10; i++)
    		    	{
    		    		if(Worlds.getCurrentWorld() == world) break;
    		    		MethodProvider.log("Trying to hop to world: " + world + " attempt #" + i);
    		    		WorldHopper.hopWorld(world);
    		    		Sleep.sleep(666, 666);
    		    	}
    		    	
    		        return Sleep.calculate(50,555);
    			}
    		}
    	}
    	
        return Sleep.calculate(111,111);
    }

}