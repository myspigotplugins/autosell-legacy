package io.github.shiryu.autosell.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DropUtil {

    private static DropUtil instance;

    private DropUtil(){

    }

    @NotNull
    public Collection<ItemStack> dropsFor(@NotNull final ItemStack hand, @NotNull final Block block){
        final Collection<ItemStack> drops = block.getDrops(hand);

        if (canGiveBonus(block) && hand.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)){
            final List<ItemStack> drs = new ArrayList<>();

            if (block.getType() == Material.REDSTONE_ORE || block.getType() == Material.LAPIS_ORE){
                drops.forEach(item -> item.setAmount(item.getAmount() + getBonus(hand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS))));

                drs.addAll(drops);
            }else{

                for (int i = 0; i < getBonus(hand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)); i++){
                    drs.addAll(drops);
                }
            }

            return drs;
        }


        return drops;
    }

    private int getBonus(final int fortuneLevel) {
        final Random random = new Random();

        int bonus = random.nextInt(fortuneLevel + 2) - 1;

        if (bonus < 0) {
            bonus = 0;
        }

        return bonus;
    }

    private boolean canGiveBonus(@NotNull final Block block) {
        if (block.getType() == Material.LAPIS_ORE || block
                .getType() == Material.COAL_ORE || block
                .getType() == Material.DIAMOND_ORE || block
                .getType() == Material.EMERALD_ORE  || block
                .getType() == Material.GOLD_ORE || block
                .getType() == Material.IRON_ORE || block
                .getType() == Material.REDSTONE_ORE || block
                .getType() == Material.CLAY) {
            return true;
        }
        return false;
    }


    public static synchronized DropUtil getInstance(){
        if (instance == null) instance = new DropUtil();

        return instance;
    }
}
