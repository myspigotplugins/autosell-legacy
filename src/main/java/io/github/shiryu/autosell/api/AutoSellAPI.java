package io.github.shiryu.autosell.api;


import io.github.shiryu.autosell.AutoSell;
import io.github.shiryu.autosell.api.item.AutoSellItem;
import io.github.shiryu.autosell.api.player.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class AutoSellAPI {

    private static AutoSellAPI instance;

    private AutoSellAPI(){

    }

    @NotNull
    public Optional<User> findUser(@NotNull final UUID uuid){
        if (!AutoSell.getInstance().getUsers().containsKey(uuid)){
            final User user = new User(uuid);

            AutoSell.getInstance().getUsers().put(uuid, user);

            user.save();
        }

        return Optional.ofNullable(
                AutoSell.getInstance().getUsers().get(uuid)
        );
    }

    @NotNull
    public Optional<AutoSellItem> findItemFromNaming(@NotNull final User user, @NotNull final String naming){
        return Optional.ofNullable(
                user.getItems()
                .stream()
                .filter(x ->{
                    final Material namingX = AutoSell.getInstance().getNamings().materialOf(naming).orElse(null);

                    return x.getMaterial() == namingX;
                })
                .findAny()
                .orElse(null)
        );
    }

    @NotNull
    public void findAndSetStack(@NotNull final User user, @NotNull final String naming, @NotNull final int stack){
        user.getItems()
                .stream()
                .filter(x ->{
                    final Material namingX = AutoSell.getInstance().getNamings().materialOf(naming).orElse(null);

                    return x.getMaterial() == namingX;
                })
                .findAny()
                .orElse(null)
                .setDefaultStackSize(stack);
    }

    @NotNull
    public void findAndSetEnabled(@NotNull final User user, @NotNull final String naming, @NotNull final boolean enabled){
        user.getItems()
                .stream()
                .filter(x -> {
                    final Material namingX = AutoSell.getInstance().getNamings().materialOf(naming).orElse(null);

                    return x.getMaterial() == namingX;
                })
                .findAny()
                .orElse(null)
                .setEnabled(enabled);
    }



    public static synchronized AutoSellAPI getInstance(){
        if (instance == null) instance = new AutoSellAPI();

        return instance;
    }
}
