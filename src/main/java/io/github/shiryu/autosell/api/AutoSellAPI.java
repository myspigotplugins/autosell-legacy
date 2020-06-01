package io.github.shiryu.autosell.api;


import io.github.shiryu.autosell.AutoSell;
import io.github.shiryu.autosell.api.player.User;
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
        final Map<UUID, User> users = AutoSell.getInstance().getUsers();

        if (!users.containsKey(uuid)){
            final User user = new User(uuid);
            users.put(uuid, user);

            user.save();
        }

        return Optional.ofNullable(
                users.get(uuid)
        );
    }



    public static synchronized AutoSellAPI getInstance(){
        if (instance == null) instance = new AutoSellAPI();

        return instance;
    }
}
