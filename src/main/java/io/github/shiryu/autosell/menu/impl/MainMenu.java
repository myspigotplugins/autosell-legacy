package io.github.shiryu.autosell.menu.impl;

import com.cryptomorin.xseries.XMaterial;
import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import io.github.shiryu.autosell.AutoSell;
import io.github.shiryu.autosell.api.AutoSellAPI;
import io.github.shiryu.autosell.api.item.AutoSellItem;
import io.github.shiryu.autosell.menu.Menu;
import io.github.shiryu.autosell.util.Colored;
import io.github.shiryu.autosell.util.ItemBuilder;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainMenu implements Menu {

    @Override
    public void openFor(@NotNull Player player) {
        final int rows = AutoSell.getInstance().getConfigs().menus.mainMenuSection.ROWS;

        final Gui gui = new Gui(
                AutoSell.getInstance(),
                rows,
                new Colored(
                        AutoSell.getInstance().getConfigs().menus.mainMenuSection.TITLE
                ).value()
        );

        final StaticPane pane = new StaticPane(9, rows);

        pane.addItem(
                new GuiItem(
                    new ItemBuilder(XMaterial.matchXMaterial(AutoSell.getInstance().getConfigs().menus.mainMenuSection.items.createItemSection.MATERIAL).orElse(null).parseItem())
                        .name(AutoSell.getInstance().getConfigs().menus.mainMenuSection.items.createItemSection.NAME)
                        .lore(AutoSell.getInstance().getConfigs().menus.mainMenuSection.items.createItemSection.LORE),
                     (click ->{
                         click.setCancelled(true);

                         final Player clicked = (Player) click.getWhoClicked();

                         AutoSellAPI.getInstance().findUser(clicked.getUniqueId()).ifPresent(user ->{
                             final List<AutoSellItem> items = user.getItems();

                             AnvilGUI anvil = new AnvilGUI.Builder()
                                     .plugin(AutoSell.getInstance())
                                     .item(new ItemStack(XMaterial.PAPER.parseMaterial()))
                                     .text("Input...")
                                     .onComplete((anvilClicker, reply) ->{

                                         final Material material = AutoSell.getInstance().getNamings().materialOf(reply);

                                         if (material == null){
                                             player.sendMessage(
                                                     new Colored(
                                                             AutoSell.getInstance().getConfigs().NAMING_NOT_FOUND
                                                     ).value()
                                             );

                                             anvilClicker.closeInventory();
                                         }

                                         items.add(
                                                 new AutoSellItem(
                                                         material,
                                                         AutoSell.getInstance().getConfigs().DEFAULT_STACK_SIZE
                                                 )
                                         );

                                         user.setItems(items);

                                         user.save();

                                         anvilClicker.closeInventory();

                                         anvilClicker.sendMessage(
                                                 new Colored(
                                                         AutoSell.getInstance().getConfigs().ITEM_CREATED
                                                 ).value()
                                         );

                                         return AnvilGUI.Response.text(reply);
                                     })
                                     .open(clicked);
                         });
                     })
                ),
                2,
                0
        );

        pane.addItem(
                new GuiItem(
                        new ItemBuilder(XMaterial.matchXMaterial(AutoSell.getInstance().getConfigs().menus.mainMenuSection.items.listItemsSection.MATERIAL).orElse(null).parseItem())
                                .name(AutoSell.getInstance().getConfigs().menus.mainMenuSection.items.listItemsSection.NAME)
                                .lore(AutoSell.getInstance().getConfigs().menus.mainMenuSection.items.listItemsSection.LORE),
                        (click ->{
                            click.setCancelled(true);

                            final Player clicked = (Player) click.getWhoClicked();

                            clicked.closeInventory();

                            final Menu itemList = new ItemListMenu();

                            itemList.openFor(clicked);
                        })
                ),
                4,
                0
        );

        pane.addItem(
                new GuiItem(
                        new ItemBuilder(XMaterial.matchXMaterial(AutoSell.getInstance().getConfigs().menus.mainMenuSection.items.removeItemSection.MATERIAL).orElse(null).parseItem())
                                .name(AutoSell.getInstance().getConfigs().menus.mainMenuSection.items.removeItemSection.NAME)
                                .lore(AutoSell.getInstance().getConfigs().menus.mainMenuSection.items.removeItemSection.LORE),
                        (click ->{
                            click.setCancelled(true);

                            final Player clicked = (Player) click.getWhoClicked();

                            AutoSellAPI.getInstance().findUser(clicked.getUniqueId()).ifPresent(user ->{
                                final List<AutoSellItem> items = user.getItems();

                                AnvilGUI anvil = new AnvilGUI.Builder()
                                        .plugin(AutoSell.getInstance())
                                        .item(new ItemStack(XMaterial.PAPER.parseMaterial()))
                                        .text("Input...")
                                        .onComplete((anvilClicker, reply) ->{
                                            AutoSellAPI.getInstance().findItemFromNaming(user, reply).ifPresent(item ->{
                                                items.remove(
                                                        item
                                                );

                                                user.setItems(items);

                                                user.save();

                                                anvilClicker.closeInventory();


                                                anvilClicker.sendMessage(
                                                        new Colored(
                                                                AutoSell.getInstance().getConfigs().ITEM_DELETED
                                                        ).value()
                                                );
                                            });

                                            return AnvilGUI.Response.text(reply);
                                        })
                                        .open(clicked);
                            });
                        })
                ),
                6,
                0
        );

        gui.addPane(pane);

        gui.show(player);
    }
}
