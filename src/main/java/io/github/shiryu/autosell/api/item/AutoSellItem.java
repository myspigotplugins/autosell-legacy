package io.github.shiryu.autosell.api.item;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class AutoSellItem {

    private final Material material;
    private final int defaultStackSize;

    private boolean enabled = true;

    public AutoSellItem(@NotNull final Material material, final int defaultStackSize){
        this.material = material;
        this.defaultStackSize = defaultStackSize;
    }

    @NotNull
    public Material getMaterial() {
        return material;
    }

    public int getDefaultStackSize() {
        return defaultStackSize;
    }

    @NotNull
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
