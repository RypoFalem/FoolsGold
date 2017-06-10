package io.github.rypofalem.foolsgold.items;


import io.github.rypofalem.foolsgold.FoolsGoldPlugin;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class FoolsAxe extends FoolsTool{
	@Getter
	private static FoolsAxe instance;

	public FoolsAxe(){
		super(new ItemStack(Material.GOLD_AXE));
		instance = this;
	}

	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		Material mat = event.getBlock().getType();
		if(mat.equals(Material.LOG) || mat.equals(Material.LOG_2)){
			event.getPlayer().performCommand("ch say l This tree is too big to handle. Better try something smaller.");
			event.setCancelled(true);
		} else if (mat.equals(Material.SAPLING)){
			event.getPlayer().performCommand("ch say l Take that, pathetic tree!");
		}
	}
}