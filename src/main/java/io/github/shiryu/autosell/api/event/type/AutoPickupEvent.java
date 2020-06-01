package io.github.shiryu.autosell.api.event.type;

import io.github.shiryu.autosell.api.event.EventBase;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AutoPickupEvent extends EventBase {

    private final ItemStack item;

    public AutoPickupEvent(@NotNull final Player player, @NotNull final ItemStack item){
        super(player);

        this.item = item;
    }

    @NotNull
    public ItemStack getItem() {
        return item;
    }
}
