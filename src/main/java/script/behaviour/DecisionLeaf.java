package script.behaviour;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.utilities.Timer;

import script.framework.Leaf;
import script.utilities.API;
import script.utilities.API.modes;

public class DecisionLeaf extends Leaf{

	@Override
	public boolean isValid() {
		return API.unlockMode == null;
	}
    public static Timer taskTimer;
    
    /**
     * sets a timer for random length.
     * enter 1 for short,
     * 2 for medium,
     * 3 for long,
     * 4 for guaranteed extra long.
     * Most likely will choose the chosen timer. But a chance to choose others. Except for 4. 4 will choose extra long.
     */
    public static void setTaskTimer (int priorityTime)
    {
    	switch(priorityTime) {
    	case(4):
    	{
    		int timer = (int)Calculations.nextGaussianRandom(12000000, 1200000);
    		taskTimer = new Timer(timer);
    		MethodProvider.log("Set timer for: " + ((double)timer / 60000) +" minutes");
    		break;
    	}
    	case(3):
    	{
    		int rand = (int) Calculations.nextGaussianRandom(50, 20);
    		int timer = 0;
    		if(rand < 30) timer = (int)Calculations.nextGaussianRandom(1200000, 800000);
    		else if (rand < 38) timer = (int)Calculations.nextGaussianRandom(3000000, 800000);
    		else timer = (int)Calculations.nextGaussianRandom(6000000, 1200000);
    		taskTimer = new Timer(timer);
    		MethodProvider.log("Set timer for: " + ((double)timer / 60000) +" minutes");
    		break;
    	}
    	case(2):
    	{
    		int rand = (int) Calculations.nextGaussianRandom(50, 20);
    		int timer = 0;
    		if(rand < 30) timer = (int)Calculations.nextGaussianRandom(6000000, 800000);
    		else if (rand < 38) timer = (int)Calculations.nextGaussianRandom(1200000, 800000);
    		else timer = (int)Calculations.nextGaussianRandom(3000000, 1200000);
    		taskTimer = new Timer(timer);
    		MethodProvider.log("Set timer for: " + ((double)timer / 60000) +" minutes");
    		break;
    	}
    	case(1):
    	{
    		int rand = (int) Calculations.nextGaussianRandom(50, 20);
    		int timer = 0;
    		if(rand < 30) timer = (int)Calculations.nextGaussianRandom(6000000, 800000);
    		else if (rand < 38) timer = (int)Calculations.nextGaussianRandom(3000000, 800000);
    		else timer = (int)Calculations.nextGaussianRandom(1200000, 1200000);
    		taskTimer = new Timer(timer);
    		MethodProvider.log("Set timer for: " + ((double)timer / 60000) +" minutes");
    		break;
    	}
    	default:{
    		MethodProvider.log("Whoops - enter 1,2,3 into setTaskTimer function! :-)");
    		break;
    	}}
    }
    
	@Override
	public int onLoop() {
		if(Skills.getTotalLevel() >= 100) API.unlockMode = API.modes.IDLE;
		else
		{
			modes[] validModez = {modes.CHOP,modes.MINE,modes.TRAIN_COMBAT};
			List<modes> validModes = Arrays.asList(validModez);
			if(Players.localPlayer().getLevel() <= 14)
			{
				MethodProvider.log("Not adding fish mode due to cb lvl <= 14! Wizards gon fuck witcha!");
			} else validModes.add(modes.FISH);
			Collections.shuffle(validModes);
			API.unlockMode = validModes.get(0);
		}
		MethodProvider.log("Switching mode: " + API.unlockMode.toString());
		if(API.unlockMode == modes.MINE) setTaskTimer(3);
		else setTaskTimer(2);
		return 10;
	}
}
