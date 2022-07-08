package script.utilities;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.dreambot.api.Client;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.methods.worldhopper.WorldHopper;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;

public class MissingAPI {
	
	public static boolean invyContainsAnythingOtherThan(int... itemIDs)
	{
		if(Inventory.isEmpty()) return false;
		for(Item i : Inventory.all())
		{
			if(i == null) continue;
			int id = i.getID();
			if(id == -1) continue;
			boolean listed = false;
			for(int itemID : itemIDs)
			{
				if(id == itemID) 
				{
					listed = true;
				}	
			}
			if(!listed)
			{
				MethodProvider.log("Found illegal item name in inventory (not allowed in list check): " + i.getName());
				return false;
			}
		}
		//at this point all items in inventory either equal -1 or matched item list
		return false;	
	}
	public static void scrollHopWorld(int world)
	{
		if(Players.localPlayer().isInCombat() ||
				Worlds.getCurrentWorld() == world) return;
		Timer timeout = new Timer(Sleep.calculate(18000, 5555));
		while(Worlds.getCurrentWorld() != world && 
				!timeout.finished() && 
				Client.isLoggedIn() && 
				!Players.localPlayer().isInCombat() && 
				Skills.getRealLevel(Skill.HITPOINTS) > 0)
		{
			if(Widgets.getWidgetChild(69,2) != null &&
					Widgets.getWidgetChild(69,2).isVisible() &&
					Widgets.getWidgetChild(69,2).getText().contains("Loading..."))
			{
				Sleep.sleep(100, 1111);
				continue;
			}
			if(Widgets.getWidgetChild(182, 7) != null &&
					Widgets.getWidgetChild(182, 7).isVisible() &&
					Widgets.getWidgetChild(182, 7).getText().contains("World Switcher"))
			{
				Widgets.getWidgetChild(182, 3).interact("World Switcher");
				Sleep.sleep(100, 1111);
				continue;
			}
			if(!Tabs.isOpen(Tab.LOGOUT))
			{
				Tabs.open(Tab.LOGOUT);
				Sleep.sleep(100, 1111);
				continue;
			}
			//worlds are now loaded
			if(Widgets.getWidgetChild(69,2) != null &&
					Widgets.getWidgetChild(69,2).isVisible() &&
					Widgets.getWidgetChild(69,2).getText().contains("Current world - "))
			{
				//establish correct WidgetChild of desired world's clickable bar
				int gc = -1;
				for(WidgetChild w : Widgets.getWidgetChildrenContainingText(Integer.toString(world)))
				{
					if(w.getX() == 563) // correct x position lineup for world number location **FIXED MODE**
					{
						gc = (w.getIndex() - 2);
						break;
					}
				}
				WidgetChild worldWidget = null;
				if(gc >= 0) worldWidget = Widgets.getWidgetChild(69,17,gc);
				if(worldWidget == null) 
				{
					Sleep.sleep(100, 111);
					continue;
				}
				Rectangle worldRectangle = worldWidget.getRectangle();
				WidgetChild worldListContainer = Widgets.getWidgetChild(69,17);
				if(worldListContainer == null)
				{
					Sleep.sleep(100, 111);
					continue;
				}
				if(worldRectangle.intersects(Widgets.getWidgetChild(69,15).getRectangle()))
				{
					//World widget is visible - clicking it
					Rectangle visibleWorldRectangle = worldRectangle.intersection(Widgets.getWidgetChild(69,15).getRectangle());
					Mouse.click(visibleWorldRectangle);
				}
				else
				{
					//World list needs scrolling
					double yPos = worldRectangle.getCenterY();
					double yMin = worldListContainer.getRectangle().getMinY();
					double yMax = worldListContainer.getRectangle().getMaxY();
					double offsetRatio = ((yPos - yMin) / (yMax - yMin));
					WidgetChild scrollContainer = Widgets.getWidgetChild(69,18,0);
					if(scrollContainer == null)
					{
						Sleep.sleep(100, 111);
						continue;
					}
					double yScrollMin = scrollContainer.getRectangle().getMinY();
					int xRand = (int) Calculations.random(scrollContainer.getRectangle().getMinX(), scrollContainer.getRectangle().getMaxX());
					int yClickPos = (int) ((scrollContainer.getHeight() * offsetRatio) + yScrollMin);
					Mouse.click(new Point(xRand,yClickPos));
				}
				Sleep.sleep(111, 1111);
				continue;
			}
			Sleep.sleep(111, 1111);
		}
	}
	public static void hopWorld(int world)
	{
		if(Players.localPlayer().isInCombat() ||
				Worlds.getCurrentWorld() == world) return;
		Timer timeout = new Timer(Sleep.calculate(8888, 1111));
		while(Worlds.getCurrentWorld() != world || 
				!Client.isLoggedIn())
		{
			if(timeout.finished()) break;
			WorldHopper.hopWorld(world);
			Sleep.sleep(10, 111);
		}
	}
	public static boolean invyContainsAnythingOtherThan(List<Integer> itemIDs)
	{
		if(Inventory.isEmpty()) return false;
		else 
		{
			for(Item i : Inventory.all())
			{
				int id = i.getID();
				MethodProvider.log("Found itemID: " + id);
				if(id == -1) continue;
				boolean found = false;
				for(int itemID : itemIDs)
				{
					if(id == itemID) found = true;
				}
				if(!found) return true;
			}
		}
		//at this point all items in inventory either equal -1 or matched item list
		return false;	
	}
	public static List<Player> getAllPlayersInteractingWith(Player player)
	{
		if(player != null)
		{
			List<Player> targetedBy = new ArrayList<Player>();
			for(Player p : Players.all())
			{
				if(p.isInteracting(player))
				{
					targetedBy.add(p);
				}
			}
			return targetedBy;
		}
		return null;
	}
	public static List<NPC> getAllNPCsInteractingWith(Player player)
	{
		if(player != null)
		{
			List<NPC> targetedBy = new ArrayList<NPC>();
			for(NPC p : NPCs.all())
			{
				if(p.isInteracting(player))
				{
					targetedBy.add(p);
				}
			}
			return targetedBy;
		}
		return null;
	}
	public static List<Player> getAllPlayersInteractingWith(NPC npc)
	{
		if(npc != null)
		{
			List<Player> targetedBy = new ArrayList<Player>();
			for(Player p : Players.all())
			{
				if(p.isInteracting(npc))
				{
					targetedBy.add(p);
				}
			}
			return targetedBy;
		}
		return null;
	}
	public static List<NPC> getAllNPCsInteractingWith(NPC npc)
	{
		if(npc != null)
		{
			List<NPC> targetedBy = new ArrayList<NPC>();
			for(NPC p : NPCs.all())
			{
				if(p.isInteracting(npc))
				{
					targetedBy.add(p);
				}
			}
			return targetedBy;
		}
		return null;
	}
	public static boolean isInteractedByNPC()
	{
		List<NPC> targetedBy = getAllNPCsInteractingWith(Players.localPlayer());
		for(NPC npc : targetedBy)
		{
			if(npc != null)
			{
				return true;
			}
		}
		return false;
	}
	public static boolean isInteracting()
	{
		return Players.localPlayer().getInteractingCharacter() != null;
	}
	public static boolean isInCombat()
	{
		return isInteracting() || isInteractedByNPC();
	}
	public static boolean talkToNPC(String NPC)
	{
		NPC npc = NPCs.closest(NPC);
		if(npc == null) 
		{
			MethodProvider.log("NPC: \""+NPC+"\" not found in TalkToNPC call");
			return false;
		}
		else
		{
			if(npc.getSurroundingArea(1).canReach())
			{
				npc.interact("Talk-to");
				Sleep.sleep(666,111);
				MethodProvider.sleepUntil(
						() -> Dialogues.inDialogue(),
						Sleep.calculate(5555,1111));
				Sleep.sleep(111,1111);
			}
			else 
			{
				Walking.walk(npc.getTile());
			}
			
			return true;
		}
	}
	public static boolean inventoryOnlyContains(int...IDs)
	{
		if(Inventory.isEmpty()) return false;
		for(Item i : Inventory.all())
		{
			int j = i.getID();
			if(j == -1) continue;
			boolean found = false;
			for(int k : IDs)
			{
				if (j == k)
				{
					found = true;
				}
			}
			if(!found) return false;
		}
		return true;
	}
	public static boolean isInteractedByAnotherPlayer()
	{
		List<Player> targetedBy = getAllPlayersInteractingWith(Players.localPlayer());
		for(Player player : targetedBy)
		{
			if(player != null)
			{
				return true;
			}
		}
		return false;
	}
	public static boolean isInteracting(NPC toCheck) {
		return toCheck.getInteractingCharacter() != null;
	}
	public static boolean isInteracting(Player toCheck) {
		return toCheck.getInteractingCharacter() != null;
	}
	public static boolean isInteractedByAnotherPlayerThanMe(Player playerToCheck)
	{
		List<Player> targetedBy = getAllPlayersInteractingWith(playerToCheck); 
		for(Player player : targetedBy)
		{
			if(player != null && !player.equals(Players.localPlayer()))
			{
				return true;
			}
		}
		return false;
	}
	public static boolean isInteractedByAnotherPlayerThanMe(NPC npcToCheck)
	{
		List<Player> targetedBy = getAllPlayersInteractingWith(npcToCheck);
		for(Player player : targetedBy)
		{
			if(player != null && !player.equals(Players.localPlayer()))
			{
				return true;
			}
		}
		return false;
	}

	private static boolean isInteractedByAnotherPlayer(NPC toCheck) {
		List<Player> targetedBy = getAllPlayersInteractingWith(toCheck);
		for(Player player : targetedBy)
		{
			if(player != null)
			{
				return true;
			}
		}
		return false;
	}
	public static boolean isInteractedByAnotherNPC(Player playerToCheck)
	{
		List<NPC> targetedBy = getAllNPCsInteractingWith(playerToCheck);
		for(NPC NPC : targetedBy)
		{
			if(NPC != null)
			{
				return true;
			}
		}
		return false;
	}
	public static boolean isInteractedByAnotherPlayer(Player playerToCheck)
	{
		List<Player> targetedBy = getAllPlayersInteractingWith(playerToCheck);
		for(Player player : targetedBy)
		{
			if(player != null)
			{
				return true;
			}
		}
		return false;
	}
	public static boolean isInteractedByAnotherNPC(NPC npcToCheck)
	{
		List<NPC> targetedBy = getAllNPCsInteractingWith(npcToCheck);
		for(NPC NPC : targetedBy)
		{
			if(NPC != null)
			{
				return true;
			}
		}
		
		return false;
	}
	public static boolean isPlayerInteractedByAnythingOtherThanMe(Player toCheck)
	{
		return isInteractedByAnotherNPC(toCheck) || isInteractedByAnotherPlayerThanMe(toCheck); 
	}
	public static boolean isNPCInteractedByAnythingOtherThanMe(NPC toCheck)
	{
		return isInteractedByAnotherNPC(toCheck) || isInteractedByAnotherPlayerThanMe(toCheck); 
	}
	public static boolean isNPCOccupiedInSingles(NPC toCheck)
	{
		return !Combat.isInMultiCombat() && 
				isNPCInteractedByAnythingOtherThanMe(toCheck) &&
				isInteracting(toCheck);
	}
	public static boolean isPlayerOccupiedInSingles(Player toCheck)
	{
		return !Combat.isInMultiCombat() && !isPlayerInteractedByAnythingOtherThanMe(toCheck) &&
				isInteracting(toCheck);
	}
	public static boolean isInteractedWith() //no argument assumes from local player
	{
		return isInteractedByAnotherPlayer(Players.localPlayer()) || isInteractedByAnotherNPC(Players.localPlayer());
	}
	public static boolean isInteractedWith(NPC toCheck)
	{
		return isInteractedByAnotherPlayer(toCheck) || isInteractedByAnotherNPC(toCheck);
	}
	public static boolean isInteractedWith(Player toCheck) 
	{
		return isInteractedByAnotherPlayer(toCheck) || isInteractedByAnotherNPC(toCheck);
	}
}
