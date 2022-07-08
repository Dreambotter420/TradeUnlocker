package script;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalDateTime;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.*;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Timer;
import script.behaviour.*;
import script.behaviour.initialization.*;
import script.behaviour.tradeunlock.Idle;
import script.behaviour.tradeunlock.combattraining.*;
import script.behaviour.tradeunlock.questing.Questing;
import script.framework.Tree;
import script.paint.*;
import script.utilities.API;
import script.utilities.Sleep;


@ScriptManifest(
		name = "TradeUnlocker", 
		author = "Dreambotter420", 
		description = "Kills chikken and cow. Waits. Starts tmp's 10 QP.",
		version = 420.69,
		category = Category.MISC, 
		image = "lFwvTuW.jpg")
public class Main extends AbstractScript implements PaintInfo, ChatListener
{
    public static Timer timer;
    @Override
    public void onStart()
    {
    	MethodProvider.log("Trade unlocker starting!");
    	timer = new Timer(2000000000);
    	Keyboard.setWordsPerMinute(150);
    	Sleep.dt = LocalDateTime.now();
        API.runTimer = new Timer();
        instantiateTree();
    }

    private final Tree tree = new Tree();
    
    private void instantiateTree() {
        tree.addBranches(
        		new WaitForLogged_N_Loaded(),
    			new Initialize(),
    			new ContinueDialogue(),
    			new Idle(),
    			new TrainMelee().addLeafs(
    				new DropItemsBuryBones(),
    				new GetIronDagger(),
    				new HandleAttackStyles(),
    				new DetectPlayer(),
    				new AttackChickens(),
    				new AttackCows()),
    			new Questing());
    }
    @Override
    public int onLoop()
    {
        if(API.unlockMode == null)
		{
    		API.unlockMode = API.modes.TRAIN_COMBAT;
    		MethodProvider.log("Setting trade unlock mode to "+API.unlockMode);
		}
        return tree.onLoop();
    }
    // Our paint info
    // Add new lines to the paint here
    @Override
    public String[] getPaintInfo()
    {
    	return new String[] {
    			getManifest().name() +" "+ getManifest().version() + " by Dreambotter420 ^_^",
                "Current Branch: " + API.currentBranch,
                "Current Leaf: " + API.currentLeaf,
        };
    }

    // Instantiate the paint object. This can be customized to your liking.
    private final CustomPaint CUSTOM_PAINT = new CustomPaint(this,
            CustomPaint.PaintLocations.BOTTOM_LEFT_PLAY_SCREEN,
            new Color[] {new Color(255, 251, 255)},
            "Trebuchet MS",
            new Color[] {new Color(50, 50, 50, 175)},
            new Color[] {new Color(28, 28, 29)},
            1, false, 5, 3, 0);
    private final RenderingHints aa = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


    @Override
    public void onPaint(Graphics2D graphics2D)
    {
        // Set the rendering hints
        graphics2D.setRenderingHints(aa);
        // Draw the custom paint
        CUSTOM_PAINT.paint(graphics2D);
    }
}
