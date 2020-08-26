package io.github.shiryu.autosell.handle.impl;

import com.cryptomorin.xseries.XMaterial;
import io.github.shiryu.autosell.AutoSell;
import io.github.shiryu.autosell.handle.Manager;
import io.github.shiryu.autosell.handle.Node;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Prices implements Manager<Prices.PriceNode> {

    private List<PriceNode> nodes = new ArrayList<>();

    @Override
    public PriceNode parse(@NotNull String string) {
        final String[] split = string.split(":");

        if (split.length == 2){
            final Material material = Material.matchMaterial(split[0]);

            if (material == null) return null;

            final int price = Integer.parseInt(split[1]);

            return new PriceNode(
                    material,
                    price
            );
        }

        return null;
    }

    public void prepareFor(@NotNull final AutoSell plugin){
        if (plugin.getConfigs().ESSENTIALS_PRICES){
            final File worth = new File("plugins/Essentials/worth.yml");

            if (!worth.exists()) return;

            FileConfiguration config = YamlConfiguration.loadConfiguration(worth);

            for (String key : config.getConfigurationSection("worth").getKeys(false)){
                final int price = (int) config.getDouble("worth." + key + ".0");

                nodes.add(
                        parse(key + ":" + price)
                );
            }
        }else{
            nodes = plugin.getConfigs().PRICES
                    .stream()
                    .map(s -> parse(s))
                    .collect(Collectors.toList());
        }
    }

    @NotNull
    public int priceOf(@NotNull final Material material){
        return this.nodes.stream()
                .filter(x -> x.getMaterial() == material)
                .findAny()
                .orElse(null)
                .getPrice();
    }

    @Override
    public @NotNull List<PriceNode> getNodes() {
        return this.nodes;
    }

    public class PriceNode implements Node {

        private final Material material;
        private final int price;

        private PriceNode(@NotNull final Material material, final int price){
            this.material = material;
            this.price = price;
        }

        public Material getMaterial() {
            return material;
        }

        public int getPrice() {
            return price;
        }
    }
}
