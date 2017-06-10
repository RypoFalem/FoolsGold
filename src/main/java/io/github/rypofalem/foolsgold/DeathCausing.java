package io.github.rypofalem.foolsgold;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface DeathCausing {

	public String getDeathMessage(Player deceased);

	//applies damage to the player, registering a potentially used custom death message before the damage is applied and
	//unregestering it after
	default void hurtPlayer(Player player, Entity source, double dmg){
		Listener.getInstance().getDeathMessages().put(player.getUniqueId(), getDeathMessage(player));
		player.damage(dmg, source);
		Listener.getInstance().getDeathMessages().remove(player.getUniqueId());
	}
}