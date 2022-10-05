package script;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalDateTime;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.script.*;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Timer;
import script.behaviour.*;
import script.framework.Tree;
import script.paint.*;
import script.unlock.idle.Idle;
import script.unlock.quests.Questing;
import script.unlock.skills.melee.*;
import script.unlock.skills.skills.Chop_n_Burn;
import script.unlock.skills.skills.Fish_n_Cook;
import script.unlock.skills.skills.Mine_n_Smelt_n_Smith;
import script.utilities.API;
import script.utilities.Sleepz;


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
    public void onStart(String[] args)
    {
    	Logger.log("Trade unlocker starting!");
    	timer = new Timer(2000000000);
    	Keyboard.setWordsPerMinute(150);
    	Sleepz.dt = LocalDateTime.now();
        API.runTimer = new Timer();
        instantiateTree();
    }
    @Override
    public void onStart()
    {
    	Logger.log("Trade unlocker starting!");
    	timer = new Timer(2000000000);
    	Keyboard.setWordsPerMinute(150);
    	Sleepz.dt = LocalDateTime.now();
        API.runTimer = new Timer();
        instantiateTree();
    }

    private final Tree tree = new Tree();
    
    private void instantiateTree() {
        tree.addBranches(
        		new WaitForLogged_N_Loaded(),
        		new p(),
    			new Initialize(),
    			new ContinueDialogue(),
    			new DecisionLeaf(),
    			new Chop_n_Burn(),
    			new Fish_n_Cook(),
    			new Mine_n_Smelt_n_Smith(),
    			new TrainMelee().addLeafs(
        				new DropItemsBuryBones(),
        				new GetBestWeapon(),
        				new HandleAttackStyles(),
        				new DetectPlayer(),
        				new AttackChickens(),
        				new AttackCows()),
    			new Idle(),
    			new Questing());
    }
    @Override
    public int onLoop()
    {
        return tree.onLoop();
    }
    public static String customPaintText1 = "";
    public static String customPaintText2 = "";
    public static String customPaintText3 = "";
    // Our paint info
    // Add new lines to the paint here
    @Override
    public String[] getPaintInfo()
    {
    	return new String[] {
    			getManifest().name() +" "+ getManifest().version() + " by Dreambotter420 ^_^",
                "Current Branch: " + API.currentBranch,
                "Current Leaf: " + API.currentLeaf,
                "Time until next task switch: " + (DecisionLeaf.taskTimer != null ? Timer.formatTime(DecisionLeaf.taskTimer.remaining()) : "N/A"),
                customPaintText1,
                customPaintText2,
                customPaintText3
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
