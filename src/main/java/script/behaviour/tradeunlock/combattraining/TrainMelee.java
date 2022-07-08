package script.behaviour.tradeunlock.combattraining;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;

import script.framework.Root;
import script.utilities.API;
import script.utilities.API.modes;
import script.utilities.Sleep;

public class TrainMelee extends Root {
    @Override
    public boolean isValid() {
    	if(Dialogues.canContinue())
    	{
    		Dialogues.continueDialogue();
    		Sleep.sleep(420,696);
    	}
    	//start questing if not already
    	if(API.unlockMode == API.modes.TRAIN_COMBAT && ((Skills.getRealLevel(Skill.ATTACK) >= API.attEndpoint && 
    			Skills.getRealLevel(Skill.STRENGTH) >= API.strEndpoint && 
    			Skills.getRealLevel(Skill.DEFENCE) >= API.defEndpoint) || 
    			(Skills.getRealLevel(Skill.PRAYER) == 1 && Skills.getTotalLevel() >= 92) ||
    			Skills.getTotalLevel() >= 100))
        {
        	MethodProvider.log("got Setting unlock mode to IDLE");
    		API.unlockMode = API.modes.IDLE;
        }
		return API.unlockMode == API.modes.TRAIN_COMBAT;
    }
}
