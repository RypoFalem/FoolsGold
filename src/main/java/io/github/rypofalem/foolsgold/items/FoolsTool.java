package io.github.rypofalem.foolsgold.items;

import com.winthier.custom.item.CustomItem;
import com.winthier.custom.item.UncraftableItem;
import io.github.rypofalem.foolsgold.FoolsGoldPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public abstract class FoolsTool implements CustomItem, UncraftableItem {
	protected final ItemStack itemStack;

	FoolsTool(ItemStack itemstack){
		this.itemStack = itemstack;
	}

	@Override
	public String getCustomId() {
		return FoolsGoldPlugin.getInstance().getName() + ":" + this.getClass().getSimpleName();
	}

	@Override
	public ItemStack spawnItemStack(int amount) {
		ItemStack item = itemStack.clone();
		item.setAmount(amount);
		return item;
	}

	//Damages the given itemstack by the given durability. If the durability drops below zero the tool will turn to air
	//If a non-null location is provided, a tool breaking sound will be played there
	protected ItemStack damageTool(ItemStack item, short durabilityLoss, Location location){
		short durability = (short) Math.min((int)item.getDurability() + (int)durabilityLoss, Short.MAX_VALUE);
		if(durability >= item.getType().getMaxDurability()){
			item.setType(Material.AIR);
			if(location != null) location.getWorld().playSound(location, Sound.ENTITY_ITEM_BREAK, 1, 1);
		} else{
			item.setDurability(durability);
		}
		return item;
	}

	protected ItemStack breakTool(ItemStack item, Location location){
		return damageTool(item, Short.MAX_VALUE, location);
	}

}
