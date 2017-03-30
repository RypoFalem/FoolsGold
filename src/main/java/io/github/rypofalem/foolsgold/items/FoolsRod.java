package io.github.rypofalem.foolsgold.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

//unimplemented
public class FoolsRod extends FoolsTool {

	FoolsRod() {
		super(new ItemStack(Material.GOLD_SWORD));
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName("Super Rod");
		meta.setLore(Arrays.asList(
				"Catches level 40 fish",
				"\"You can't use that here!\""));
		itemStack.setItemMeta(meta);
	}
}
