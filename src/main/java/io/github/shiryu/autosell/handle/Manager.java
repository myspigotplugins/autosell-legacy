package io.github.shiryu.autosell.handle;

import io.github.shiryu.autosell.AutoSell;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Manager<T extends Node> {

    @NotNull
    List<T> getNodes();

    T parse(@NotNull final String string);

    void prepareFor(@NotNull final AutoSell plugin);
}
