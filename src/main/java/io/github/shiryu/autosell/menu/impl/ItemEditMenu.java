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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.dnd.Autoscroll;
import java.util.List;

public class ItemEditMenu implements Menu {

    private final AutoSellItem item;

    public ItemEditMenu(@NotNull final AutoSellItem item){
        this.item = item;
    }

    @Override
    public void openFor(@NotNull Player player) {
        final int rows = AutoSell.getInstance().getConfigs().menus.itemEditMenuSection.ROWS;

        final Gui gui = new Gui(
                AutoSell.getInstance(),
                rows,
                new Colored(
                        AutoSell.getInstance().getConfigs().menus.itemEditMenuSection.TITLE
                ).value()
        );

        final StaticPane pane = new StaticPane(9, rows);

        pane.addItem(
                new GuiItem(
                        new ItemBuilder(XMaterial.matchXMaterial(AutoSell.getInstance().getConfigs().menus.itemEditMenuSection.items.setStackSizeItemSection.MATERIAL).orElse(null).parseItem())
                                .name(AutoSell.getInstance().getConfigs().menus.itemEditMenuSection.items.setStackSizeItemSection.NAME)
                                .lore(AutoSell.getInstance().getConfigs().menus.itemEditMenuSection.items.setStackSizeItemSection.LORE),
                        (click ->{
                            click.setCancelled(true);

                            final Player clicked = (Player) click.getWhoClicked();

                            AutoSellAPI.getInstance().findUser(clicked.getUniqueId()).ifPresent(user ->{
                                AnvilGUI anvil = new AnvilGUI.Builder()
                                        .plugin(AutoSell.getInstance())
                                        .item(new ItemStack(XMaterial.PAPER.parseMaterial()))
                                        .text("Input...")
                                        .onComplete((anvilClicker, reply) ->{
                                            try{
                                                final int stack = Integer.parseInt(reply);

                                                AutoSellAPI.getInstance().findAndSetStack(
                                                        user,
                                                        reply,
                                                        stack
                                                );

                                                user.save();

                                                anvilClicker.closeInventory();

                                                anvilClicker.sendMessage(
                                                        new Colored(
                                                                AutoSell.getInstance().getConfigs().ITEM_SET_STACK
                                                                .replaceAll("%stack%", String.valueOf(stack))
                                                        ).value()
                                                );
                                            }catch(NumberFormatException ex){
                                                anvilClicker.closeInventory();
                                            }

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
                        new ItemBuilder(XMaterial.matchXMaterial(AutoSell.getInstance().getConfigs().menus.itemEditMenuSection.items.statusItemSection.MATERIAL).orElse(null).parseItem())
                                .name(AutoSell.getInstance().getConfigs().menus.itemEditMenuSection.items.statusItemSection.NAME)
                                .lore(AutoSell.getInstance().getConfigs().menus.itemEditMenuSection.items.statusItemSection.LORE),
                        (click ->{
                            click.setCancelled(true);

                            final Player clicked = (Player) click.getWhoClicked();

                            AutoSellAPI.getInstance().findUser(clicked.getUniqueId()).ifPresent(user ->{
                                final boolean current = this.item.isEnabled();

                                final boolean set = !current;

                                AutoSellAPI.getInstance().findAndSetEnabled(
                                        user,
                                        AutoSell.getInstance().getNamings().namingOf(item.getMaterial()),
                                        set
                                );

                                user.save();

                                clicked.closeInventory();

                                String message = AutoSell.getInstance().getConfigs().ITEM_SET_STATUS;


                                if (set){
                                    message = message.replaceAll("%status%", AutoSell.getInstance().getConfigs().ENABLED);
                                }else{
                                    message = message.replaceAll("%status%", AutoSell.getInstance().getConfigs().DISABLED);
                                }

                                clicked.sendMessage(
                                        new Colored(
                                                message
                                        ).value()
                                );
                            });
                        })
                ),
                4,
                0
        );

        gui.addPane(pane);

        gui.show(player);
    }
}
