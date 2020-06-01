package io.github.shiryu.autosell.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class EventBase extends Event implements Cancellable {

    private static final HandlerList handlers;

    static{
        handlers = new HandlerList();
    }

    private final Player player;

    private boolean cancellable;

    public EventBase(@NotNull final Player player){
        this.player = player;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return this.cancellable;
    }

    @Override
    public void setCancelled(@NotNull final boolean cancel) {
        this.cancellable = cancel;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
