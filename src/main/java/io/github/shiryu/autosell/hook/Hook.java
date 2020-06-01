package io.github.shiryu.autosell.hook;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface Hook<T extends Wrapper> {

    boolean initiate(@NotNull final Plugin plugin);

    @NotNull
    T create();
}
