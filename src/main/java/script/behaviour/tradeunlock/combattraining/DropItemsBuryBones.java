package script.behaviour.tradeunlock.combattraining;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;

import script.framework.Leaf;
import script.utilities.Sleep;

public class DropItemsBuryBones extends Leaf {
    @Override
    public boolean isValid() {
        return Inventory.contains("Cowhide", "Beef", "Egg", "Raw chicken") || 
        		 Inventory.contains("Bones");
    }


    @Override
    public int onLoop() {
    	if(Players.localPlayer().isInCombat())
    	{
    		MethodProvider.sleep((int)Calculations.nextGaussianRandom(666,111));
    		return Sleep.calculate(111,1111);
    	}
    	if(Inventory.contains("Bones") && 
    			Inventory.get("Bones").interact("Bury"))
    	{
    		MethodProvider.sleep((int)Calculations.nextGaussianRandom(666,111));
    	}
    	if(Inventory.contains("Cowhide", "Beef", "Egg", "Raw chicken") &&
    			Inventory.dropAll("Cowhide", "Beef", "Egg", "Raw chicken"))
    	{
    		MethodProvider.sleep((int)Calculations.nextGaussianRandom(666,111));
    	}
        return Sleep.calculate(111,1111);
    }
}
