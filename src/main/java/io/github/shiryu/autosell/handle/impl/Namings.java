package io.github.shiryu.autosell.handle.impl;

import com.cryptomorin.xseries.XMaterial;
import io.github.shiryu.autosell.AutoSell;
import io.github.shiryu.autosell.handle.Manager;
import io.github.shiryu.autosell.handle.Node;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Namings implements Manager<Namings.NamingNode> {

    private  List<NamingNode> nodes = new ArrayList<>();


    @Override
    public NamingNode parse(@NotNull String string) {
        final String[] split = string.split(":");

        if (split.length == 2){
            final Material material = XMaterial.matchXMaterial(split[0]).orElse(null).parseMaterial();

            return new NamingNode(
                    material,
                    split[1]
            );
        }

        return null;
    }

    @Override
    public void prepareFor(@NotNull AutoSell plugin) {
        this.nodes = plugin.getConfigs().NAMINGS
                .stream()
                .map(s -> parse(s))
                .collect(Collectors.toList());
    }

    @NotNull
    public String namingOf(@NotNull final Material material){
        return this.nodes.stream()
                .filter(x -> x.getMaterial() == material)
                .findAny()
                .orElse(null)
                .getNaming();
    }

    @Override
    public @NotNull List<NamingNode> getNodes() {
        return this.nodes;
    }

    public class NamingNode implements Node{

        private final Material material;
        private final String naming;

        public NamingNode(@NotNull final Material material, @NotNull final String naming){
            this.material = material;
            this.naming = naming;
        }

        public Material getMaterial() {
            return material;
        }

        public String getNaming() {
            return naming;
        }
    }
}
