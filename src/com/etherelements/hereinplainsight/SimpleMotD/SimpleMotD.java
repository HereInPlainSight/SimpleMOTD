package com.etherelements.hereinplainsight.SimpleMotD;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleMotD extends JavaPlugin implements Listener{
	String fullmotd;
	FileConfiguration config;

	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		config = this.getConfig();

		config.options().copyDefaults(true);
		saveConfig();
		fullmotd = config.getString("motd");
	}

	@Override
	public void onDisable(){
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onLogin(PlayerJoinEvent event){
		event.getPlayer().sendMessage(getMotD());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (cmd.getName().compareToIgnoreCase("setmotd")==0){
			if (!(sender instanceof Player)){
				setMotD(args);
				return true;
			} else {
				if (sender.hasPermission("simplemotd.set")){
					setMotD(args);
					this.getServer().broadcastMessage(getMotD());
					return true;
				}else{
					sender.sendMessage("You don't have permission to change the MotD.");
					return false;
				}
			}
		}else if(cmd.getName().compareToIgnoreCase("motd")==0){
			sender.sendMessage(getMotD());
			return true;
		}else
			return false;
	}

	public boolean setMotD(String[] input){
		StringBuilder motd = new StringBuilder();
		for (int index = 0; index < input.length; index++) {
			if (index == input.length)
				motd.append(input[index]);
			else
				motd.append(input[index] + " ");
		}
		fullmotd = motd.toString();
		config.set("motd", fullmotd);
		saveConfig();
		return true;
	}
	
	public String getMotD(){
		return fullmotd;
	}
}
