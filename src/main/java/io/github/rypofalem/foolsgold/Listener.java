package io.github.rypofalem.foolsgold;

import com.winthier.custom.CustomPlugin;
import com.winthier.custom.item.CustomItem;
import io.github.rypofalem.foolsgold.entities.FoolsArrow;
import io.github.rypofalem.foolsgold.items.FoolsAxe;
import io.github.rypofalem.foolsgold.items.FoolsBow;
import io.github.rypofalem.foolsgold.items.FoolsPick;
import io.github.rypofalem.foolsgold.items.FoolsSword;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class Listener implements org.bukkit.event.Listener{
	@Getter
	static Listener instance;
	//deathMessages should be set just before DeathCausing things apply damage and removed immediatly after
	@Getter
	HashMap<UUID, String> deathMessages = new HashMap<>();
	@Getter
	HashMap<UUID, Integer> ticksWatchingCollapse = new HashMap<>();

	public Listener(){
		instance = this;
	}

	@EventHandler (priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event){
		String deathMessage = deathMessages.get(event.getEntity().getUniqueId());
		if(deathMessage != null){
			event.setDeathMessage(deathMessage);
			if(deathMessage.equals( FoolsSword.getInstance().getDeathMessage(event.getEntity()))){
				FoolsGoldPlugin.getInstance().incrementStat("puzzlesSolved");
			} else if(deathMessage.equals( FoolsArrow.getInstance().getDeathMessage(event.getEntity()))){
				FoolsGoldPlugin.getInstance().incrementStat("arrowsReturned");
			} else if(deathMessage.equals( FoolsPick.getInstance().getDeathMessage(event.getEntity()))){
				FoolsGoldPlugin.getInstance().incrementStat("stoneBreathers");
			}
		}
		deathMessages.remove(event.getEntity().getUniqueId());
	}

	@EventHandler
	public void onArrowHit(ProjectileHitEvent event) {
		FoolsArrow.getInstance().onArrowHit(event);
	}

	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onDamageInBlock(EntityDamageEvent event){
		if(!(event.getEntity() instanceof Player)) return;
		if(event.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION) return;
		Player player = (Player) event.getEntity();
		Integer ticks = ticksWatchingCollapse.get(player.getUniqueId());
		if(ticks == null) return;
		ticksWatchingCollapse.put(player.getUniqueId(), 20);
		event.setCancelled(true);
		FoolsPick.getInstance().hurtPlayer(player, player, event.getDamage()*8); //cave-ins hurt more ;)
		if(player.isDead()) ticksWatchingCollapse.remove(player.getUniqueId());
	}

	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPurchase(InventoryClickEvent event){
		if(event.getClickedInventory() == null) return;
		if(!event.getClickedInventory().getType().equals(InventoryType.MERCHANT)) return;
		if(event.getRawSlot() != 2) return;
		ItemStack result = event.getCurrentItem();
		if(result == null || result.getType() == Material.AIR) return;
		CustomItem item = CustomPlugin.getInstance().getItemManager().getCustomItem(result);
		if(item == null) return;
		String id = item.getCustomId();
		if(FoolsAxe.getInstance().getCustomId().equals(id)){
			FoolsGoldPlugin.getInstance().incrementStat("axesAquired");
		} else if(FoolsBow.getInstance().getCustomId().equals(id)){
			FoolsGoldPlugin.getInstance().incrementStat("bowsBought");
		} else if(FoolsPick.getInstance().getCustomId().equals(id)){
			FoolsGoldPlugin.getInstance().incrementStat("picksPicked");
		} else if(FoolsSword.getInstance().getCustomId().equals(id)){
			FoolsGoldPlugin.getInstance().incrementStat("swordsSold");
		}
	}
}
