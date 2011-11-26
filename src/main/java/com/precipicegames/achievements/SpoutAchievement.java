package com.precipicegames.achievements;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.gui.Widget;

public interface SpoutAchievement extends Achievement{
	Widget getDisplay(Player p);
}
