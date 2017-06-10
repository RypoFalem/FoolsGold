package io.github.rypofalem.foolsgold.items;

import com.winthier.generic_events.GenericEventsPlugin;
import io.github.rypofalem.foolsgold.DeathCausing;
import io.github.rypofalem.foolsgold.FoolsGoldPlugin;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class FoolsPick extends FoolsTool implements DeathCausing{
	@Getter
	final int COLLAPSECHANCE = 10; //1 in in this number, bigger number is lower chance
	final int COLLAPSERADIUS = 4;
	@Getter
	private static FoolsPick instance;

	public FoolsPick(){
		super(new ItemStack(Material.GOLD_PICKAXE));
		instance = this;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBreakBlock(BlockBreakEvent event){
		if(!isEarth(event.getBlock().getType())) return;
		if(!isEarth(event.getPlayer().getEyeLocation().clone().add(0, 2, 0).getBlock().getType())) return;
		if(FoolsGoldPlugin.getInstance().getRand().nextInt(COLLAPSECHANCE) != 0) return;
		List<Block> blocks = getGriefableHemisphere(event.getPlayer());
		if(blocks.isEmpty() || blocks.size() < 30) return;
		event.getPlayer().getInventory().setItemInMainHand(
				this.breakTool(event.getPlayer().getInventory().getItemInMainHand(), event.getPlayer().getLocation())
		);
		for(Block block : blocks){
			if(block.getType() == Material.AIR) continue; //in case a duplicate made it into the list somehow
			MaterialData data= block.getState().getData().clone();
			block.breakNaturally(new ItemStack(Material.AIR));
			block.getWorld().spawnFallingBlock(block.getLocation().add(0.5, 0.0, 0.5), data);
		}
		io.github.rypofalem.foolsgold.Listener.getInstance()
				.getTicksWatchingCollapse().put(event.getPlayer().getUniqueId(), 20);
	}

	private List<Block> getGriefableHemisphere(Player player){
		ArrayList<Block> blocks = new ArrayList<>();
		int radiusSquared = COLLAPSERADIUS * COLLAPSERADIUS;
		Location center = player.getEyeLocation().add(0,1,0);
		Location block;
		for(double x = center.getX() - COLLAPSERADIUS; x <= center.getX() + COLLAPSERADIUS; x++){
			for(double y = center.getY(); y <= center.getY() + COLLAPSERADIUS; y++){
				for(double z = center.getZ() - COLLAPSERADIUS; z <= center.getZ() + COLLAPSERADIUS; z++){
					block = new Location(center.getWorld(), x, y, z);
					if(isEarth(block.getBlock().getType())
							&& GenericEventsPlugin.getInstance().playerCanGrief(player, block.getBlock())
							&& center.distanceSquared(block) <= radiusSquared)
					{
						blocks.add(block.getBlock());
					}
				}
			}
		}
		return blocks;
	}

	//returns true for most blocks you might find naturally underground such as stone, ores and dirt
	private boolean isEarth(Material material){
		switch(material){
			case STONE:
			case COBBLESTONE:
			case CLAY:
			case DIRT:
			case COAL_ORE:
			case IRON_ORE:
			case GOLD_ORE:
			case REDSTONE_ORE:
			case DIAMOND_ORE:
				return true;
		}
		return false;
	}

	@Override
	public String getDeathMessage(Player deceased) {
		return String.format("%s's greed led to a cave collapse.", deceased.getName());
	}
}
