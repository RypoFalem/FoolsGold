package io.github.rypofalem.foolsgold;

import io.github.rypofalem.foolsgold.entities.FoolsArrow;
import io.github.rypofalem.foolsgold.items.FoolsPick;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

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

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		String deathMessage = deathMessages.get(event.getEntity().getUniqueId());
		if(deathMessage != null) event.setDeathMessage(deathMessage);
		deathMessages.remove(event.getEntity().getUniqueId());
	}

	@EventHandler
	public void onArrowHit(ProjectileHitEvent event) {
		FoolsArrow.getInstance().onArrowHit(event);
	}

	@EventHandler
	public void onDamageInBlock(EntityDamageEvent event){
		if(!(event.getEntity() instanceof Player)) return;
		if(event.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION) return;
		Player player = (Player) event.getEntity();
		Integer ticks = ticksWatchingCollapse.get(player.getUniqueId());
		if(ticks == null) return;
		ticksWatchingCollapse.put(player.getUniqueId(), 20);
		Bukkit.broadcastMessage("test");
		event.setCancelled(true);
		FoolsPick.getInstance().hurtPlayer(player, player, event.getDamage()*8); //cave-ins hurt more ;)
		if(player.isDead()) ticksWatchingCollapse.remove(player.getUniqueId());
	}
}
