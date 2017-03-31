package io.github.rypofalem.foolsgold;

import io.github.rypofalem.foolsgold.items.FoolsTool;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;

public class MerchantGUI {
	private final Merchant merchant;

	public MerchantGUI(Player player, List<FoolsTool> tools){
		merchant = Bukkit.createMerchant("The April Fool");
		ArrayList<MerchantRecipe> recipes = new ArrayList<>();
		for(FoolsTool tool : tools){
			MerchantRecipe recipe = new MerchantRecipe(tool.spawnItemStack(1), 0, 100, false);
			recipe.addIngredient(new ItemStack(Material.GOLD_BLOCK));
			recipes.add(recipe);
		}
		merchant.setRecipes(recipes);
	}

	public void open(Player player){
		player.openMerchant(merchant, true);
	}
}
