package at.mrpuffi.sellsystem.gui;

import at.mrpuffi.sellsystem.Main;
import at.mrpuffi.sellsystem.data.SellableItem;
import de.waischbrot.invlib.basic.Button;
import de.waischbrot.invlib.basic.RubyInventory;
import de.waischbrot.invlib.pagemenu.PageHandler;
import de.waischbrot.itembuilder.ItemBuilder;
import de.waischbrot.messages.MessageUtil;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class SellMenu extends RubyInventory {

    private final Economy economy;
    private final Main plugin;
    private final Collection<SellableItem> items;
    private final PageHandler pagination;

    public SellMenu(final @NotNull Player player, @NotNull final Main plugin, final @NotNull Collection<SellableItem> items, final @NotNull String title) {
        super(player, "sellwood", title, 4);
        this.plugin = plugin;
        this.economy = plugin.getEconomy();
        this.items = items;
        this.pagination = new PageHandler(this);
        registerPages();
    }

    private void registerPages() {
        pagination.registerPageSlotsBetween(10, 16);
        pagination.registerPageSlotsBetween(19, 25);
        //Items adden
        List<String> lore = new ArrayList<>();
        for (SellableItem item : items) {
            lore.clear();
            lore.add(MessageUtil.getMessageColour("&8"));
            lore.add(MessageUtil.getMessageColour("&8× &7Preis: &b" + (item.price * 64) + "$&8/&764"));
            lore.add(MessageUtil.getMessageColour("&8"));
            lore.add(MessageUtil.getMessageColour("&7Linksklick &8× &e1x Verkaufen"));
            lore.add(MessageUtil.getMessageColour("&7Rechtsklick &8× &e64x Verkaufen"));
            pagination.addButton(new Button(new ItemBuilder(item.type)
                    .setDisplayName(item.displayName)
                    .setLore(lore)
                    .build())
                    .setClickEventConsumer(e -> {
                        if (e.isLeftClick()) {
                            sellItems(item.type, 1, item.price);
                        } else if (e.isRightClick()) {
                            sellItems(item.type, 64, item.price);
                        }
                    }));
        }
    }

    @Override
    public void handleInventoryOpenEvent(final InventoryOpenEvent event) {
        ItemStack border = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(MessageUtil.getMessageColour("&8")).build();
        fillRow(border, 0);
        fillRow(border, 3);
        fillColumn(border, 0);
        fillColumn(border, 8);

        //Page Items + Back
        addButton(3, 4, new Button(new ItemBuilder(Material.BARRIER)
                .setDisplayName(MessageUtil.getMessageColour("&8» &cZurück"))
                .build())
                .setClickEventConsumer(e -> new MainMenu(player, plugin).open()));

        if (!pagination.isFirstPage()) {
            addButton(3, 2, new Button(new ItemBuilder(Material.ARROW)
                    .setDisplayName(MessageUtil.getMessageColour("&8» &6Vorherige Seite"))
                    .build())
                    .setClickEventConsumer(e -> {
                        pagination.previousPage();
                        open();
                    }));
        }

        if (!pagination.isLastPage()) {
            addButton(3, 6, new Button(new ItemBuilder(Material.ARROW)
                    .setDisplayName(MessageUtil.getMessageColour("&8» &6Nächste Seite"))
                    .build())
                    .setClickEventConsumer(e -> {
                        pagination.nextPage();
                        open();
                    }));
        }

        pagination.update();
    }

    public void sellItems(final @NotNull Material material, final int amount, final double price) {
        ItemStack itemStack = new ItemStack(material, amount);

        if (player.getInventory().containsAtLeast(itemStack, amount)) {

            final double reward = price * amount;
            EconomyResponse response = economy.depositPlayer(player, reward);

            if (response.transactionSuccess()) {

                player.getInventory().removeItem(itemStack);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
                player.sendMessage(MessageUtil.getMessageColour("&7Du hast &e" + amount + "x " + material.name().toUpperCase(Locale.ROOT) + " &7für &a" + reward + "$ &bverkauft&7!"));

            } else {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
                player.sendMessage(MessageUtil.getMessageColour("&cEs ist etwas schiefgelaufen, die Transaktion wurde daher abgebrochen!"));
            }
        } else {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
            player.sendMessage(MessageUtil.getMessageColour("&cDu hast nicht genügend Items!"));
        }
    }
}
