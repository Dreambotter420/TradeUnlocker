package script.unlock.skills.melee;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.utilities.Sleep;

import script.framework.Leaf;
import script.utilities.Sleepz;

import script.p;
public class DropItemsBuryBones extends Leaf {
    @Override
    public boolean isValid() {
        return Inventory.contains("Cowhide", "Beef", "Egg", "Raw chicken") || 
        		 Inventory.contains("Bones");
    }


    @Override
    public int onLoop() {
    	if(p.l.isInCombat())
    	{
    		Sleep.sleep((int)Calculations.nextGaussianRandom(666,111));
    		return Sleepz.calculate(111,1111);
    	}
    	if(Inventory.contains("Bones") && 
    			Inventory.get("Bones").interact("Bury"))
    	{
    		Sleep.sleep((int)Calculations.nextGaussianRandom(666,111));
    	}
    	if(Inventory.contains("Cowhide", "Beef", "Egg", "Raw chicken") &&
    			Inventory.dropAll("Cowhide", "Beef", "Egg", "Raw chicken"))
    	{
    		Sleep.sleep((int)Calculations.nextGaussianRandom(666,111));
    	}
        return Sleepz.calculate(111,1111);
    }
}
