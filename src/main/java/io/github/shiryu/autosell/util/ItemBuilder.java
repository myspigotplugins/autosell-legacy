package io.github.shiryu.autosell.util;


import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a chainable builder for {@link ItemStack}s in {@link org.bukkit.Bukkit}
 * <br>
 * @author MiniDigger, computerwizjared
 * @version 1.2
 */
public class ItemBuilder extends ItemStack {
    /**
     * Initializes the builder with the given {@link Material}
     *
     * @param mat the {@link Material} to start the builder from
     * @since 1.0
     */
    public ItemBuilder(final Material mat) {
        super(mat);
    }

    /**
     * Inits the builder with the given {@link ItemStack}
     *
     * @param is the {@link ItemStack} to start the builder from
     * @since 1.0
     */
    public ItemBuilder(final ItemStack is) {
        super(is);
    }

    /**
     * Changes the amount of the {@link ItemStack}
     *
     * @param amount the new amount to set
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder amount(final int amount) {
        setAmount(amount);
        return this;
    }

    /**
     * Changes the display name of the {@link ItemStack}
     *
     * @param name the new display name to set
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder name(final String name) {
        final ItemMeta meta = getItemMeta();
        meta.setDisplayName(new Colored(name).value());
        setItemMeta(meta);
        return this;
    }

    /**
     * Adds a new line to the lore of the {@link ItemStack}
     *
     * @param text the new line to add
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder lore(final String text) {
        final ItemMeta meta = getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.add(new Colored(text).value());
        meta.setLore(lore);
        setItemMeta(meta);
        return this;
    }

    /**
     * Adds a new line to the lore of the {@link ItemStack}
     *
     * @param text the new line to add
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder lore(final String... text) {
        Arrays.stream(text).forEach(this::lore);
        return this;
    }

    /**
     * Adds a new line to the lore of the {@link ItemStack}
     *
     * @param text the new line to add
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder lore(final List<String> text) {
        text.forEach(this::lore);
        return this;
    }

    /**
     * Changes the durability of the {@link ItemStack}
     *
     * @param durability the new durability to set
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder durability(final int durability) {
        setDurability((short) durability);
        return this;
    }

    /**
     * Changes the data of the {@link ItemStack}
     *
     * @param data the new data to set
     * @return this builder for chaining
     * @since 1.0
     */
    @SuppressWarnings("deprecation")
    public ItemBuilder data(final int data) {
        setData(new MaterialData(getType(), (byte) data));
        return this;
    }

    /**
     * Adds an {@link Enchantment} with the given level to the {@link ItemStack}
     *
     * @param enchantment the enchantment to add
     * @param level       the level of the enchantment
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder enchantment(final Enchantment enchantment, final int level) {
        addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Adds an {@link Enchantment} with the level 1 to the {@link ItemStack}
     *
     * @param enchantment the enchantment to add
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder enchantment(final Enchantment enchantment) {
        addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    /**
     * Changes the {@link Material} of the {@link ItemStack}
     *
     * @param material the new material to set
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder type(final Material material) {
        setType(material);
        return this;
    }

    /**
     * Clears the lore of the {@link ItemStack}
     *
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder clearLore() {
        final ItemMeta meta = getItemMeta();
        meta.setLore(new ArrayList<>());
        setItemMeta(meta);
        return this;
    }

    /**
     * Clears the list of {@link Enchantment}s of the {@link ItemStack}
     *
     * @return this builder for chaining
     * @since 1.0
     */
    public ItemBuilder clearEnchantments() {
        getEnchantments().keySet().forEach(this::removeEnchantment);
        return this;
    }

    /**
     * Sets the {@link Color} of a part of leather armor
     *
     * @param color the {@link Color} to use
     * @return this builder for chaining
     * @since 1.1
     */
    public ItemBuilder color(Color color) {
        if (getType() == Material.LEATHER_BOOTS || getType() == Material.LEATHER_CHESTPLATE || getType() == Material.LEATHER_HELMET
                || getType() == Material.LEATHER_LEGGINGS) {
            LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
            meta.setColor(color);
            setItemMeta(meta);
            return this;
        } else {
            throw new IllegalArgumentException("color() only applicable for leather armor!");
        }
    }

    public ItemBuilder flag(ItemFlag... flag) {
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(flag);
        setItemMeta(meta);
        return this;
    }
}
