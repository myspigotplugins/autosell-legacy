package io.github.shiryu.autosell.util.loader;

import io.github.shiryu.autosell.AutoSell;
import io.github.shiryu.autosell.api.item.AutoSellItem;
import io.github.shiryu.autosell.api.player.User;
import io.github.shiryu.autosell.util.FileUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PlayerLoader {

    private final UUID uuid;

    public PlayerLoader(@NotNull final UUID uuid){
        this.uuid = uuid;
    }

    @NotNull
    public User loadFrom(@NotNull final File file){
        final User user = new User(this.uuid);

        final FileConfiguration config = FileUtil.getInstance().loadFile(file);

        final List<AutoSellItem> items = new ArrayList<>();

        if (config.get("items") != null){
            final ConfigurationSection section = config.getConfigurationSection("items");

            if (section.getKeys(false) != null){
                for (String key : section.getKeys(false)){
                    final Material material = Material.matchMaterial(section.getString(key + ".material"));
                    final int stackSize = section.getInt(key + ".stackSize");
                    final boolean enabled = section.getBoolean(key + ".enabled");

                    final AutoSellItem item = new AutoSellItem(
                            material,
                            stackSize
                    );

                    item.setEnabled(enabled);

                    items.add(item);
                }
            }
        }

        user.setItems(items);

        return user;
    }
}
