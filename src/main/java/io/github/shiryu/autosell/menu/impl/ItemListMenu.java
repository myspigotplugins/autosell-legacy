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
import io.github.shiryu.autosell.util.ReplaceAllList;
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

            List<String> sunglas = AutoSell.getInstance().getConfigs().menus.itemListMenuSection.items.LORE;

            int slot = 0;

            for (AutoSellItem item : items){
                slot++;

                ReplaceAllList replaceAllList = new ReplaceAllList(sunglas)
                        .replaceAll("%item%", AutoSell.getInstance().getNamings().namingOf(item.getMaterial()))
                        .replaceAll("%stack%", String.valueOf(item.getDefaultStackSize()));

                if (item.isEnabled()){
                    replaceAllList.replaceAll("%enabled%", AutoSell.getInstance().getConfigs().ENABLED);
                }else{
                    replaceAllList.replaceAll("%enabled%", AutoSell.getInstance().getConfigs().DISABLED);
                }

                pane.addItem(
                        new GuiItem(
                                new ItemBuilder(item.getMaterial())
                                .name(AutoSell.getInstance().getConfigs().menus.itemListMenuSection.items.NAME.replaceAll("%item%", AutoSell.getInstance().getNamings().namingOf(item.getMaterial())))
                                .lore(replaceAllList.value()),
                                (click ->{
                                    click.setCancelled(true);

                                    final Player clicked = (Player) click.getWhoClicked();

                                    clicked.closeInventory();

                                    final Menu menu = new ItemEditMenu(item);

                                    menu.openFor(clicked);
                                })
                        ),
                        (slot % 9) - 1,
                        slot / 9
                );

            }
        });

        gui.addPane(pane);

        gui.show(player);
    }
}
