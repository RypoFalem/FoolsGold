package io.github.rypofalem.foolsgold.items;

import io.github.rypofalem.foolsgold.DeathCausing;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class FoolsSword extends FoolsTool implements DeathCausing {
	private final double damage = 20;
	@Getter
	private static FoolsSword instance;
	boolean ignoreEvents = false;

	public FoolsSword(){
		super(new ItemStack(Material.GOLD_SWORD));
		instance = this;
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName("Sword of Puzzles");
		meta.setLore(Arrays.asList(
				"A sword of mysterious origin",
				"and even mysteriouser purpose. ",
				"Can you solve the puzzle?"));
		itemStack.setItemMeta(meta);
	}

	@EventHandler
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

	@EventHandler
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
		return String.format("%s commited sudoku.", deceased.getName());
	}
}