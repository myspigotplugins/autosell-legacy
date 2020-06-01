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

        loadManagers();
        loadSql();
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

    private void loadUser(@NotNull final UUID uuid){
        final User user = new User(uuid);
    }
    private void loadUsers(){
        this.users.clear();

        final Gson gson = new Gson();

        final List<UUID> players = this.sql.listGet("uuid", "uuid", "!=", "x", "autosell");

        players.forEach(player ->{
            final User user = new User(player);

            final List<AutoSellItem> items = gson.fromJson(
                    this.sql.get("items", "uuid", "=", player.toString(), "autosell", "x"),
                    List.class
            );

            user.setItems(items);

            this.users.put(
                    player,
                    user
            );
        });
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
        this.sql = new SQLBasic(new SQLite(this, "autosell.db"));

        this.sql.createTable(
                "autosell",
                Arrays.asList(
                        new MapEntry<>(
                                "uuid",
                                "VARCHAR(128) NOT NULL"
                        ),
                        new MapEntry<>(
                                "items",
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
        return sql;
    }

    public AutoSellConfig getConfigs() {
        return configs;
    }

    public static AutoSell getInstance() {
        return instance;
    }
}
