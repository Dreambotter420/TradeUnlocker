package script.unlock.skills.melee;

import org.dreambot.api.input.Mouse;
import script.p;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;

import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Locations;
import script.utilities.MissingAPI;
import script.utilities.Sleepz;
import script.utilities.Walkz;

public class AttackCows extends Leaf  {
    @Override
    public boolean isValid() {
        return Skills.getRealLevel(Skill.ATTACK) >= API.attMidpoint && 
                Skills.getRealLevel(Skill.STRENGTH) >= API.strMidpoint && 
                Skills.getRealLevel(Skill.DEFENCE) >= API.defMidpoint &&
        		((Skills.getRealLevel(Skill.ATTACK) < API.attEndpoint) || 
        		(Skills.getRealLevel(Skill.STRENGTH) < API.strEndpoint) || 
        		(Skills.getRealLevel(Skill.DEFENCE) < API.defEndpoint));
    }
    
    @Override
    public int onLoop() {
    	Sleepz.sleep(69,420);
        if (Walkz.walkToArea(Locations.LARGE_COWPEN, Locations.LARGE_COWPEN_WALKABLE))
        {
        	NPC cow = NPCs.closest(q -> q != null &&
        			Locations.LARGE_COWPEN.contains(q) &&
            		q.getHealthPercent() > 0 &&
            		(q.getName().equals("Cow") || q.getName().equals("Cow calf") ||q.getName().equals("Goblin")) &&
            		!MissingAPI.isInteractedByAnotherPlayerThanMe(q) &&
            		q.canReach() &&
            		q.hasAction("Attack"));
        	if (cow != null) 
        	{
                if (!MissingAPI.isInCombat()) {
                	Sleepz.sleep(111,3333);
                	if (cow.interact("Attack")) 
                	{
                		Sleep.sleep((int)Calculations.nextGaussianRandom(666,222));
                		if(Calculations.nextGaussianRandom(555,111) < 450)
                		{
                			Mouse.moveMouseOutsideScreen();
                		}
                    }
                } 
                else 
                {
                    Sleep.sleepWhile(() -> p.l.isAnimating(), () -> p.l.isAnimating(),5000,50);
                }
           }
        }
        return Sleepz.calculate(111,1111);
    }
}
