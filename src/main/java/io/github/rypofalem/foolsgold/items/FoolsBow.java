package io.github.rypofalem.foolsgold.items;


import com.winthier.generic_events.ItemNameEvent;
import io.github.rypofalem.foolsgold.entities.FoolsArrow;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class FoolsBow extends FoolsTool{
	private final double damage = 20;
	@Getter
	private static FoolsBow instance;

	public FoolsBow(){
		super(new ItemStack(Material.BOW));
		instance = this;
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName("Bouncing Bow");
		meta.setLore(Arrays.asList(
				"Fires arrows capable of",
				"hitting multiple targets."));
		itemStack.setItemMeta(meta);
		new FoolsArrow();
	}

	@EventHandler (priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onShootBowEvent(EntityShootBowEvent event){
		if(!(event.getProjectile() instanceof Arrow)) return;
		if(!(event.getEntity() instanceof Player)) return;
		FoolsArrow.getInstance().makeArrow((Arrow)event.getProjectile(), (Player)event.getEntity());
	}
}
