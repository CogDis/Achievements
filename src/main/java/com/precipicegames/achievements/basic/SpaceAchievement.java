package com.precipicegames.achievements.basic;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerListener;

import com.precipicegames.achievements.Achievement;

public class SpaceAchievement implements Achievement {

	public String getTitle() {
		
		return "Billions of Stars";
	}
	public String getDescription() {
		return "Travel to Space!";
	}

	public Material getIcon() {
		return Material.IRON_CHESTPLATE;
	}

}
