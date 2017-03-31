package io.github.rypofalem.foolsgold.entities;

import io.github.rypofalem.foolsgold.DeathCausing;
import io.github.rypofalem.foolsgold.FoolsGoldPlugin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;


public class FoolsArrow implements DeathCausing{
	final HashMap<UUID, Info> arrowTracker = new HashMap<>();
	@Getter
	static FoolsArrow instance;

	public FoolsArrow(){
		instance = this;
	}

	//redirected from Listener
	public void onArrowHit(ProjectileHitEvent event){
		Info info = arrowTracker.get(event.getEntity().getUniqueId());
		if(info == null) return;
		arrowTracker.remove(event.getEntity().getUniqueId());
		Arrow arrow = (Arrow) event.getEntity();
		Player player = Bukkit.getServer().getPlayer(info.playerUuid);
		if(player == null || !player.isOnline()) return;
		if(info.stage == FoolsArrow.Stage.one){

			//(point destination - point start) normalized to 1. Gets the direction of the arrow
			Vector direction = player.getEyeLocation().toVector().subtract(arrow.getLocation().toVector()).normalize();
			//set spawn location to a point between the old arrow's death and the player, .5 blocks away from old death
			Location spawnArrowLocation = arrow.getLocation().add(direction.multiply(.5));

			arrow.remove();
			arrow = arrow.getWorld().spawnArrow(spawnArrowLocation, direction, 1.5f, 1);
			makeArrow(arrow, player, FoolsArrow.Stage.two);
			FoolsGoldPlugin.getInstance().incrementStat("arrowsBounced");
		} else if(player.equals(event.getHitEntity())){
			FoolsArrow.getInstance().hurtPlayer(player, player, 20);
			arrow.remove();
		}
	}

	void makeArrow(Arrow arrow, Player shooter, Stage stage){
		arrowTracker.put(arrow.getUniqueId(), new Info(shooter.getUniqueId(), stage));
	}

	public void makeArrow(Arrow arrow, Player shooter){
		makeArrow(arrow, shooter, Stage.one);
	}

	@Override
	public String getDeathMessage(Player deceased) {
		return String.format("%s jumped in front of their own arrow!", deceased.getName());
	}


	enum Stage {
		one, two
	}

	@AllArgsConstructor
	class Info{
		UUID playerUuid;
		Stage stage;
	}
}