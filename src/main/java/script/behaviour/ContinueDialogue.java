package script.behaviour;

import org.dreambot.api.methods.dialogues.Dialogues;

import script.framework.Leaf;
import script.utilities.Sleep;

public class ContinueDialogue extends Leaf {

    @Override
    public boolean isValid() {
    	return Dialogues.canContinue();
    }

    @Override
    public int onLoop() {
    	if(Dialogues.continueDialogue())
    	{
    		Sleep.sleep(666,111);
    	}
		return Sleep.calculate(111,111);
    }

}