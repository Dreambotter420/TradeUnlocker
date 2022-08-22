package script.unlock.skills.melee;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.combat.CombatStyle;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;

import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Sleep;

public class HandleAttackStyles  extends Leaf {
	public static boolean trainStr = false;
    public static boolean trainAtt = false;
	public static boolean trainDef = false;
    
	@Override
    public boolean isValid() {
        
		if(Skills.getRealLevel(Skill.STRENGTH) < API.strMidpoint)
        {
			if(Combat.getCombatStyle() != CombatStyle.STRENGTH)
			{
				trainStr = true;
				trainDef = false;
				trainAtt = false;
				return true;
			}
			return false;
        }
		if(Skills.getRealLevel(Skill.ATTACK) < API.attMidpoint)
		{
			if(Combat.getCombatStyle() != CombatStyle.ATTACK)
			{
				trainAtt = true;
				trainDef = false;
				trainStr = false;
				return true;
			}
			return false;
		}
		if(Skills.getRealLevel(Skill.DEFENCE) < API.defMidpoint)
		{
			if(Combat.getCombatStyle() != CombatStyle.DEFENCE)
			{
				trainDef = true;
				trainStr = false;
				trainAtt = false;
				return true;
			}
			return false;
		}
		if(Skills.getRealLevel(Skill.STRENGTH) < API.strEndpoint)
        {
			if(Combat.getCombatStyle() != CombatStyle.STRENGTH)
			{
				trainStr = true;
				trainDef = false;
				trainAtt = false;
				return true;
			}
			return false;
        }
		if(Skills.getRealLevel(Skill.ATTACK) < API.attEndpoint)
		{
			if(Combat.getCombatStyle() != CombatStyle.ATTACK)
			{
				trainAtt = true;
				trainDef = false;
				trainStr = false;
				return true;
			}
			return false;
		}
		if(Skills.getRealLevel(Skill.DEFENCE) < API.defEndpoint)
		{
			if(Combat.getCombatStyle() != CombatStyle.DEFENCE)
			{
				trainDef = true;
				trainStr = false;
				trainAtt = false;
				return true;
			}
			return false;
		}
		trainDef = false;
		trainStr = false;
		trainAtt = false;
    	return false;
    }
    @Override
    public int onLoop() {
    	if(trainStr) Combat.setCombatStyle(CombatStyle.STRENGTH);
    	else if(trainAtt) Combat.setCombatStyle(CombatStyle.ATTACK);
    	else if(trainDef) Combat.setCombatStyle(CombatStyle.DEFENCE);
    	MethodProvider.sleep((int)Calculations.nextGaussianRandom(666,111));
		return Sleep.calculate(111,1111);
    }
}

