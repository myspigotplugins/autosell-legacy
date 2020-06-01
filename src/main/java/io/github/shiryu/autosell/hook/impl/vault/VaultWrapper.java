package io.github.shiryu.autosell.hook.impl.vault;

import io.github.shiryu.autosell.hook.Wrapper;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VaultWrapper implements Wrapper {

    private final Economy economy;

    public VaultWrapper(@NotNull final Economy economy){
        this.economy = economy;
    }


    public void addMoney(@NotNull final Player player, final int money){
        this.economy.depositPlayer(
                player,
                money
        );
    }

    public void removeMoney(@NotNull final Player player, final int money){
        this.economy.withdrawPlayer(
                player,
                money
        );
    }

    public int getMoney(@NotNull final Player player){
        return (int) this.economy.getBalance(player);
    }
}
