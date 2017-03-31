package io.github.rypofalem.foolsgold;


import com.winthier.custom.event.CustomRegisterEvent;
import io.github.rypofalem.foolsgold.items.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FoolsGoldPlugin extends JavaPlugin implements CommandExecutor, org.bukkit.event.Listener{
	@Getter
	Random rand = new Random();
	@Getter
	static FoolsGoldPlugin instance;
	List<FoolsTool> tools = new ArrayList<>();
	private SaveConfigTask saveConfigTask;

	public FoolsGoldPlugin(){
		instance = this;
	}

	@Override
	public void onEnable(){
		Bukkit.getPluginManager().registerEvents(new Listener(), this);
		Bukkit.getPluginManager().registerEvents(this, this);
		this.getCommand("fgmerchant").setExecutor(this);
		saveDefaultConfig();
		saveConfigTask = new SaveConfigTask();
		Bukkit.getScheduler().runTaskTimer(this, saveConfigTask, 180 * 20, 180 * 20);
	}

	@Override
	public void onDisable(){
		saveConfig();
	}

	@EventHandler
	public void onCustomRegister(CustomRegisterEvent event) {
		tools.add(new FoolsSword());
		tools.add(new FoolsAxe());
		tools.add(new FoolsBow());
		tools.add(new FoolsPick());
		for(FoolsTool tool : tools){
			event.addItem(tool);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(!(sender instanceof Player)) return false;
		new MerchantGUI((Player)sender, tools).open((Player)sender);
		return true;
	}

	public void incrementStat(String path){
		if(getConfig().isInt(path)) getConfig().set(path, getConfig().getInt(path) + 1);
		saveConfigTask.saveQueued = true;
	}

	class SaveConfigTask implements Runnable{
		boolean saveQueued = false;

		@Override
		public void run() {
			if(saveQueued) {
				saveConfig();
				saveQueued = false;
			}
		}
	}
}
