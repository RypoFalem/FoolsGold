package io.github.rypofalem.foolsgold.items;

import io.github.rypofalem.foolsgold.DeathCausing;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class FoolsSword extends FoolsTool implements DeathCausing {
	private final double damage = 20;
	@Getter
	private static FoolsSword instance;
	boolean ignoreEvents = false;

	public FoolsSword(){
		super(new ItemStack(Material.GOLDEN_SWORD));
		instance = this;
	}

	@EventHandler (priority = EventPriority.HIGH)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(ignoreEvents) return;
		if(!event.getHand().equals(EquipmentSlot.HAND)) return;
		if(!(event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_AIR))) return;
		event.setCancelled(true);
		Player player = event.getPlayer();
		ignoreEvents = true;
		hurtPlayer(player, player, damage);
		ignoreEvents = false;
		player.getInventory().setItemInMainHand(damageTool(event.getItem(), (short)5, player.getLocation()));
	}

	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerAttack(EntityDamageByEntityEvent event) {
		if(ignoreEvents) return;
		if(!(event.getDamager() instanceof Player)) return;
		Player player = (Player) event.getDamager();
		ignoreEvents = true;
		hurtPlayer(player, player, damage);
		ignoreEvents = false;
		player.getInventory().setItemInMainHand(damageTool(player.getEquipment().getItemInMainHand(), (short)5, player.getLocation()));
	}

	@Override
	public String getDeathMessage(Player deceased) {
		return String.format("%s committed sudoku.", deceased.getName());
	}
}