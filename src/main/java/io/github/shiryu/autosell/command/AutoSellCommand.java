package io.github.shiryu.autosell.command;

import io.github.shiryu.autosell.menu.Menu;
import io.github.shiryu.autosell.menu.impl.MainMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AutoSellCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;

        final Player player = (Player) sender;

        final Menu menu = new MainMenu();

        menu.openFor(player);

        return true;
    }
}
