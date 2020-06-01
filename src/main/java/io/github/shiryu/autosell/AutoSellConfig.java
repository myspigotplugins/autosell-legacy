package io.github.shiryu.autosell;

import io.github.portlek.configs.annotations.*;
import io.github.portlek.configs.bukkit.BukkitManaged;
import io.github.portlek.configs.bukkit.BukkitSection;
import io.github.shiryu.autosell.menu.impl.ItemEditMenu;

import java.util.Arrays;
import java.util.List;

@Config(
        location = "%basedir%/AutoSell/",
        value= "config"
)
public class AutoSellConfig extends BukkitManaged {

    /*
        namings & prices
     */
    @Property(value = "PRICES.ESSENTIALS")
    public boolean ESSENTIALS_PRICES = false;

    @Property(value = "PRICES.LIST")
    public List<String> PRICES = Arrays.asList(
            "IRON_INGOT:256"
    );

    @Property(value = "NAMINGS")
    public List<String> NAMINGS = Arrays.asList(
            "IRON_INGOT:Iron"
    );

    /*
        messages
     */

    @Property(value = "MESSAGES.ITEM_SELL")
    public String ITEM_SELL = "&bAutoSell&7► &a%item% &7is successfully sold with price &a%price%";

    @Property(value = "MESSAGES.ITEM_CREATED")
    public String ITEM_CREATED = "&bAutoSell&7► &aItem successfully created!";

    @Property(value = "MESSAGES.ITEM_DELETED")
    public String ITEM_DELETED = "&bAutoSell&7► &aItem successfully removed!";

    @Property(value = "MESSAGES.INVENTORY_FULL")
    public String INVENTORY_FULL = "&bAutoSell&7► &aYour Inventory is full!";

    @Property(value = "MESSAGES.ITEM_SET_STACK")
    public String ITEM_SET_STACK = "&bAutoSell&7► &aItem stack size successfully changed with &e%stack%";

    @Property(value = "MESSAGES.ITEM_SET_STATUS")
    public String ITEM_SET_STATUS = "&bAutoSell&7► &aItem status successfully changed to %status%";

    /*
        settings
     */
    @Property(value = "SETTINGS.DEFAULT_STACK_SIZE")
    public int DEFAULT_STACK_SIZE = 64;

    @Property(value = "SETTINGS.AUTO_PICKUP_BLACKLIST")
    public List<String> BLACK_LIST = Arrays.asList(
            "ICE",
            "PACKET_ICE"
    );

    @Property(value = "SETTINGS.BOOLS.ENABLED")
    public String ENABLED = "&aEnabled";

    @Property(value = "SETTINGS.BOOLS.DISABLED")
    public String DISABLED = "&cDisabled";

    /*
        menus
     */

    @Instance
    public MenuSection menus = new MenuSection();


    @Section(value = "MENU")
    public class MenuSection extends BukkitSection{

        @Instance
        public MainMenuSection mainMenuSection = new MainMenuSection();

        @Instance
        public ItemListMenuSection itemListMenuSection = new ItemListMenuSection();

        @Instance
        public ItemEditMenuSection itemEditMenuSection = new ItemEditMenuSection();

        @Section(value = "MENU.ITEM_EDIT")
        public class ItemEditMenuSection extends BukkitSection{

            @Property
            public String TITLE = "&b&lAutoSell &7- &eItem Edit";

            @Property
            public int ROWS = 1;

            @Instance
            public ItemsSection items = new ItemsSection();

            @Section(value = "MENU.ITEM_EDIT.ITEMS")
            public class ItemsSection extends BukkitSection{

                @Instance
                public StatusItemSection statusItemSection = new StatusItemSection();

                @Instance
                public SetStackSizeItemSection setStackSizeItemSection = new SetStackSizeItemSection();

                @Section(value = "MENU.ITEM_EDIT.ITEMS.STATUS")
                public class StatusItemSection extends BukkitSection{

                    @Property
                    public String NAME = "&aReverse the Status";

                    @Property
                    public List<String> LORE = Arrays.asList(
                            "",
                            "&7- &eStatus: %status%"
                    );

                    @Property
                    public String MATERIAL = "COMMAND_BLOCK";
                }

                @Section(value = "MENU.ITEM_EDIT.ITEMS.SET_STACK_SIZE")
                public class SetStackSizeItemSection extends BukkitSection{

                    @Property
                    public String NAME = "&aSet the stack size";

                    @Property
                    public List<String> LORE = Arrays.asList(
                            "",
                            "&7- &eStack Size: &a%stack%"
                    );

                    @Property
                    public String MATERIAL = "GOLD_INGOT";
                }
            }
        }

        @Section(value = "MENU.ITEM_LIST")
        public class ItemListMenuSection extends BukkitSection{

            @Property
            public String TITLE = "&b&lYour items";

            @Property
            public int ROWS = 6;

            @Instance
            public ItemsSection items = new ItemsSection();

            @Section(value = "MENU.ITEM_LIST.ITEMS")
            public class ItemsSection extends BukkitSection{

                @Property
                public String NAME = "&a%item%";

                @Property
                public List<String> LORE = Arrays.asList(
                        "",
                        "&7- &eItem: &a%item%",
                        "&7- &eStack Size: &a%stack%",
                        "&7- &eEnabled: %enabled%"
                );

            }
        }

        @Section(value = "MENU.MAIN")
        public class MainMenuSection extends BukkitSection{

            @Property
            public String TITLE = "&b&lAutoSell &7- &eMain Menu";

            @Property
            public int ROWS = 1;

            @Instance
            public ItemsSection items = new ItemsSection();

            @Section(value = "MENU.MAIN.ITEMS")
            public class ItemsSection extends BukkitSection{

                @Instance
                public CreateItemSection createItemSection = new CreateItemSection();

                @Instance
                public ListItemsSection listItemsSection = new ListItemsSection();

                @Instance
                public RemoveItemSection removeItemSection = new RemoveItemSection();

                @Section(value = "MENU.MAIN.ITEMS.CREATE")
                public class CreateItemSection extends BukkitSection{

                    @Property
                    public String NAME = "&aCreate new item";

                    @Property
                    public List<String> LORE = Arrays.asList(
                            "",
                            "&7- &eRight click to use"
                    );

                    @Property
                    public String MATERIAL = "ANVIL";
                }

                @Section(value = "MENU.MAIN.ITEMS.LIST")
                public class ListItemsSection extends BukkitSection{

                    @Property
                    public String NAME = "&aList items";

                    @Property
                    public List<String> LORE = Arrays.asList(
                            "",
                            "&7- &eRight click to use"
                    );

                    @Property
                    public String MATERIAL = "CHEST";
                }

                @Section(value = "MENU.MAIN.ITEMS.REMOVE")
                public class RemoveItemSection extends BukkitSection{

                    @Property
                    public String NAME = "&aRemove an item";

                    @Property
                    public List<String> LORE = Arrays.asList(
                            "",
                            "&7- &eRight click to use"
                    );

                    @Property
                    public String MATERIAL = "BARRIER";
                }
            }
        }
    }
}
