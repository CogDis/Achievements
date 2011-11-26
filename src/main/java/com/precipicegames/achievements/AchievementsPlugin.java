package com.precipicegames.achievements;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;
import com.precipicegames.achievements.basic.SpaceAchievement;

import net.sf.json.JSONObject;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

public class AchievementsPlugin extends JavaPlugin {
	private static SpaceAchievement SA = new SpaceAchievement();
	public class BasicListener extends PlayerListener {
		public void onPlayerDropItem(PlayerDropItemEvent event)
		{
			if(event.getItemDrop().getItemStack().getType() == Material.SPONGE)
			{
				award(event.getPlayer(),SA,null);
			}
		}
	}
	private HashMap <String,Achievement> achievements = new HashMap<String,Achievement>();
	private Database db;

	public void onDisable() {
        // TODO: Place any custom disable code here.
        System.out.println(this + " is now disabled!");
    }

    public void onEnable() {
    	log(Level.INFO, "Starting plugin..");
    	//Create configuration directory
    	this.getDataFolder().mkdirs();
    	Session dbSession = new Session("localhost",5984);
    	Database db = dbSession.getDatabase("achievements");
    	if(db == null)
    		db = dbSession.createDatabase("achievements");
    	if(db == null)
    		return;
    	
    	this.register(SA);
    	this.getServer().getPluginManager().registerEvent(Type.PLAYER_DROP_ITEM, new BasicListener(), Priority.Normal, this);
    	
        log(Level.INFO, "Startup finished!");
    }
    private void log(Level level, String msg)
    {
		this.getServer().getLogger().log(level,"[" + this.getDescription().getName() + "] " + msg);
    }
    public void register(Achievement a)
    {
		achievements.put(a.getTitle(),a);
    }
    public void unregister(Achievement a)
    {
    	achievements.remove(a.getTitle());
    }
    public Document getDoc(String ID)
    {
    	Document doc;
		try {
			doc = db.getDocument("dotblank");
		} catch (IOException e) {
			doc = new Document();
		}
		return doc;
    }
    public void award(Player p, Achievement a, JSONObject data)
    {
    	Document doc = getDoc(p.getName());
    	JSONObject storage = new JSONObject();
    	if(data != null)
    		storage.put("data", data);
    	storage.put("time", new Date().toString());
    	doc.put(a.getTitle(), storage);
    	try {
			db.saveDocument(doc, p.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		SpoutPlayer sp = SpoutManager.getPlayer(p);
		if(sp.isSpoutCraftEnabled())
		{
			sp.sendNotification("New Achievement!" , a.getTitle(), a.getIcon());
		}
		else
		{
			sp.sendMessage("New Achievement!" + a.getTitle() + "\n" + a.getDescription());
		}
    }
    public void displayAchievements(Player p)
    {
    	Document doc = getDoc(p.getName());
    	p.sendMessage("Acquired Achievements!");
    	for(String plugin : (String[]) doc.keySet().toArray())
    	{
    		p.sendMessage("-> " + plugin);
    	}
    }
    public JSONObject getData(Achievement a, Player p)
    {
    	Document doc = getDoc(p.getName());
    	return doc.getJSONObject(a.getTitle());
    }
    public void setData(Achievement a, Player p, JSONObject data)
    {
    	Document doc = getDoc(p.getName());
    	doc.put(a.getTitle(), new JSONObject().put("data", data));
    	try {
			db.saveDocument(doc, p.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
