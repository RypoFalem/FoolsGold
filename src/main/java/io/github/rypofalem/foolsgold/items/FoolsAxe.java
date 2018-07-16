package io.github.rypofalem.foolsgold.items;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class FoolsAxe extends FoolsTool{
	@Getter
	private static FoolsAxe instance;

	public FoolsAxe(){
		super(new ItemStack(Material.GOLDEN_AXE));
		instance = this;
	}

	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		Material mat = event.getBlock().getType();
		switch(mat){
			case ACACIA_LOG:
			case BIRCH_LOG:
			case DARK_OAK_LOG:
			case JUNGLE_LOG:
			case OAK_LOG:
			case SPRUCE_LOG:
				event.getPlayer().performCommand("ch say l This tree is too big to handle. Better try something smaller.");
				event.setCancelled(true);
				break;
			case SPRUCE_SAPLING:
			case ACACIA_SAPLING:
			case BIRCH_SAPLING:
			case DARK_OAK_SAPLING:
			case JUNGLE_SAPLING:
			case OAK_SAPLING:
				event.getPlayer().performCommand("ch say l Take that, pathetic tree!");
				break;
		}
	}
}