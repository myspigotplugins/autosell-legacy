package io.github.shiryu.autosell.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class InventoryUtil {

    private static InventoryUtil instance;

    private InventoryUtil(){

    }

    public boolean checkFor(@NotNull final Player player, @NotNull final ItemStack itemStack){
        if (itemStack.getType() == Material.AIR) {
            return true;
        }

        if (itemStack.getAmount() > 5000) {
            return false;
        }

        if (player.getInventory().firstEmpty() >= 0 && itemStack.getAmount() <= itemStack.getMaxStackSize()) {
            return true;
        }

        if (itemStack.getAmount() > itemStack.getMaxStackSize()) {
            final ItemStack clone = itemStack.clone();

            clone.setAmount(itemStack.getMaxStackSize());

            return checkFor(
                    player,
                    clone
            );
        }

        final Map<Integer, ? extends ItemStack> all = player.getInventory().all(itemStack.getType());
        int amount = itemStack.getAmount();

        for (ItemStack element : all.values()) {
            amount = amount - (element.getMaxStackSize() - element.getAmount());
        }

        return amount <= 0;
    }

    public static synchronized InventoryUtil getInstance(){
        if (instance == null) instance = new InventoryUtil();

        return instance;
    }
}
