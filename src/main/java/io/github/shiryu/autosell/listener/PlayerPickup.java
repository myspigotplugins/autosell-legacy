package io.github.shiryu.autosell.listener;

import io.github.shiryu.autosell.AutoSell;
import io.github.shiryu.autosell.api.AutoSellAPI;
import io.github.shiryu.autosell.api.event.type.AutoPickupEvent;
import io.github.shiryu.autosell.api.item.AutoSellItem;
import io.github.shiryu.autosell.hook.impl.vault.VaultWrapper;
import io.github.shiryu.autosell.util.Colored;
import io.github.shiryu.autosell.util.DropUtil;
import io.github.shiryu.autosell.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerPickup implements Listener {

    @EventHandler
    public void playerBreak(final BlockBreakEvent event){
        final Player player = event.getPlayer();
        final Block block = event.getBlock();

        if (player.getGameMode() == GameMode.CREATIVE) return;

        final ItemStack hand = player.getItemInHand();

        final Collection<ItemStack> drops = DropUtil.getInstance().dropsFor(hand, block);

        drops.forEach(item ->{
            if (InventoryUtil.getInstance().checkFor(player, item) && !AutoSell.getInstance().getConfigs().BLACK_LIST.contains(item.getType().name())){
                player.getInventory().addItem(
                        item
                );

                final AutoPickupEvent pickupEvent = new AutoPickupEvent(
                        player,
                        item
                );

                Bukkit.getPluginManager().callEvent(pickupEvent);
            }else{
                player.sendMessage(
                        new Colored(
                                AutoSell.getInstance().getConfigs().INVENTORY_FULL
                        ).value()
                );
                return;
            }
        });

        event.setCancelled(true);
        block.setType(Material.AIR);

        player.giveExp(event.getExpToDrop());
    }

    @EventHandler
    public void playerPickup(final AutoPickupEvent event){
        event.setCancelled(true);

        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();

        AutoSellAPI.getInstance().findUser(player.getUniqueId()).ifPresent(user ->{
            final List<AutoSellItem> items = user.getItems()
                    .stream()
                    .filter(x -> x.isEnabled())
                    .collect(Collectors.toList());

            if (items.stream().anyMatch(x -> x.getMaterial() == item.getType())){
                items.stream()
                        .filter(x -> x.getMaterial() == item.getType())
                        .filter(x -> player.getInventory().containsAtLeast(item, x.getDefaultStackSize()))
                        .forEach(x ->{
                            final int price = AutoSell.getInstance().getPrices().priceOf(x.getMaterial());

                            final int stackSize = count(player, x.getMaterial());

                            final VaultWrapper wrapper = AutoSell.getInstance().getVaultHook().create();

                            final int money = price * stackSize;

                            wrapper.addMoney(
                                    player,
                                    money
                            );

                            player.sendMessage(
                                    new Colored(
                                            AutoSell.getInstance().getConfigs().ITEM_SELL
                                            .replaceAll("%item%", AutoSell.getInstance().getNamings().namingOf(x.getMaterial()))
                                            .replaceAll("%price%", String.valueOf(money))
                                    ).value()
                            );
                        });
            }
        });
    }

    private int count(@NotNull final Player player, @NotNull final Material material){
        int count = 0;

        for (ItemStack item : player.getInventory().getContents()){
            if (item == null || item.getType() == Material.AIR || item.getType() != material) continue;

            count++;
        }

        return count;
    }


}
