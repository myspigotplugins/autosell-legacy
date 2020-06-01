package io.github.shiryu.autosell.menu.impl;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import io.github.shiryu.autosell.AutoSell;
import io.github.shiryu.autosell.api.AutoSellAPI;
import io.github.shiryu.autosell.api.item.AutoSellItem;
import io.github.shiryu.autosell.menu.Menu;
import io.github.shiryu.autosell.util.Colored;
import io.github.shiryu.autosell.util.ItemBuilder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemListMenu implements Menu {

    @Override
    public void openFor(@NotNull Player player) {
        final int rows = AutoSell.getInstance().getConfigs().menus.itemListMenuSection.ROWS;

        final Gui gui = new Gui(
                AutoSell.getInstance(),
                rows,
                new Colored(
                        AutoSell.getInstance().getConfigs().menus.itemListMenuSection.TITLE
                ).value()
        );

        final StaticPane pane = new StaticPane(9, rows);

        AutoSellAPI.getInstance().findUser(player.getUniqueId()).ifPresent(user ->{
            final List<AutoSellItem> items = user.getItems();

            int slot = 0;

            for (AutoSellItem item : items){
                pane.addItem(
                        new GuiItem(
                                new ItemBuilder(item.getMaterial())
                                .name(AutoSell.getInstance().getConfigs().menus.itemListMenuSection.items.NAME)
                                .lore(AutoSell.getInstance().getConfigs().menus.itemListMenuSection.items.LORE),
                                (click ->{
                                    click.setCancelled(true);
                                })
                        ),
                        (slot % 9) - 1,
                        slot / 9
                );

                slot++;
            }
        });

        gui.addPane(pane);

        gui.show(player);
    }
}
