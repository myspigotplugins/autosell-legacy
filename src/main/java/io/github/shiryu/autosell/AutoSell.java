package io.github.shiryu.autosell;

import com.google.gson.Gson;
import io.github.portlek.database.MapEntry;
import io.github.portlek.database.SQL;
import io.github.portlek.database.database.SQLite;
import io.github.portlek.database.sql.SQLBasic;
import io.github.shiryu.autosell.api.item.AutoSellItem;
import io.github.shiryu.autosell.api.player.User;
import io.github.shiryu.autosell.command.AutoSellCommand;
import io.github.shiryu.autosell.handle.impl.Namings;
import io.github.shiryu.autosell.handle.impl.Prices;
import io.github.shiryu.autosell.hook.impl.vault.VaultHook;
import io.github.shiryu.autosell.listener.PlayerPickup;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class AutoSell extends JavaPlugin {

    private static AutoSell instance;

    private AutoSellConfig configs;

    private final Map<UUID, User> users = new HashMap<>();
    private SQL sql;

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
        loadSql();
        loadUsers();
        loadHooks();
    }

    @Override
    public void onDisable(){
        this.sql.getDatabase().disconnect();
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
        final Gson gson = new Gson();

        this.users.clear();

        final List<String> players = this.sql.listGet("uuid", "uuid", "!=", "x", "autosell");
        final List<String> items =  this.sql.listGet("items", "uuid", "!=", "x", "autosell");

        System.out.println(players.size());

        int counter = 0;

        for (int i = 0; i < players.size(); i++){
            UUID uuid;
            String item = items.get(i);

            try{
                uuid = UUID.fromString(players.get(i));
            }catch (Exception e){
                continue;
            }

            final User user = new User(uuid);

            final List<AutoSellItem> xd = gson.fromJson(
                    item,
                    List.class
            );

            user.setItems(xd);

            this.users.put(
                    uuid,
                    user
            );

            counter++;
        }


        System.out.println("[autosell] " + counter + " user has been loaded!");
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

    private void loadSql(){
        final SQLite lite = new SQLite(this, "autosell.db");

        lite.connect();

        this.sql = new SQLBasic(lite);

        this.sql.createTable(
                "autosell",
                Arrays.asList(
                        new MapEntry<>(
                                "'uuid'",
                                "VARCHAR(128) NOT NULL"
                        ),
                        new MapEntry<>(
                                "'items'",
                                "VARCHAR(255) NOT NULL"
                        )
                )
        );
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

    @NotNull
    public SQL getSql() {
        return this.sql;
    }

    public AutoSellConfig getConfigs() {
        return configs;
    }

    public static AutoSell getInstance() {
        return instance;
    }
}
