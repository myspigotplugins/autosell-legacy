package io.github.shiryu.autosell;

import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.Property;
import io.github.portlek.configs.bukkit.BukkitManaged;

import java.util.Arrays;
import java.util.List;

@Config(
        location = "%basedir%/AutoSell/",
        value= "config"
)
public class AutoSellConfig extends BukkitManaged {

    @Property(value = "PRICES.ESSENTIALS")
    public boolean ESSENTIALS_PRICES = false;

    @Property(value = "PRICES.LIST")
    public List<String> PRICES = Arrays.asList(
            "IRON_INGOT:256"
    );

    @Property(value = "NAMINGS")
    public List<String> NAMINGS = Arrays.asList(
            "IRON_INGOT:Iron"
    );

    @Property(value = "MESSAGES.ITEM_SELL")
    public String ITEM_SELL = "&bAutoSell&7► &a%item% &7is successfully sold with price &a%price%";
}
