package io.github.shiryu.autosell;

import io.github.shiryu.autosell.api.player.User;
import io.github.shiryu.autosell.command.AutoSellCommand;
import io.github.shiryu.autosell.handle.impl.Namings;
import io.github.shiryu.autosell.handle.impl.Prices;
import io.github.shiryu.autosell.hook.impl.vault.VaultHook;
import io.github.shiryu.autosell.listener.PlayerPickup;
import io.github.shiryu.autosell.util.FileUtil;
import io.github.shiryu.autosell.util.loader.PlayerLoader;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.File;
import java.util.*;


public class AutoSell extends JavaPlugin {

    private static AutoSell instance;

    private AutoSellConfig configs;

    private final Map<UUID, User> users = new HashMap<>();

    private Prices prices;
    private Namings namings;

    private VaultHook vaultHook;



    @Override
    public void onEnable(){
        if (instance != null) throw new IllegalStateException("[AutoSell] cannot be enable twice!");

        instance = this;

        this.configs = new AutoSellConfig();
        this.configs.load();

        registerListeners(
                new PlayerPickup()
        );

        getCommand("autosell").setExecutor(new AutoSellCommand());

        if (configs.AUTO_SAVE){
            getServer().getScheduler().runTaskTimer(
                    this,
                    () ->{
                        this.users.values().forEach(User::save);
                    },
                    configs.AUTO_SAVE_TIME * 20L,
                    configs.AUTO_SAVE_TIME * 20L
            );
        }

        loadManagers();
        loadUsers();
        loadHooks();
    }


    private void registerListeners(final Listener... listeners){
        if (listeners.length == 0) return;

        for (Listener listener : listeners){
            Bukkit.getPluginManager().registerEvents(
                    listener,
                    this
            );
        }
    }

    private void loadUsers(){
        this.users.clear();

        final File playersDir = FileUtil.getInstance().createDirectoryIfDoNotExists("/players/", getDataFolder());

        if (playersDir.listFiles() == null) return;

        for (File file : playersDir.listFiles()){
            try{
                final UUID uuid = UUID.fromString(file.getName().replaceAll(".yml", ""));

                this.users.put(
                        uuid,
                        new PlayerLoader(uuid).loadFrom(file)
                );
            }catch (Exception e){
                continue;
            }
        }
    }

    private void loadHooks(){
        this.vaultHook = new VaultHook();
        this.vaultHook.initiate(this);
    }

    private void loadManagers(){
        this.prices = new Prices();
        this.prices.prepareFor(this);

        this.namings = new Namings();
        this.namings.prepareFor(this);
    }


    public Map<UUID, User> getUsers() {
        return users;
    }

    public VaultHook getVaultHook() {
        return vaultHook;
    }

    public Namings getNamings() {
        return namings;
    }

    public Prices getPrices() {
        return prices;
    }


    public AutoSellConfig getConfigs() {
        return configs;
    }

    public static AutoSell getInstance() {
        return instance;
    }
}
