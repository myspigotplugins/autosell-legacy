package io.github.shiryu.autosell.api.player;

import io.github.shiryu.autosell.AutoSell;
import io.github.shiryu.autosell.api.AutoSellAPI;
import io.github.shiryu.autosell.api.item.AutoSellItem;
import io.github.shiryu.autosell.util.FileUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
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

        final File file = AutoSellAPI.getInstance().findPlayerDirectory(this.uuid);

        final FileConfiguration config = FileUtil.getInstance().loadFile(file);

        if (config.get("items") == null) config.createSection("items");

        final ConfigurationSection section = config.getConfigurationSection("items");

        for (int i = 0; i < this.items.size(); i++){
            try{
                AutoSellItem item = this.items.get(i);

                section.set(i + ".material", item.getMaterial().name());
                section.set(i + ".stackSize", item.getDefaultStackSize());
                section.set(i + ".enabled", item.isEnabled());
            }catch (Exception e){
                continue;
            }
        }

        FileUtil.getInstance().saveFile(file, config);
        FileUtil.getInstance().loadFile(file);
    }
}
