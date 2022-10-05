package script.unlock.idle;

import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.utilities.Timer;

import script.p;
import script.framework.Leaf;
import script.utilities.API;
import script.utilities.Sleepz;

public class Idle  extends Leaf {
    public final static Tile idleTile = new Tile(3420, 3185, 1);
	@Override
    public boolean isValid() {
    	return API.unlockMode == API.modes.IDLE;
    }
	public static boolean doneIdling = false;
	public static boolean checkedTime = false;
	public static Timer timeCheckTimer = new Timer(0);
    @Override
    public int onLoop() {
    	if(Dialogues.areOptionsAvailable())
		{
			Dialogues.chooseOption("Yes and don\'t ask me again");
		}
    	if(timeCheckTimer.finished())
    	{
    		checkedTime = false;
    	}
    	if(!checkedTime)
    	{
    		checkTimePlayed();
    	}
    	if(checkedTime)
    	{
    		if(API.gameTimeHours < 20)
    		{
    			//need to go to idle tile and wait
    			if(idleTile.getArea(2).contains(p.l))
    			{
    				if(API.gameTimeHours < 19)
    		    	{
    		    		if(Calculations.nextGaussianRandom(666,222) > 276)
    		        	{
    		        		if(!Tabs.isOpen(Tab.SKILLS))
    		        		{
    		        			Tabs.open(Tab.SKILLS);
    		        			Sleepz.sleep(555, 4444);
    		        		}
    		        		if(Tabs.isOpen(Tab.SKILLS))
    		        		{
    		        			int childID = 0;
    		        			int rand = Calculations.random(0, 15);
    		        			switch(rand)
    		        			{
    		        			case(0): childID=1; break;
    		        			case(1): childID=2; break;
    		        			case(2): childID=3; break;
    		        			case(3): childID=4; break;
    		        			case(4): childID=5; break;
    		        			case(5): childID=6; break;
    		        			case(6): childID=7; break;
    		        			case(7): childID=9; break;
    		        			case(8): childID=13; break;
    		        			case(9): childID=17; break;
    		        			case(10): childID=18; break;
    		        			case(11): childID=19; break;
    		        			case(12): childID=20; break;
    		        			case(13): childID=21; break;
    		        			case(14): childID=22; break;
    		        			}
    		        			if(childID > 0)
    		        			{
    		        				if(Widgets.getWidgetChild(320,childID) != null)
    		        				{
    		        					Mouse.move(Widgets.getWidgetChild(320,childID).getRectangle());
    		        					int secs = (int) Calculations.nextGaussianRandom(200000, 42000);
    									Logger.log("Reset idle by hovering a skills icon, waiting for another " + ((int) (secs / 1000)) + " seconds");
    									Sleep.sleep(secs);
    		        				}
    		        			}
    		        		}
    		        	}
    		    		//low chance of opening up time played
    		    		else
    		    		{
    		    			checkTimePlayed();
    		    		}
    		    	}
    				else
    				{
    					checkTimePlayed();
    				}
    			}
    			else if(Walking.shouldWalk())
    			{
    				Walking.walk(idleTile);
    			}
    		}
    		//we have achieved at least 20 hours gametime
    		else
    		{
    			API.unlockMode = null;
    			doneIdling = true;
    			Logger.log("All done getting 20 hours!");
    		}
    	}
		return Sleepz.calculate(420,1111);
    }
    
    public static void checkTimePlayed()
    {
    	if(!Tabs.isOpen(Tab.QUEST)) 
    	{
    		Tabs.open(Tab.QUEST);
    		Sleep.sleepUntil(() -> Tabs.isOpen(Tab.QUEST), Sleepz.calculate(2000, 2222));
    		Sleepz.sleep(69, 420);
    	}
		if(Tabs.isOpen(Tab.QUEST))
		{
			//need to switch to Character Summary mini-tab
			if(PlayerSettings.getConfig(1141) != 0)
			{
				if(Widgets.getWidgetChild(629,7) != null &&
						Widgets.getWidgetChild(629,7).isVisible()) 
				{
					if(Widgets.getWidgetChild(629,7).interact("Character Summary")) 
					{
						Sleep.sleepUntil(() -> PlayerSettings.getConfig(1141) == 0, Sleepz.calculate(2222, 1111));
						Sleepz.sleep(69, 420);
					}
					else Logger.log("did not click Character Summary minitab while Quest tab open");
				}
			}
			if(PlayerSettings.getConfig(1141) == 0)
			{
				//cannot see time played, click to reveal
				if(PlayerSettings.getBitValue(12933) == 0)
				{
					if(Widgets.getWidgetChild(712, 2 , 100) != null &&
							Widgets.getWidgetChild(712, 2 , 100).isVisible()) 
					{
						if(Widgets.getWidgetChild(712, 2 , 100).interact("Reveal"))
						{
							Sleep.sleepUntil(() -> PlayerSettings.getBitValue(12933) != 0, Sleepz.calculate(2000, 2332));
							Sleepz.sleep(69, 420);
						}
						else Logger.log("Did not click reveal mask button while character Summary minitab open");
					}
				}
				if(PlayerSettings.getBitValue(12933) != 0)
				{
					boolean clicked = false;
					if(Widgets.getWidgetChild(629,7) != null &&
							Widgets.getWidgetChild(629,7).isVisible()) 
					{
						if(Widgets.getWidgetChild(629,7).interact("Character Summary"))
						{
							Sleepz.sleep(1231, 1234);
							clicked = true;
						}
					}
					else
					{
						Logger.log("Something wrong clicking Character Summary minitab while idling to refresh time");
					}
					if(clicked &&
							Widgets.getWidgetChild(712, 2 , 100) != null &&
							Widgets.getWidgetChild(712, 2 , 100).isVisible())
					{
						int gameTimeHours = 0;
						String timeplayed = Widgets.getWidgetChild(712,2,100).getText().toLowerCase();
						String[] t1 = timeplayed.split(">", 2); //t1[1] = text after <color=shit>
						String[] t2 = t1[1].split("<", 2); //t2[0] = text before </col>
						timeplayed = t2[0];
						if(timeplayed.contains("day"))
						{//Time played is at least one day and dont need to wait any longer
							gameTimeHours = 24;
						} 
						else 
						{
						    if(timeplayed.contains("hour")) {
						        t1 = timeplayed.split(" hour", 2);
						        gameTimeHours = Integer.parseInt(t1[0]);
						    } 
						    else
						    {//Time played is less than one hour and need to wait
						        gameTimeHours = 0;
						    }
						}
						API.gameTimeHours = gameTimeHours;
						Logger.log("Checked gametime hours, and have: " + API.gameTimeHours + " gametime hours");
						checkedTime = true;
						if(gameTimeHours >= 20) 
						{
							Logger.log("Have at least 20 hours!");
							return;
						}
						int secs = (int) Calculations.nextGaussianRandom(200000, 42000);
						Logger.log("Also reset idle by clicking time played icon, waiting for another " + ((int) (secs / 1000)) + " seconds");
						int timerSecs = (int) Calculations.nextGaussianRandom(4200000, 300000);
						Logger.log("Also... reset timer until next forcecheck to " + ((int) (timerSecs / 60000)) + " minutes");
						timeCheckTimer = new Timer(timerSecs);
						
						Sleep.sleep(secs);
					}
					else
					{
						Logger.log("Something wrong getting text of Time Played while character Summary minitab open");
					}
				}
			}
		}
    }
}

