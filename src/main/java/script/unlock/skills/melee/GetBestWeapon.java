package script.unlock.skills.melee;

import java.util.List;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankMode;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.items.GroundItem;
import script.framework.Leaf;
import script.utilities.API;
import script.utilities.MissingAPI;
import script.utilities.Sleepz;

import script.p;
public class GetBestWeapon extends Leaf {

    @Override
    public boolean isValid() {
    	return Equipment.getItemInSlot(EquipmentSlot.WEAPON) == null || !Equipment.getItemInSlot(EquipmentSlot.WEAPON).getName().equals(getBestWeapon());
    }
    
    public static String getBestWeapon()
    {
    	//scans equipment/inventory/bank for best applicable scimitar, returns that, otherwise returns iron dagger
    	final int att = Skills.getRealLevel(Skill.ATTACK);
    	if(att >= 40 && (Equipment.contains("Rune scimitar") || Inventory.contains("Rune scimitar") || Bank.contains("Rune scimitar"))) return "Rune scimitar";
    	if(att >= 30 && (Equipment.contains("Adamant scimitar") || Inventory.contains("Adamant scimitar") || Bank.contains("Adamant scimitar"))) return "Adamant scimitar";
    	if(att >= 20 && (Equipment.contains("Mithril scimitar") || Inventory.contains("Mithril scimitar") || Bank.contains("Mithril scimitar"))) return "Mithril scimitar";
    	if(att >= 5 && (Equipment.contains("Steel scimitar") || Inventory.contains("Steel scimitar") || Bank.contains("Steel scimitar"))) return "Steel scimitar";
    	if(Equipment.contains("Iron scimitar") || Inventory.contains("Iron scimitar") || Bank.contains("Iron scimitar")) return "Iron scimitar";
    	if(Equipment.contains("Bronze scimitar") || Inventory.contains("Bronze scimitar") || Bank.contains("Bronze scimitar")) return "Bronze scimitar";
    	return "Iron dagger";
    }
    private final Tile ironDaggerTile = new Tile(3248, 3245, 0);
    private final int ironDaggerID = 1203;
    @Override
    public int onLoop() {
    	final String bestWeapon = getBestWeapon();
    	
    	//equip if have it
		if(Inventory.contains(bestWeapon))
		{
			if(!Tabs.isOpen(Tab.INVENTORY)) Tabs.open(Tab.INVENTORY);
			if(Inventory.interact(bestWeapon, "Wield")) Sleep.sleepUntil(() -> Equipment.contains(bestWeapon), Sleepz.calculate(2222,2222));
	        return Sleepz.calculate(111,111);
		}
		if(Bank.contains(bestWeapon))
		{
			if(Bank.open(Bank.getClosestBankLocation()))
			{
				if(Bank.getWithdrawMode() != BankMode.ITEM)
				{
					Bank.setWithdrawMode(BankMode.ITEM);
					return Sleepz.calculate(420, 696);
				}
				if(Bank.withdraw(bestWeapon,1))
				{
					Sleep.sleepUntil(() -> Inventory.contains(bestWeapon), Sleepz.calculate(2222, 2222));
				}
			}
			return Sleepz.calculate(420,1111);
		}
		if(ironDaggerTile.distance() > 8 || 
				!ironDaggerTile.canReach()) 
		{
			if(Walking.shouldWalk() && Walking.walk(ironDaggerTile)) Sleepz.sleep(420, 696);
			return Sleepz.calculate(111, 111);
		}
		if(p.l.isMoving()) return Sleepz.calculate(666,1111);
		List<GroundItem> items = GroundItems.getForTile(ironDaggerTile);
		for(GroundItem g : items)
		{
			if(g != null && g.getID() == ironDaggerID) 
			{
				g.interact("Take");
				return Sleepz.calculate(666,1111);
			}
		}
		final int world = API.getF2PWorld();
		Logger.log("Hopping to world " +world+" after being in iron dagger spot but its missing!");
		MissingAPI.hopWorld(world);
        return Sleepz.calculate(50,555);
    }

}