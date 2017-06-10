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
import java.util.Map;
import java.util.Random;

public class FoolsGoldPlugin extends JavaPlugin implements CommandExecutor, org.bukkit.event.Listener{
	@Getter
	Random rand;
	@Getter
	static FoolsGoldPlugin instance;
	List<FoolsTool> tools;

	public FoolsGoldPlugin(){
		instance = this;
	}

	@Override
	public void onEnable(){
		tools = new ArrayList<>();
		 rand = new Random();
		Bukkit.getPluginManager().registerEvents(new Listener(), this);
		Bukkit.getPluginManager().registerEvents(this, this);
		this.getCommand("fgmerchant").setExecutor(this);
		saveDefaultConfig();
		tools.add(new FoolsSword());
		tools.add(new FoolsAxe());
		tools.add(new FoolsBow());
		tools.add(new FoolsPick());
	}

	@Override
	public void onDisable(){
		saveConfig();
	}

	@EventHandler
	public void onCustomRegister(CustomRegisterEvent event) {
		for(FoolsTool tool : tools){
			event.addItem(tool);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(args != null && args.length > 0 && args[0].equals("stats")){
			StringBuilder sb = new StringBuilder();
			for(String stat : getConfig().getValues(false).keySet()){
				if(getConfig().isInt(stat))
					sb.append( String.format("%s: %d\n", stat, getConfig().getInt(stat)));
			}
			sender.sendMessage(sb.toString());
			return true;
		}
		if(!(sender instanceof Player)) return false;
		new MerchantGUI((Player)sender, tools).open((Player)sender);
		return true;
	}
}
