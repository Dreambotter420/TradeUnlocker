package script.behaviour;

import java.time.LocalTime;

import org.dreambot.api.utilities.Logger;

import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Locations;
import script.utilities.Sleepz;


public class Initialize extends Leaf {

	 @Override
	 public boolean isValid() 
	 {
	    return !API.initialized;
	 }
   
    @Override
    public int onLoop() {
    	API.rand2.setSeed(LocalTime.now().getNano());
    	Sleepz.initSleepMod = 1.2 + (API.rand2.nextDouble()/1.25);
    	Sleepz.initSleepMod = Sleepz.initSleepMod * Sleepz.initSleepMod;
    	//all initial randomizations that depend on new random seed go here
    	API.randomizeSkillSetpoints();
    	Locations.chooseLocations();
    	Logger.log("Initialized");
		API.initialized = true;
        return 5;
    }
}
