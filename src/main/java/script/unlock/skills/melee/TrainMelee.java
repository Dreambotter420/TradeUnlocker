package script.unlock.skills.melee;

import script.framework.Root;
import script.utilities.API;

public class TrainMelee extends Root {
    @Override
    public boolean isValid() {
		return API.unlockMode == API.modes.TRAIN_COMBAT;
    }
}
