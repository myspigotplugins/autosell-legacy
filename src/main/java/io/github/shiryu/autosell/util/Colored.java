package io.github.shiryu.autosell.util;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class Colored {

    private final String text;

    public Colored(@NotNull final String text){
        this.text = ChatColor.translateAlternateColorCodes('&', text);
    }

    @NotNull
    public String value(){
        return this.text;
    }
}
