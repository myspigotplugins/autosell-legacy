package io.github.shiryu.autosell.hook.impl.vault;

import io.github.shiryu.autosell.hook.Hook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class VaultHook implements Hook<VaultWrapper>{

    private Economy economy;

    @Override
    public boolean initiate(@NotNull Plugin plugin) {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        final RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(Economy.class);

        if (economyProvider != null) {
            this.economy = economyProvider.getProvider();
        }

        return (this.economy != null);
    }

    @Override
    public @NotNull VaultWrapper create() {
        return new VaultWrapper(this.economy);
    }
}
