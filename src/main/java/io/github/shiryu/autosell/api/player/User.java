package io.github.shiryu.autosell.api.player;

import com.google.gson.Gson;
import io.github.portlek.database.MapEntry;
import io.github.portlek.database.SQL;
import io.github.shiryu.autosell.AutoSell;
import io.github.shiryu.autosell.api.item.AutoSellItem;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class User {

    private final UUID uuid;

    private List<AutoSellItem> items = new ArrayList<>();

    public User(@NotNull final UUID uuid){
        this.uuid = uuid;
    }

    public @NotNull UUID getUUID() {
        return this.uuid;
    }

    public @NotNull List<AutoSellItem> getItems() {
        return this.items;
    }

    public void setItems(@NotNull final List<AutoSellItem> items) {
        this.items = items;
    }

    public void save() {
        if (items.size() == 0) return;

        final Gson gson = new Gson();
        final SQL sql = AutoSell.getInstance().getSql();

        System.out.println(sql.getDatabase().isConnected());

        if (!sql.exists("uuid", this.uuid.toString(), "autosell")){
            sql.insertData(
                    "autosell",
                    Arrays.asList(
                            new MapEntry<>(
                                    "uuid",
                                    this.uuid.toString()
                            ),
                            new MapEntry<>(
                                    "items",
                                    gson.toJson(this.items)
                            )
                    )
            );

            return;
        }

        sql.set("items", gson.toJson(this.items), "uuid", "=", this.uuid.toString(), "autosell");

    }
}
