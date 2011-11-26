package com.precipicegames.achievements;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.config.ConfigurationNode;

public interface Achievement {
	
	public String getTitle();
	
	public String getDescription();
	
	public Material getIcon();
}
