package io.github.rypofalem.foolsgold.items;

import io.github.rypofalem.foolsgold.entities.FoolsArrow;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public class FoolsBow extends FoolsTool{
	@Getter
	private static FoolsBow instance;

	public FoolsBow(){
		super(new ItemStack(Material.BOW));
		instance = this;
		new FoolsArrow();
	}

	@EventHandler (priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onShootBowEvent(EntityShootBowEvent event){
		if(!(event.getProjectile() instanceof Arrow)) return;
		if(!(event.getEntity() instanceof Player)) return;
		FoolsArrow.getInstance().makeArrow((Arrow)event.getProjectile(), (Player)event.getEntity());
	}
}
