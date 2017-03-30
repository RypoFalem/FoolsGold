package io.github.rypofalem.foolsgold.items;


import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class FoolsAxe extends FoolsTool{

	public FoolsAxe(){
		super(new ItemStack(Material.GOLD_AXE));
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName("Tree Feller");
		meta.setLore(Arrays.asList("Cuts down the WHOLE tree!"));
		itemStack.setItemMeta(meta);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Material mat = event.getBlock().getType();
		if(mat.equals(Material.LOG) || mat.equals(Material.LOG_2)){
			event.getPlayer().performCommand("l This tree is too big to handle. Better try something smaller.");
			event.setCancelled(true);
		} else if (mat.equals(Material.SAPLING)){
			event.getPlayer().performCommand("l Take that, pathetic tree!");
		}
	}
}
